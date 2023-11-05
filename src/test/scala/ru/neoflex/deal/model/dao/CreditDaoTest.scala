package ru.neoflex.deal.model.dao

import ru.neoflex.deal.configuration.{DslConnection, JooqDsl}
import ru.neoflex.deal.model.Credit
import ru.neoflex.deal.model.jsonb.PaymentScheduleElement
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertTrue}
import zio.{Scope, ZIO}

import java.time.LocalDate
import scala.collection.immutable.Seq

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
          schedules <- ZIO.succeed(
                         Seq(
                           PaymentScheduleElement(10, LocalDate.now, 10, 10, 10, 10),
                           PaymentScheduleElement(20, LocalDate.now, 20, 20, 20, 20),
                           PaymentScheduleElement(30, LocalDate.now, 30, 30, 30, 30)
                         )
                       )
          _ <- creditDao.insert(
                 Credit(None, 1, 0.5, 0.5, None, withInsurance = true, salaryClient = true, "ISSUED", schedules),
                 dsl
               )
          _ <- creditDao.insert(
                 Credit(
                   None,
                   100,
                   0.1,
                   0.1,
                   Some(0.32),
                   withInsurance = true,
                   salaryClient = true,
                   "CALCULATED",
                   schedules
                 ),
                 dsl
               )
          _ <-
            creditDao.insert(
              Credit(Some(13), 100, 0.1, 0.1, None, withInsurance = true, salaryClient = true, "CALCULATED", schedules),
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
          _ <- creditDao.deleteAll(dsl)
          schedules <- ZIO.succeed(
                         Seq(
                           PaymentScheduleElement(10, LocalDate.now, 10, 10, 10, 10),
                           PaymentScheduleElement(20, LocalDate.now, 20, 20, 20, 20),
                           PaymentScheduleElement(30, LocalDate.now, 30, 30, 30, 30)
                         )
                       )
          _ <- creditDao.insert(
                 Credit(None, 1, 0.5, 0.5, None, withInsurance = true, salaryClient = true, "ISSUED", schedules),
                 dsl
               )
          last   <- creditDao.getLastId(dsl)
          result <- creditDao.get(last, dsl)
        } yield assertTrue(result.creditId.map(_.toInt).contains(last))
      }
    )
  }.provide(
    DslConnection.live,
    CreditDaoImpl.live,
    CreditStatusDaoImpl.live,
    PaymentScheduleDaoImpl.live
  )
}
