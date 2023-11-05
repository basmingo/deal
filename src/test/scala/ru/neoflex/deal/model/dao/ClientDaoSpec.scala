package ru.neoflex.deal.model.dao

import ru.neoflex.deal.configuration.{DslConnection, JooqDsl}
import ru.neoflex.deal.model.Client
import ru.neoflex.deal.model.jsonb.{Employment, Passport}
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertTrue}
import zio.{Scope, ZIO}

import java.time.LocalDate

object ClientDaoSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Client dao test")(
      test("return 0 when get on empty") {
        for {
          clientDao <- ZIO.service[ClientDao]
          dsl       <- ZIO.service[JooqDsl]
          _         <- clientDao.deleteAll(dsl)
          result    <- clientDao.getLastId(dsl)
        } yield assertTrue(result == 0)
      },
      test("test insert and get last") {
        for {
          clientDao <- ZIO.service[ClientDao]
          dsl       <- ZIO.service[JooqDsl]
          _ <-
            clientDao.insert(
              Client(
                None,
                "TestName",
                "TestLastName",
                Some("TestMiddleName"),
                LocalDate.now,
                "TestMail",
                "MALE",
                "SINGLE",
                Some(100_000),
                Some("TestAccount"),
                Seq(Passport(1234, 123456, "TestBranch", "TestDate")),
                Seq(Employment("TestStatus", "TestInn", 100_000, "TestPosition", 12, 12))
              ),
              dsl
            )
          result <- clientDao.getLastId(dsl)
          _      <- clientDao.deleteAll(dsl)
        } yield assertTrue(result != 0)
      },
      test("get client") {
        for {
          clientDao <- ZIO.service[ClientDao]
          dsl       <- ZIO.service[JooqDsl]
          _         <- clientDao.deleteAll(dsl)
          _ <-
            clientDao.insert(
              Client(
                None,
                "TestName",
                "TestLastName",
                Some("TestMiddleName"),
                LocalDate.now,
                "TestMail",
                "MALE",
                "SINGLE",
                Some(100_000),
                Some("TestAccount"),
                Seq(Passport(1234, 123456, "TestBranch", "TestDate")),
                Seq(Employment("TestStatus", "TestInn", 100_000, "TestPosition", 12, 12))
              ),
              dsl
            )
          last   <- clientDao.getLastId(dsl)
          result <- clientDao.get(last, dsl)
        } yield assertTrue(result.clientId.map(_.toInt).contains(last))
      }
    ).provide(
      DslConnection.live,
      ClientDaoImpl.live,
      EmploymentDaoImpl.live,
      PassportDaoImpl.live,
      MaritalStatusDaoImpl.live,
      GenderDaoImpl.live
    )
}
