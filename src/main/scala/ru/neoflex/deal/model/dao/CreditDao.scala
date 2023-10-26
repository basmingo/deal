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

  def insert(credit: Credit, dsl: JooqDsl): Task[Unit]

  def getLastId(dsl: JooqDsl): Task[Integer]
}

case class CreditDaoImpl(creditStatusDao: CreditStatusDao) extends CreditDao {
  override def insert(credit: Credit, dsl: JooqDsl): Task[Unit] =
    for {
      dsl          <- dsl.getJooqContext
      creditStatus <- creditStatusDao.getCreditStatusId(credit.creditStatus)
      _ <- ZIO.succeed(
             dsl
               .insertInto(
                 CREDIT,
                 CREDIT.TERM,
                 CREDIT.MONTHLY_PAYMENT,
                 CREDIT.RATE,
                 CREDIT.PSK,
                 CREDIT.WITH_INSURANCE,
                 CREDIT.SALARY_CLIENT,
                 CREDIT.CREDIT_STATUS
               )
               .values(
                 credit.term,
                 new java.math.BigDecimal(credit.monthlyPayment.toString),
                 new java.math.BigDecimal(credit.rate.toString),
                 credit.psk
                   .map(it => new java.math.BigDecimal(it.toString))
                   .orNull,
                 credit.withInsurance,
                 credit.salaryClient,
                 creditStatus
               )
               .execute
           )
    } yield ()

  override def get(id: Integer, dsl: JooqDsl): Task[Credit] =
    for {
      dsl <- dsl.getJooqContext
      creditData <- ZIO.succeed(
                      dsl
                        .select(
                          CREDIT.CREDIT_ID,
                          CREDIT.TERM,
                          CREDIT.MONTHLY_PAYMENT,
                          CREDIT.RATE,
                          CREDIT.PSK,
                          CREDIT.WITH_INSURANCE,
                          CREDIT.SALARY_CLIENT,
                          CREDIT_STATUS.CREDIT_STATUS_TYPE
                        )
                        .from(CREDIT)
                        .join(CREDIT_STATUS)
                        .on(CREDIT_STATUS.CREDIT_STATUS_ID.eq(CREDIT.CREDIT_STATUS))
                        .where(CREDIT.CREDIT_ID.eq(id))
                        .fetchAnyInto(classOf[CreditDbResponseDto])
                    )
      credit <- CreditMapper.toCredit(creditData)
    } yield credit

  override def getLastId(dsl: JooqDsl): Task[Integer] =
    for {
      ctx <- dsl.getJooqContext
      id <- ZIO.succeed(
              ctx
                .select(CREDIT.CREDIT_ID)
                .from(CREDIT)
                .orderBy(CREDIT.CREDIT_ID.desc)
                .limit(1)
                .fetchOneInto(classOf[Integer])
            )
    } yield id
}

object CreditDaoImpl {
  val live: ZLayer[CreditStatusDao, Nothing, CreditDaoImpl] = ZLayer {
    for {
      creditStatusDao <- ZIO.service[CreditStatusDao]
    } yield CreditDaoImpl(creditStatusDao)
  }
}
