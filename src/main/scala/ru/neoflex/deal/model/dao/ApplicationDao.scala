package ru.neoflex.deal.model.dao

import org.jooq.JSONB
import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.configuration.deal.tables.Application._
import ru.neoflex.deal.model.dao.ApplicationDaoImpl.toJsonb
import ru.neoflex.deal.model.Application
import ru.neoflex.deal.model.jsonb.ApplicationOffer
import ru.neoflex.deal.model.mappers.ApplicationMapper
import zio.json.EncoderOps
import zio.macros.accessible
import zio.{Task, ZIO, ZLayer}

import scala.language.implicitConversions

@accessible
trait ApplicationDao {

  def insert(application: Application): Task[Unit]

  def get(id: Int): Task[Application]

  def setApplicationOffer(applicationOffer: ApplicationOffer): Task[Unit]

}

case class ApplicationDaoImpl(dsl: JooqDsl) extends ApplicationDao {
  override def insert(application: Application): Task[Unit] = ???
//    dsl.getJooqContext
//      .map(_.insertInto(APPLICATION)
//            .columns(
//              APPLICATION.APPLICATION_ID,
//              APPLICATION.CLIENT_ID,
//              APPLICATION.CREDIT_ID,
//              APPLICATION.CREATED,
//              APPLICATION.APPLIED_OFFER,
//              APPLICATION.SIGNED,
//              APPLICATION.SES_CODE)
//            .values(
//              application.applicationId.orNull,
//              application.clientId,
//              application.creditId,
//              application.created,
//              application.appliedOffer
//                .map(toJsonb)
//                .orNull,
//              application.signed.orNull,
//              application.sesCode.orNull)
//            .execute
//      )

  override def get(id: Int): Task[Application] =
    ApplicationMapper.toModel(for {
      dsl    <- dsl.getJooqContext
      record <- ZIO.succeed(dsl.selectFrom(APPLICATION)
                                .where(APPLICATION.APPLICATION_ID.eq(id))
                                .fetchAnyInto(APPLICATION))
    } yield record)

  override def setApplicationOffer(applicationOffer: ApplicationOffer): Task[Unit] =
    dsl.getJooqContext
      .map(_.update(APPLICATION)
            .set(APPLICATION.APPLIED_OFFER, toJsonb(applicationOffer))
            .execute)
}

object ApplicationDaoImpl {
  val live: ZLayer[JooqDsl, Nothing, ApplicationDao] = ZLayer {
    for {
      dsl <- ZIO.service[JooqDsl]
    } yield ApplicationDaoImpl(dsl)
  }

  def toJsonb(applicationOffer: ApplicationOffer): JSONB =
    JSONB.jsonb(applicationOffer.toJson)
}
