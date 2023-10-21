package ru.neoflex.deal.model.mappers

import ru.neoflex.deal.configuration.deal.tables.records.ApplicationRecord
import ru.neoflex.deal.model.Application
import ru.neoflex.deal.model.jsonb.ApplicationOffer
import zio.Task
import zio.json.DecoderOps

import scala.language.implicitConversions

object ApplicationMapper {
  implicit def toModel(result: Task[ApplicationRecord]): Task[Application] =
    result.map(result =>
      Application(
        Some(result.getApplicationId),
        result.getClientId,
        result.getCreditId,
        result.getCreated,
        Option(result.getAppliedOffer).flatMap(_.toString.fromJson[ApplicationOffer].toOption),
        Option(result.getSigned),
        Option(result.getSesCode)
      )
    )
}
