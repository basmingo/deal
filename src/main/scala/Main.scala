import ru.neoflex.deal.configuration.DslConnection
import ru.neoflex.deal.model._
import ru.neoflex.deal.model.dao.{ApplicationDaoImpl, ClientDao, ClientDaoImpl, CreditDao, CreditDaoImpl, CreditStatusDaoImpl, EmploymentDaoImpl, GenderDaoImpl, MaritalStatusDaoImpl, PassportDaoImpl}
import ru.neoflex.deal.model.jsonb.{Employment, Passport}
import zio.ExitCode.success
import zio.json.DecoderOps
import zio.{Schedule, Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

import java.time.LocalDate
import scala.language.implicitConversions

object Main extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    val insertMe = Client(ClientData(null, "Zilko", "Zobrich", null, LocalDate.now, "zilko@mail", "MALE", "SINGLE", 1000, "?"), Seq(Passport(1234, 123456, "no", "noo")), Seq(Employment("?", "?", BigDecimal(100), "OWNER", 112, 110)))
    for {
      //      res   <- ZIO succeed ("""{"number": 123456, "series": 1234, "issueDate": "25-05-1996", "issueBranch": "MVD"}""".fromJson[Passport] .toOption)
      _ <- ClientDao.insert(insertMe).repeat(Schedule.recurs(10))
//      _ <- ZIO log get.toString
    } yield()
  }.provide(
    ApplicationDaoImpl.live,
    ClientDaoImpl.live,
    CreditDaoImpl.live,
    CreditStatusDaoImpl.live,
    MaritalStatusDaoImpl.live,
    GenderDaoImpl.live,
    DslConnection.live,
    PassportDaoImpl.live,
    EmploymentDaoImpl.live
  )
}
