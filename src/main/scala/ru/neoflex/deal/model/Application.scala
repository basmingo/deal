package ru.neoflex.deal.model

import ru.neoflex.deal.model.jsonb.ApplicationOffer

import java.time.LocalDateTime

case class Application(applicationId: Option[Integer],
                       clientId: Int,
                       creditId: Int,
                       created: LocalDateTime,
                       appliedOffer: Option[ApplicationOffer],
                       signed: Option[LocalDateTime],
                       sesCode: Option[Integer])
