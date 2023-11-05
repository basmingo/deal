package ru.neoflex.deal.model

import ru.neoflex.deal.model.jsonb.PaymentScheduleElement
import zio.{Task, ZIO}

case class CreditDbResponseDto(
  creditId: Integer,
  term: Integer,
  monthlyPayment: java.math.BigDecimal,
  rate: java.math.BigDecimal,
  psk: java.math.BigDecimal,
  withInsurance: Boolean,
  salaryClient: Boolean,
  creditStatus: String
)

case class Credit(
  creditId: Option[Integer],
  term: Integer,
  monthlyPayment: BigDecimal,
  rate: BigDecimal,
  psk: Option[BigDecimal],
  withInsurance: Boolean,
  salaryClient: Boolean,
  creditStatus: String,
  creditSchedule: Seq[PaymentScheduleElement]
)

object CreditMapper {
  def toCredit(creditData: CreditDbResponseDto, paymentSchedules: Seq[PaymentScheduleElement]): Task[Credit] =
    ZIO.succeed(
      Credit(
        creditId = Option(creditData.creditId),
        term = creditData.term,
        monthlyPayment = creditData.monthlyPayment,
        rate = creditData.rate,
        psk = Option(creditData.psk),
        withInsurance = creditData.withInsurance,
        salaryClient = creditData.salaryClient,
        creditStatus = creditData.creditStatus,
        creditSchedule = paymentSchedules
      )
    )
}
