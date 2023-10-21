package ru.neoflex.deal.model.dao

import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.configuration.deal.tables.MaritalStatusType._
import zio.{Task, ZIO, ZLayer}

trait MaritalStatusDao {

  def getMaritalStatusId(maritalStatus: String): Task[Integer]
}

case class MaritalStatusDaoImpl(dsl: JooqDsl) extends MaritalStatusDao {
  override def getMaritalStatusId(maritalStatus: String): Task[Integer] =
    dsl.getJooqContext
      .map(_.select(MARITAL_STATUS_TYPE.MARITAL_STATUS_ID)
            .from(MARITAL_STATUS_TYPE)
            .where(MARITAL_STATUS_TYPE.STATUS.eq(maritalStatus))
            .fetchAnyInto(classOf[Integer]))
}

object MaritalStatusDaoImpl {
  val live: ZLayer[JooqDsl, Nothing, MaritalStatusDaoImpl] = ZLayer {
    for {
      dsl <- ZIO.service[JooqDsl]
    } yield MaritalStatusDaoImpl(dsl)
  }
}