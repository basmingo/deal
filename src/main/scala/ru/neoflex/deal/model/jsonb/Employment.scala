package ru.neoflex.deal.model.jsonb

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

case class Employment(status: String,
                      employerInn: String,
                      salary: BigDecimal,
                      position: String,
                      workExperienceTotal: Int,
                      workExperienceCurrent: Int)

object Employment {
  implicit val decoder: JsonDecoder[Employment] = DeriveJsonDecoder.gen[Employment]
  implicit val encoder: JsonEncoder[Employment] = DeriveJsonEncoder.gen[Employment]
}
