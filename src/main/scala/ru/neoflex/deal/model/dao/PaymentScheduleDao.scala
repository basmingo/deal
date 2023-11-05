package ru.neoflex.deal.model.dao

import org.jooq.JSONB
import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.model.jsonb.PaymentScheduleElement
import ru.neoflex.deal.configuration.deal.tables.PaymentSchedule._
import zio.json.{DecoderOps, EncoderOps}
import zio.{Task, ULayer, ZIO, ZLayer}
import zio.macros.accessible

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

@accessible
trait PaymentScheduleDao {
  def get(id: Int, dsl: JooqDsl): Task[Seq[PaymentScheduleElement]]

  def insert(id: Int, schedules: Seq[PaymentScheduleElement], dsl: JooqDsl): Task[Unit]
}

case class PaymentScheduleDaoImpl() extends PaymentScheduleDao {
  override def get(id: Int, dsl: JooqDsl): Task[Seq[PaymentScheduleElement]] =
    for {
      ctx <- dsl.getJooqContext
      paymentSchedule <- ZIO.succeed(
                           ctx
                             .select(PAYMENT_SCHEDULE.SCHEDULE_DATA)
                             .from(PAYMENT_SCHEDULE)
                             .where(PAYMENT_SCHEDULE.CREDIT_ID.eq(id))
                             .fetch
                             .into(classOf[String])
                         )
    } yield paymentSchedule.toSeq
      .map(_.fromJson[PaymentScheduleElement])
      .collect { case Right(value) => value }

  override def insert(id: Int, schedules: Seq[PaymentScheduleElement], dsl: JooqDsl): Task[Unit] =
    for {
      ctx <- dsl.getJooqContext
      batchInserts <- ZIO.succeed(
                        ctx.batch(
                          ctx
                            .insertInto(PAYMENT_SCHEDULE, PAYMENT_SCHEDULE.CREDIT_ID, PAYMENT_SCHEDULE.SCHEDULE_DATA)
                        )
                      )
      _ <- ZIO.succeed(
             schedules
               .map(element => JSONB.jsonb(element.toJson))
               .foreach(paymentSchedule => batchInserts.bind(id, paymentSchedule))
           )
      _ <- ZIO.succeed(batchInserts.execute())
    } yield ()
}

object PaymentScheduleDaoImpl {
  val live: ULayer[PaymentScheduleDaoImpl] = ZLayer.succeed(PaymentScheduleDaoImpl())
}
