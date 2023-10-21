package ru.neoflex.deal.configuration

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.jooq.{DSLContext, SQLDialect}
import org.jooq.impl.DSL
import zio.{Task, ULayer, ZIO, ZLayer}

import javax.sql.DataSource

trait JooqDsl {
  def getJooqContext: Task[DSLContext]
}

case class DslConnection() extends JooqDsl {
  val hikariConfig: HikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5431/conveyor_deal")
  hikariConfig.setUsername("admin")
  hikariConfig.setPassword("password")
  hikariConfig.setMaximumPoolSize(100)


  def getJooqContext: Task[DSLContext] =
    ZIO.succeed(new HikariDataSource(hikariConfig))
      .map(ds => DSL.using(ds, SQLDialect.POSTGRES))
}

object DslConnection {
  val live: ULayer[DslConnection] = ZLayer.succeed(DslConnection())
}
