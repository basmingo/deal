package ru.neoflex.deal.model

import ru.neoflex.deal.model.jsonb.ApplicationOffer

import java.time.LocalDateTime

case class ApplicationData(applicationId: Integer,
                           client: Client,
                           credit: Credit,
                           created: LocalDateTime,
                           appliedOffer: ApplicationOffer,
                           signed: LocalDateTime,
                           sesCode: Integer)
case class Application(applicationData: ApplicationData) {
   val applicationId: Option[Integer] = Option(applicationData.applicationId)
   val clientId: Client = applicationData.client
   val creditId: Credit = applicationData.credit
   val created: LocalDateTime = applicationData.created
   val appliedOffer: Option[ApplicationOffer] = Option(applicationData.appliedOffer)
   val signed: Option[LocalDateTime] = Option(applicationData.signed)
   val sesCode: Option[Integer] = Option(applicationData.sesCode)
}
