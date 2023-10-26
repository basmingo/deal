package ru.neoflex.deal.model

import ru.neoflex.deal.model.jsonb.{Employment, Passport}
import zio.{Task, ZIO}
import zio.json.DecoderOps

import java.time.LocalDate

case class ClientDbResponseDto(
  clientId: Integer,
  firstName: String,
  lastName: String,
  middleName: String,
  birthdate: LocalDate,
  email: String,
  gender: String,
  maritalStatus: String,
  dependentAmount: Integer,
  account: String
)

case class Client(
  clientId: Option[Int],
  firstName: String,
  lastName: String,
  middleName: Option[String],
  birthdate: LocalDate,
  email: String,
  gender: String,
  maritalStatus: String,
  dependentAmount: Option[Integer],
  account: Option[String],
  passport: Seq[Passport],
  employment: Seq[Employment]
)

object ClientMapper {
  def toClient(
    clientData: ClientDbResponseDto,
    passportData: Seq[Passport],
    employmentData: Seq[Employment]
  ): Task[Client] =
    ZIO.succeed(
      Client(
        Option(clientData.clientId.toInt),
        clientData.firstName,
        clientData.lastName,
        Option(clientData.middleName),
        clientData.birthdate,
        clientData.email,
        clientData.gender,
        clientData.maritalStatus,
        Option(clientData.dependentAmount),
        Option(clientData.account),
        passportData,
        employmentData
      )
    )
}
