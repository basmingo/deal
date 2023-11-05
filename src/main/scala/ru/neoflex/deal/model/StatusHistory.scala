package ru.neoflex.deal.model

import zio.{Task, ZIO}

import java.time.LocalDateTime

case class StatusHistoryResponseDb(
  statusHistoryId: Integer,
  previousStatus: Integer,
  currentStatus: Integer,
  updated: LocalDateTime,
  changeType: String
)

case class StatusHistory(
  id: Option[Int],
  previousStatus: Option[Int],
  currentStatus: Int,
  updated: LocalDateTime,
  changeType: String
)

object StatusHistoryMapper {
  def toStatusHistory(statusHistoryResponse: Seq[StatusHistoryResponseDb]): Task[Seq[StatusHistory]] =
    ZIO.succeed(
      statusHistoryResponse.map(elem =>
        StatusHistory(
          Option(elem.statusHistoryId),
          Option(elem.previousStatus),
          elem.currentStatus,
          elem.updated,
          elem.changeType
        )
      )
    )
}
