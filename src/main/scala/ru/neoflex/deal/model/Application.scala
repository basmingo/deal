package ru.neoflex.deal.model

import org.jooq.JSONB
import ru.neoflex.deal.model.jsonb.ApplicationOffer
import zio.json.DecoderOps
import zio.{Task, ZIO}

import java.time.LocalDateTime

case class ApplicationData(
  applicationId: Integer,
  client: Client,
  credit: Credit,
  created: LocalDateTime,
  appliedOffer: JSONB,
  signed: LocalDateTime,
  sesCode: Integer
)

case class Application(
  applicationId: Option[Integer],
  client: Client,
  credit: Credit,
  created: LocalDateTime,
  appliedOffer: Option[ApplicationOffer],
  signed: Option[LocalDateTime],
  sesCode: Option[Integer]
) {}

object ApplicationMapper {
  def toApplication(applicationData: ApplicationData): Task[Application] =
    ZIO.succeed(
      Application(
        Option(applicationData.applicationId),
        applicationData.client,
        applicationData.credit,
        applicationData.created,
        applicationData.appliedOffer.toString
          .fromJson[ApplicationOffer]
          .toOption,
        Option(applicationData.signed),
        Option(applicationData.sesCode)
      )
    )
}
