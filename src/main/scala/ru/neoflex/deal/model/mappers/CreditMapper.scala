package ru.neoflex.deal.model.mappers

import ru.neoflex.deal.configuration.deal.tables.records.CreditRecord
import ru.neoflex.deal.model.Credit
import zio.Task

import scala.language.implicitConversions

object CreditMapper {
   implicit def toModel(result: Task[CreditRecord]): Task[Credit] =
    result.map(
      result => Credit(
        Some(result.getCreditId),
        result.getTerm,
        result.getMonthlyPayment,
        result.getRate,
        Option(result.getPsk),
        result.getWithInsurance,
        result.getSalaryClient,
        result.getCreditStatus.toString)
    )
}
