package ru.neoflex.deal.model.dao

import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.model.Application
import zio.{Scope, ZIO}
import zio.test.{Spec, TestEnvironment, ZIOSpecAbstract, ZIOSpecDefault, assertTrue}

object ApplicationDaoSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = ???
//    suite("Application Dao Test")(
//      test("return 0 when get on empty") {
//        for {
//          applicationDao <- ZIO.service[ApplicationDao]
//          dsl            <- ZIO.service[JooqDsl]
//          _              <- applicationDao.deleteAll(dsl)
//          result         <- applicationDao.getLastId(dsl)
//        } yield assertTrue(result == 0)
//      },
//      test("test insert and get last") {
//        for {
//          applicationDao <- ZIO.service[ApplicationDao]
//          dsl            <- ZIO.service[JooqDsl]
//          _ <-
//            applicationDao.insert(Application(None, ), dsl)
//          _ <-
//            applicationDao.insert(
//              Application(),
//              dsl
//            )
//          _ <-
//            applicationDao.insert(
//              Application(),
//              dsl
//            )
//          result <- applicationDao.getLastId(dsl)
//          _      <- applicationDao.deleteAll(dsl)
//        } yield assertTrue(result != 0)
//      },
//      test("get application") {
//        for {
//          applicationDao <- ZIO.service[ApplicationDao]
//          dsl            <- ZIO.service[JooqDsl]
//          _              <- applicationDao.deleteAll(dsl)
//          _ <-
//            applicationDao.insert(Application(), dsl)
//          last   <- applicationDao.getLastId(dsl)
//          result <- applicationDao.get(last, dsl)
//        } yield assertTrue(result.applicationId.map(_.toInt).contains(last))
//      }
//    )
}
