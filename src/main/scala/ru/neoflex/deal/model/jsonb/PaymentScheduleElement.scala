package ru.neoflex.deal.model.jsonb

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

import java.time.LocalDate

case class PaymentScheduleElement(
  number: Int,
  date: LocalDate,
  totalPayment: BigDecimal,
  interestPayment: BigDecimal,
  debtPayment: BigDecimal,
  remainingDebt: BigDecimal
)

object PaymentScheduleElement {
  implicit val decoder: JsonDecoder[PaymentScheduleElement] = DeriveJsonDecoder.gen[PaymentScheduleElement]
  implicit val encoder: JsonEncoder[PaymentScheduleElement] = DeriveJsonEncoder.gen[PaymentScheduleElement]
}
