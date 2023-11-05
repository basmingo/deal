import ru.neoflex.deal.configuration.{DslConnection, JooqDsl}
import ru.neoflex.deal.model._
import ru.neoflex.deal.model.dao.{
  ApplicationDaoImpl,
  ClientDao,
  ClientDaoImpl,
  CreditDao,
  CreditDaoImpl,
  CreditStatusDaoImpl,
  EmploymentDaoImpl,
  GenderDaoImpl,
  MaritalStatusDaoImpl,
  PassportDaoImpl
}
import ru.neoflex.deal.model.jsonb.{Employment, Passport}
import zio.ExitCode.success
import zio.json.DecoderOps
import zio.{Schedule, Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

import java.time.LocalDate
import scala.language.implicitConversions

object Main extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    for {
      dsl <- ZIO.service[JooqDsl]
      a   <- CreditDao.get(19000, dsl).repeatN(10000)
      _   <- ZIO.log(a.toString)
    } yield ()
  }.provide(
    CreditDaoImpl.live,
    CreditStatusDaoImpl.live,
    DslConnection.live,
  )
}
