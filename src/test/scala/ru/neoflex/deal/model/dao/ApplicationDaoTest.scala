package ru.neoflex.deal.model.dao

import com.dimafeng.testcontainers.{ContainerDef, PostgreSQLContainer}
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import io.github.scottweaver.zio.testcontainers.postgres.ZPostgreSQLContainer
import io.github.scottweaver.zio.testcontainers.postgres.ZPostgreSQLContainer.Settings
import ru.neoflex.deal.configuration.{DslConnection, JooqDsl}
import zio.{Scope, ZIO, ZLayer}
import zio.test.{Spec, TestEnvironment, ZIOSpecAbstract, ZIOSpecDefault, assertTrue, suite, test}

object ApplicationDaoTest extends ZIOSpecDefault with TestContainerForAll {
  override val containerDef: ContainerDef = PostgreSQLContainer.Def(
    databaseName = "conveyor_deal",
    username = "admin",
    password = "password",
    urlParams = Map[String, String]("localhost" -> "5431")
  )

  override def spec: Spec[TestEnvironment with Scope, Any] = {

    suite("AAA")(
      test("first test") {
        for {
          creditDao <- ZIO.service[CreditDao]
          log <- ZIO.log(ZPostgreSQLContainer.toString)
          dsl <- ZIO.service[JooqDsl]
          //          ctx <- dsl.getJooqContext
          //          result <- creditDao.getLastId(dsl)
        } yield assertTrue(true)
      }
    )
  }.provide(
    DslConnection.live,
    CreditDaoImpl.live,
    CreditStatusDaoImpl.live,
    ZLayer.succeed(
      Settings(
        imageVersion = "latest",
        databaseName = "conveyor_deal",
        username = "admin",
        password = "password"
      )
    ),
    ZPostgreSQLContainer.live
  )
}
