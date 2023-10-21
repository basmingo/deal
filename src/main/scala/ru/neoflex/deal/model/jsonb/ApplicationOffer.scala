package ru.neoflex.deal.model.jsonb

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

case class ApplicationOffer(name: String, nameL: String)

object ApplicationOffer {
  implicit val decoder: JsonDecoder[ApplicationOffer] = DeriveJsonDecoder.gen[ApplicationOffer]
  implicit val encoder: JsonEncoder[ApplicationOffer] = DeriveJsonEncoder.gen[ApplicationOffer]
}
