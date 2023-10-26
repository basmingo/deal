package ru.neoflex.deal.model.dao

import ru.neoflex.deal.configuration.{DslConnection, JooqDsl}
import ru.neoflex.deal.model.Credit
import zio.{Scope, ZIO}
import zio.test.{Spec, TestEnvironment, ZIOSpecAbstract, ZIOSpecDefault, assertTrue, suite, test}

object CreditDaoTest extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = {
    suite("Credit DAO test")(
      test("return 0 when get on empty") {
        for {
          creditDao <- ZIO.service[CreditDao]
          dsl       <- ZIO.service[JooqDsl]
          _         <- creditDao.deleteAll(dsl)
          result    <- creditDao.getLastId(dsl)
        } yield assertTrue(result == 0)
      },
      test("test insert and get last") {
        for {
          creditDao <- ZIO.service[CreditDao]
          dsl       <- ZIO.service[JooqDsl]
          _ <-
            creditDao.insert(Credit(None, 1, 0.5, 0.5, None, withInsurance = true, salaryClient = true, "ISSUED"), dsl)
          _ <-
            creditDao.insert(
              Credit(None, 100, 0.1, 0.1, Some(0.321), withInsurance = true, salaryClient = true, "CALCULATED"),
              dsl
            )
          _ <-
            creditDao.insert(
              Credit(Some(13), 100, 0.1, 0.1, None, withInsurance = true, salaryClient = true, "CALCULATED"),
              dsl
            )
          result <- creditDao.getLastId(dsl)
          _      <- creditDao.deleteAll(dsl)
        } yield assertTrue(result != 0)
      },
      test("get credit") {
        for {
          creditDao <- ZIO.service[CreditDao]
          dsl       <- ZIO.service[JooqDsl]
          _         <- creditDao.deleteAll(dsl)
          _ <-
            creditDao.insert(Credit(None, 1, 0.5, 0.5, None, withInsurance = true, salaryClient = true, "ISSUED"), dsl)
          last   <- creditDao.getLastId(dsl)
          result <- creditDao.get(last, dsl)
        } yield assertTrue(result.creditId.map(_.toInt).contains(last))
      }
    )
  }.provide(
    DslConnection.live,
    CreditDaoImpl.live,
    CreditStatusDaoImpl.live
  )
}
