package ru.neoflex.deal.model.dao

import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.configuration.deal.tables.Employment.EMPLOYMENT
import ru.neoflex.deal.model.jsonb.Employment
import zio.json.DecoderOps
import zio.macros.accessible
import zio.{Task, ZIO, ZLayer}

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

@accessible
trait EmploymentDao {
  def getByClient(id: Int): Task[Seq[Employment]]
}

case class EmploymentDaoImpl(dsl: JooqDsl) extends EmploymentDao {
  override def getByClient(id: Int): Task[Seq[Employment]] =
    for {
      dsl <- dsl.getJooqContext
      employmentData <- ZIO.succeed(
                          dsl
                            .select(EMPLOYMENT.EMPLOYMENT_DATA)
                            .from(EMPLOYMENT)
                            .where(EMPLOYMENT.CLIENT_ID.eq(id))
                            .fetch()
                            .into(classOf[String])
                        )
    } yield employmentData.toSeq
      .map(_.fromJson[Employment])
      .collect { case Right(value) => value }
}

object EmploymentDaoImpl {
  val live: ZLayer[JooqDsl, Nothing, EmploymentDaoImpl] = ZLayer {
    for {
      dsl <- ZIO.service[JooqDsl]
    } yield EmploymentDaoImpl(dsl)
  }
}
