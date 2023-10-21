package ru.neoflex.deal.model.jsonb

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

case class Passport(series: Int, number: Int, issueBranch: String, issueDate: String)

object Passport {
  implicit val decoder: JsonDecoder[Passport] = DeriveJsonDecoder.gen[Passport]
  implicit val encoder: JsonEncoder[Passport] = DeriveJsonEncoder.gen[Passport]
}
