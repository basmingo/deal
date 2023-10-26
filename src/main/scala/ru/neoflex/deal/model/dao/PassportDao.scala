package ru.neoflex.deal.model.dao

import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.configuration.deal.tables.Passport.PASSPORT
import ru.neoflex.deal.model.jsonb.Passport
import zio.json.DecoderOps
import zio.macros.accessible
import zio.{Task, ZIO, ZLayer}

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

@accessible
trait PassportDao {
  def getByClient(id: Int): Task[Seq[Passport]]
}

case class PassportDaoImpl(dsl: JooqDsl) extends PassportDao {
  override def getByClient(id: Int): Task[Seq[Passport]] =
    for {
      dsl <- dsl.getJooqContext
      passportData <- ZIO.succeed(
                        dsl
                          .select(PASSPORT.PASSPORT_DATA)
                          .from(PASSPORT)
                          .where(PASSPORT.CLIENT_ID.eq(id))
                          .fetch()
                          .into(classOf[String])
                      )
    } yield passportData.toSeq
      .map(_.fromJson[Passport])
      .collect { case Right(value) => value }
}

object PassportDaoImpl {
  val live: ZLayer[JooqDsl, Nothing, PassportDaoImpl] = ZLayer {
    for {
      dsl <- ZIO.service[JooqDsl]
    } yield PassportDaoImpl(dsl)
  }
}
