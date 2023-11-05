package ru.neoflex.deal.model.dao

import org.jooq.JSONB
import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.model.jsonb.PaymentScheduleElement
import ru.neoflex.deal.configuration.deal.tables.PaymentSchedule._
import zio.json.{DecoderOps, EncoderOps}
import zio.{Task, ULayer, ZIO, ZLayer}
import zio.macros.accessible

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import scala.jdk.CollectionConverters.SeqHasAsJava

@accessible
trait PaymentScheduleDao {
  def getByCreditId(id: Int, dsl: JooqDsl): Task[Seq[PaymentScheduleElement]]

  def insert(id: Int, schedules: Seq[PaymentScheduleElement], dsl: JooqDsl): Task[Unit]
}

case class PaymentScheduleDaoImpl() extends PaymentScheduleDao {
  override def getByCreditId(creditId: Int, dsl: JooqDsl): Task[Seq[PaymentScheduleElement]] =
    for {
      ctx <- dsl.getJooqContext
      paymentSchedule <- ZIO.succeed(
                           ctx
                             .select(PAYMENT_SCHEDULE.SCHEDULE_DATA)
                             .from(PAYMENT_SCHEDULE)
                             .where(PAYMENT_SCHEDULE.CREDIT_ID.eq(creditId))
                             .fetch
                             .into(classOf[String])
                         )
    } yield paymentSchedule.toSeq
      .map(_.fromJson[PaymentScheduleElement])
      .collect { case Right(value) => value }

  override def insert(id: Int, schedules: Seq[PaymentScheduleElement], dsl: JooqDsl): Task[Unit] =
    for {
      ctx <- dsl.getJooqContext
      paymentSchedules <- ZIO.succeed(
                            schedules
                              .map(elem => JSONB.jsonb(elem.toJson))
                              .map(elem =>
                                ctx
                                  .newRecord(PAYMENT_SCHEDULE.CREDIT_ID, PAYMENT_SCHEDULE.SCHEDULE_DATA)
                                  .values(id, elem)
                              )
                          )
      _ <- ZIO.succeed(
             ctx
               .insertInto(PAYMENT_SCHEDULE, PAYMENT_SCHEDULE.CREDIT_ID, PAYMENT_SCHEDULE.SCHEDULE_DATA)
               .valuesOfRecords(paymentSchedules.asJava)
               .execute()
           )
    } yield ()
}

object PaymentScheduleDaoImpl {
  val live: ULayer[PaymentScheduleDaoImpl] = ZLayer.succeed(PaymentScheduleDaoImpl())
}
