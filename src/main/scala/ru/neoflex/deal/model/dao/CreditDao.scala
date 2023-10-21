package ru.neoflex.deal.model.dao

import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.configuration.deal.tables.Credit._
import ru.neoflex.deal.configuration.deal.tables.CreditStatus._
import ru.neoflex.deal.model.{Credit, CreditDbResponseDto, CreditMapper}
import zio.macros.accessible
import zio.{Task, ZIO, ZLayer}

import scala.language.implicitConversions

@accessible
trait CreditDao {
  def get(id: Integer, dsl: JooqDsl): Task[Credit]

  //  def insert(credit: Credit, dsl: JooqDsl): Task[Unit]
}

case class CreditDaoImpl(creditStatusDao: CreditStatusDao) extends CreditDao {
  //  override def insert(credit: Credit, dsl: JooqDsl): Task[Unit] = ???
  //    for {
  //      creditStatus <- creditStatusDao.creditStatusId(credit.creditStatus)
  //      dsl <- dsl.getJooqContext
  //    } yield dsl.insertInto(CREDIT)
  //      .columns(
  //        CREDIT.TERM,
  //        CREDIT.MONTHLY_PAYMENT,
  //        CREDIT.RATE,
  //        CREDIT.PSK,
  //        CREDIT.WITH_INSURANCE,
  //        CREDIT.SALARY_CLIENT,
  //        CREDIT.CREDIT_STATUS)
  //      .values(
  //        credit.term,
  //        credit.monthlyPayment,
  //        credit.rate,
  //        credit.psk.orNull,
  //        credit.withInsurance,
  //        credit.salaryClient,
  //        creditStatus
  //      )
  //      .execute

  override def get(id: Integer, dsl: JooqDsl): Task[Credit] =
    for {
      dsl <- dsl.getJooqContext
      creditData <- ZIO.succeed(dsl.select(
        CREDIT.CREDIT_ID,
        CREDIT.TERM,
        CREDIT.MONTHLY_PAYMENT,
        CREDIT.RATE,
        CREDIT.PSK,
        CREDIT.WITH_INSURANCE,
        CREDIT.SALARY_CLIENT,
        CREDIT_STATUS.CREDIT_STATUS_TYPE
      ).from(CREDIT)
        .join(CREDIT_STATUS)
        .on(CREDIT_STATUS.CREDIT_STATUS_ID.eq(CREDIT.CREDIT_STATUS))
        .where(CREDIT.CREDIT_ID.eq(id))
        .fetchAnyInto(classOf[CreditDbResponseDto]))
      credit <- CreditMapper.toCredit(creditData)
    } yield credit
}

object CreditDaoImpl {
  val live: ZLayer[CreditStatusDao, Nothing, CreditDaoImpl] = ZLayer {
    for {
      creditStatusDao <- ZIO.service[CreditStatusDao]
    } yield CreditDaoImpl(creditStatusDao)
  }
}
