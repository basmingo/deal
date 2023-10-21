package ru.neoflex.deal.model.dao

import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.configuration.deal.tables.Credit._
import ru.neoflex.deal.model.mappers.CreditMapper._
import ru.neoflex.deal.model.Credit
import zio.macros.accessible
import zio.{Task, ZIO, ZLayer}

import scala.language.implicitConversions

@accessible
trait CreditDao {
  def insert(credit: Credit): Task[Unit]

  def get(id: Integer): Task[Credit]
}

case class CreditDaoImpl(dsl: JooqDsl, creditStatusDao: CreditStatusDao) extends CreditDao {
  override def insert(credit: Credit): Task[Unit] =
    for {
      creditStatus  <- creditStatusDao.creditStatusId(credit.creditStatus)
      dsl           <- dsl.getJooqContext
    } yield dsl.insertInto(CREDIT)
      .columns(
        CREDIT.TERM,
        CREDIT.MONTHLY_PAYMENT,
        CREDIT.RATE,
        CREDIT.PSK,
        CREDIT.WITH_INSURANCE,
        CREDIT.SALARY_CLIENT,
        CREDIT.CREDIT_STATUS)
      .values(
        credit.term,
        credit.monthlyPayment,
        credit.rate,
        credit.psk.orNull,
        credit.withInsurance,
        credit.salaryClient,
        creditStatus
      )
      .execute

  override def get(id: Integer): Task[Credit] =
    toModel(for {
      dsl     <- dsl.getJooqContext
      record  <- ZIO.succeed(dsl.selectFrom(CREDIT)
                                .where(CREDIT.CREDIT_ID.eq(id))
                                .fetchAnyInto(CREDIT))
    } yield record)
}

object CreditDaoImpl {
  val live: ZLayer[CreditStatusDao with JooqDsl, Nothing, CreditDaoImpl] = ZLayer {
    for {
      dsl             <- ZIO.service[JooqDsl]
      creditStatusDao <- ZIO.service[CreditStatusDao]
    } yield CreditDaoImpl(dsl, creditStatusDao)
  }
}
