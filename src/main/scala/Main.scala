import ru.neoflex.deal.configuration.{DslConnection, JooqDsl}
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
    val client = Client(ClientDbResponseDto(null, "Zilko", "Zobrich", null, LocalDate.now, "zilko@mail", "MALE", "SINGLE", 1000, "?"), Seq(Passport(1234, 123456, "no", "noo")), Seq(Employment("?", "?", BigDecimal(100), "OWNER", 112, 110)))
//    val credit = Credit(CreditJooqData(null, 5, java.math.BigDecimal.valueOf(1), java.math.BigDecimal.valueOf(0.3), java.math.BigDecimal.valueOf(0.14), true, false, "CALCULATED"))
    for {
      dsl <- ZIO.service[JooqDsl]
      get <- CreditDao.get(3, dsl)
      _   <- ZIO.log(get.toString)
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
