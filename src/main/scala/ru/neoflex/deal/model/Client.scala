package ru.neoflex.deal.model

import ru.neoflex.deal.model.jsonb.{Employment, Passport}
import zio.json.DecoderOps

import java.time.LocalDate

case class ClientData (clientId: Integer,
                  firstName: String,
                  lastName: String,
                  middleName: String,
                  birthdate: LocalDate,
                  email: String,
                  gender: String,
                  maritalStatus: String,
                  dependentAmount: Integer,
                  account: String)

case class Client (clientData: ClientData,
                   passportData: Seq[Passport],
                   employmentData: Seq[Employment]) {

   val clientId: Option[Int] = Option(clientData.clientId.toInt)
   val firstName: String = clientData.firstName
   val lastName: String = clientData.lastName
   val middleName: Option[String] = Option(clientData.middleName)
   val birthdate: LocalDate = clientData.birthdate
   val email: String = clientData.email
   val gender: String = clientData.gender
   val maritalStatus: String = clientData.maritalStatus
   val dependentAmount: Option[Integer] = Option(clientData.dependentAmount)
   val account: Option[String] = Option(clientData.account)
   val passport: Seq[Passport] = passportData
   val employment: Seq[Employment] = employmentData
}

