package ru.neoflex.deal.model.dao

import org.jooq.JSONB
import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.configuration.deal.tables.Client._
import ru.neoflex.deal.configuration.deal.tables.Passport._
import ru.neoflex.deal.configuration.deal.tables.GenderType._
import ru.neoflex.deal.configuration.deal.tables.MaritalStatusType._
import ru.neoflex.deal.configuration.deal.tables.Employment._
import ru.neoflex.deal.model.{Client, ClientData}
import zio.json.EncoderOps
import zio.macros.accessible
import zio.{Task, ZIO, ZLayer}

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

@accessible
trait ClientDao {

  def insert(client: Client, dsl: JooqDsl): Task[Unit]

  def get(id: Int): Task[Client]
}

case class ClientDaoImpl(
                         genderDao: GenderDao,
                         passportDao: PassportDao,
                         employmentDao: EmploymentDao,
                         maritalStatusDao: MaritalStatusDao) extends ClientDao {
  override def insert(client: Client, dsl: JooqDsl): Task[Unit] =
    for {
      dsl <- dsl.getJooqContext
      _ <- ZIO.succeed(dsl.insertInto(CLIENT)
        .columns(CLIENT.FIRST_NAME,
          CLIENT.LAST_NAME,
          CLIENT.MIDDLE_NAME,
          CLIENT.BIRTH_DATE,
          CLIENT.EMAIL,
          CLIENT.GENDER_ID,
          CLIENT.MARITAL_STATUS,
          CLIENT.DEPENDENT_AMOUNT,
          CLIENT.ACCOUNT)
        .values(client.firstName,
          client.lastName,
          client.middleName.orNull,
          client.birthdate,
          client.email,
          dsl.select(GENDER_TYPE.GENDER_TYPE_ID)
            .from(GENDER_TYPE)
            .where(GENDER_TYPE.GENDER.eq(client.gender))
            .fetchOneInto(classOf[Integer]),
          dsl.select(MARITAL_STATUS_TYPE.MARITAL_STATUS_ID)
            .from(MARITAL_STATUS_TYPE)
            .where(MARITAL_STATUS_TYPE.STATUS.eq(client.maritalStatus))
            .fetchOneInto(classOf[Integer]),
          client.dependentAmount.orNull,
          client.account.orNull)
        .execute())

      id <- ZIO.succeed(dsl.select(CLIENT.CLIENT_ID)
        .from(CLIENT)
        .orderBy(CLIENT.CLIENT_ID.desc)
        .limit(1)
        .fetchOneInto(classOf[Integer]))

      passportData <- ZIO.succeed(client.passport.map(passport => JSONB.jsonb(passport.toJson)))
      employmentData <- ZIO.succeed(client.employment.map(employment => JSONB.jsonb(employment.toJson)))

      _ <- ZIO.succeed(passportData
        .map(it => dsl.insertInto(PASSPORT, PASSPORT.PASSPORT_DATA, PASSPORT.CLIENT_ID)
        .values(it, id)
        .execute))
        .fork

      _ <- ZIO.succeed(employmentData
        .map(it => dsl.insertInto(EMPLOYMENT, EMPLOYMENT.EMPLOYMENT_DATA, EMPLOYMENT.CLIENT_ID)
        .values(it, id)
        .execute))
        .fork
    } yield ()

  override def get(id: Int): Task[Client] =
    for {
      dsl <- dsl.getJooqContext
      clientData <- ZIO.succeed(dsl.select(CLIENT.CLIENT_ID,
        CLIENT.FIRST_NAME,
        CLIENT.LAST_NAME,
        CLIENT.MIDDLE_NAME,
        CLIENT.BIRTH_DATE,
        CLIENT.EMAIL, GENDER_TYPE.GENDER,
        MARITAL_STATUS_TYPE.STATUS,
        CLIENT.DEPENDENT_AMOUNT,
        CLIENT.ACCOUNT)
        .from(CLIENT
          .join(GENDER_TYPE)
          .on(GENDER_TYPE.GENDER_TYPE_ID.eq(CLIENT.GENDER_ID))
          .join(MARITAL_STATUS_TYPE)
          .on(MARITAL_STATUS_TYPE.MARITAL_STATUS_ID.eq(CLIENT.MARITAL_STATUS)))
        .where(CLIENT.CLIENT_ID.eq(id))
        .limit(1)
        .fetchOneInto(classOf[ClientData]))
      passports <- passportDao.getByClient(id)
      employments <- employmentDao.getByClient(id)
    } yield Client(clientData, passports, employments)
}

object ClientDaoImpl {
  private type ClientEnvironment = EmploymentDao
    with PassportDao
    with MaritalStatusDao
    with GenderDao
    with JooqDsl

  val live: ZLayer[ClientEnvironment, Nothing, ClientDaoImpl] = ZLayer {
    for {
//      dsl <- ZIO.service[JooqDsl]
      genderDao <- ZIO.service[GenderDao]
      maritalStatusDao <- ZIO.service[MaritalStatusDao]
      passportDao <- ZIO.service[PassportDao]
      employmentDao <- ZIO.service[EmploymentDao]
    } yield ClientDaoImpl(genderDao, passportDao, employmentDao, maritalStatusDao)
  }
}
