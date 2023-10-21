package ru.neoflex.deal.model.dao

import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.configuration.deal.tables.GenderType._
import zio.{Task, ZIO, ZLayer}

trait GenderDao {

  def genderId(gender: String): Task[Integer]
}

case class GenderDaoImpl(dsl: JooqDsl) extends GenderDao {
  override def genderId(gender: String): Task[Integer] =
    dsl.getJooqContext
      .map(_.select(GENDER_TYPE.GENDER_TYPE_ID)
            .from(GENDER_TYPE)
            .where(GENDER_TYPE.GENDER.eq(gender))
            .fetchAnyInto(classOf[Integer]))
}

object GenderDaoImpl {
  val live: ZLayer[JooqDsl, Nothing, GenderDaoImpl] = ZLayer {
    for {
      dsl <- ZIO.service[JooqDsl]
    } yield GenderDaoImpl(dsl)
  }
}
