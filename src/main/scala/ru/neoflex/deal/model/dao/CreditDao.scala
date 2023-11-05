package ru.neoflex.deal.model.dao

import org.jooq.JSONB
import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.configuration.deal.tables.Credit._
import ru.neoflex.deal.configuration.deal.tables.CreditStatus._
import ru.neoflex.deal.configuration.deal.tables.PaymentSchedule._
import ru.neoflex.deal.model.jsonb.PaymentScheduleElement
import ru.neoflex.deal.model.{Credit, CreditDbResponseDto, CreditMapper}
import zio.json.EncoderOps
import zio.macros.accessible
import zio.{Task, ZIO, ZLayer}

import scala.language.implicitConversions

@accessible
trait CreditDao {
  def get(id: Int, dsl: JooqDsl): Task[Credit]

  def insert(credit: Credit, dsl: JooqDsl): Task[Unit]

  def getLastId(dsl: JooqDsl): Task[Int]

  def deleteAll(dsl: JooqDsl): Task[Unit]
}

case class CreditDaoImpl(creditStatusDao: CreditStatusDao, paymentScheduleDao: PaymentScheduleDao) extends CreditDao {
  override def insert(credit: Credit, dsl: JooqDsl): Task[Unit] =
    for {
      ctx          <- dsl.getJooqContext
      creditStatus <- creditStatusDao.getCreditStatusId(credit.creditStatus)
      _ <- ZIO.succeed(
             ctx
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
      lastId <- getLastId(dsl)
      _      <- paymentScheduleDao.insert(lastId, credit.creditSchedule, dsl)
    } yield ()

  override def get(id: Int, dsl: JooqDsl): Task[Credit] =
    for {
      ctx <- dsl.getJooqContext
      creditData <- ZIO.succeed(
                      ctx
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
      paymentSchedule <- paymentScheduleDao.get(creditData.creditId, dsl)
      credit          <- CreditMapper.toCredit(creditData, paymentSchedule)
    } yield credit

  override def getLastId(dsl: JooqDsl): Task[Int] =
    for {
      ctx <- dsl.getJooqContext
      id <- ZIO
              .succeed(
                ctx
                  .select(CREDIT.CREDIT_ID)
                  .from(CREDIT)
                  .orderBy(CREDIT.CREDIT_ID.desc)
                  .limit(1)
                  .fetchOneInto(classOf[Integer])
                  .toInt
              )
    } yield id

  override def deleteAll(dsl: JooqDsl): Task[Unit] =
    for {
      ctx <- dsl.getJooqContext
      _   <- ZIO.succeed(ctx.delete(CREDIT).execute())
    } yield ()
}

object CreditDaoImpl {
  val live: ZLayer[PaymentScheduleDao with CreditStatusDao, Nothing, CreditDaoImpl] = ZLayer {
    for {
      creditStatusDao <- ZIO.service[CreditStatusDao]
      paymentSchedule <- ZIO.service[PaymentScheduleDao]
    } yield CreditDaoImpl(creditStatusDao, paymentSchedule)
  }
}
