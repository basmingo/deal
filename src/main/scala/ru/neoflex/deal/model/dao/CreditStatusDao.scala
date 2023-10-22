package ru.neoflex.deal.model.dao

import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.configuration.deal.tables.CreditStatus._
import zio.macros.accessible
import zio.{Task, ZIO, ZLayer}

@accessible
trait CreditStatusDao {
  def getCreditStatusId(creditStatus: String): Task[Integer]
}

case class CreditStatusDaoImpl(dsl: JooqDsl) extends CreditStatusDao {
  override def getCreditStatusId(creditStatus: String): Task[Integer] =
    dsl.getJooqContext
      .map(_.select(CREDIT_STATUS.CREDIT_STATUS_ID)
            .from(CREDIT_STATUS)
            .where(CREDIT_STATUS.CREDIT_STATUS_TYPE.eq(creditStatus))
            .execute)
}

object CreditStatusDaoImpl {
  val live: ZLayer[JooqDsl, Nothing, CreditStatusDaoImpl] = ZLayer {
    for {
      dsl <- ZIO.service[JooqDsl]
    } yield CreditStatusDaoImpl(dsl)
  }
}
