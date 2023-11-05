package ru.neoflex.deal.model.dao

import org.jooq.JSONB
import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.configuration.deal.tables.Application._
import ru.neoflex.deal.model.Application
import ru.neoflex.deal.model.dao.ApplicationDaoImpl.toJsonb
import ru.neoflex.deal.model.jsonb.ApplicationOffer
import zio.json.EncoderOps
import zio.macros.accessible
import zio.{Task, ZIO, ZLayer}

import scala.language.implicitConversions

@accessible
trait ApplicationDao {
  def getLastId(dsl: JooqDsl): Task[Int]

  def deleteAll(dsl: JooqDsl): Task[Unit]

  def insert(application: Application, dsl: JooqDsl): Task[Unit]

  def get(id: Int, dsl: JooqDsl): Task[Application]

  def setApplicationOffer(applicationOffer: ApplicationOffer, dsl: JooqDsl): Task[Unit]
}

case class ApplicationDaoImpl(clientDao: ClientDao, creditDao: CreditDao) extends ApplicationDao {
  override def insert(application: Application, dsl: JooqDsl): Task[Unit] =
    for {
      ctx      <- dsl.getJooqContext
      _        <- clientDao.insert(application.client, dsl)
      clientId <- clientDao.getLastId(dsl)
      _        <- creditDao.insert(application.credit, dsl)
      creditId <-
        creditDao.getLastId(dsl).orDieWith(_ => new RuntimeException("unexpected, possible insert was failed?"))
      _ <- ZIO.succeed(
             ctx
               .insertInto(
                 APPLICATION,
                 APPLICATION.CLIENT_ID,
                 APPLICATION.CREDIT_ID,
                 APPLICATION.CREATED,
                 APPLICATION.APPLIED_OFFER,
                 APPLICATION.SIGNED,
                 APPLICATION.SES_CODE
               )
               .values(
                 creditId,
                 clientId,
                 application.created,
                 toJsonb(application.appliedOffer.orNull),
                 application.signed.orNull,
                 application.sesCode.orNull
               )
               .execute
           )
    } yield ()

  override def get(id: Int, dsl: JooqDsl): Task[Application] = ???
//    (for {
//      dsl <- dsl.getJooqContext
//      record <- ZIO.succeed(dsl.selectFrom(APPLICATION)
//        .where(APPLICATION.APPLICATION_ID.eq(id))
//        .fetchAnyInto(APPLICATION))
//    } yield record)

  override def setApplicationOffer(applicationOffer: ApplicationOffer, dsl: JooqDsl): Task[Unit] =
    dsl.getJooqContext
      .map(
        _.update(APPLICATION)
          .set(APPLICATION.APPLIED_OFFER, toJsonb(applicationOffer))
          .execute
      )

  override def deleteAll(dsl: JooqDsl): Task[Unit] = ???

  override def getLastId(dsl: JooqDsl): Task[Int] = ???
}

object ApplicationDaoImpl {
  val live: ZLayer[CreditDao with ClientDao, Nothing, ApplicationDao] = ZLayer {
    for {
      clientDao <- ZIO.service[ClientDao]
      creditDao <- ZIO.service[CreditDao]
    } yield ApplicationDaoImpl(clientDao, creditDao)
  }

  def toJsonb(applicationOffer: ApplicationOffer): JSONB =
    JSONB.jsonb(applicationOffer.toJson)
}
