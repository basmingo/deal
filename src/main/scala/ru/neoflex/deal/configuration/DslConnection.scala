package ru.neoflex.deal.configuration

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.jooq.{DSLContext, SQLDialect}
import org.jooq.impl.DSL
import ru.neoflex.deal.configuration.DslConnection.{ds, hikariConfig}
import zio.{Task, ULayer, ZIO, ZLayer}

import javax.sql.DataSource

trait JooqDsl {
  def getJooqContext: Task[DSLContext]
}

case class DslConnection() extends JooqDsl {

  def getJooqContext: Task[DSLContext] =
    ZIO.succeed(DSL.using(ds, SQLDialect.POSTGRES))
}

object DslConnection {
  val live: ULayer[DslConnection] = ZLayer.succeed(DslConnection())

  val hikariConfig: HikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5431/conveyor_deal")
  hikariConfig.setUsername("admin")
  hikariConfig.setPassword("password")
  hikariConfig.setMaximumPoolSize(100)

  val ds = new HikariDataSource(hikariConfig)
}
