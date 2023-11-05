package ru.neoflex.deal.model.dao

import ru.neoflex.deal.configuration.JooqDsl
import ru.neoflex.deal.model.{StatusHistory, StatusHistoryMapper, StatusHistoryResponseDb}
import ru.neoflex.deal.configuration.deal.tables.StatusHistory._
import ru.neoflex.deal.configuration.deal.tables.ChangeType._
import zio.{Task, ZIO}

import scala.jdk.CollectionConverters.ListHasAsScala

trait StatusHistoryDao {

  def getByApplicationId(id: Int, dsl: JooqDsl): Task[Seq[StatusHistory]]

  def insert(statusHistory: StatusHistory, dsl: JooqDsl)

}

case class StatusHistoryDaoImpl() extends StatusHistoryDao {
  override def getByApplicationId(applicationId: Int, dsl: JooqDsl): Task[Seq[StatusHistory]] =
    for {
      ctx <- dsl.getJooqContext
      historyResponse <- ZIO.succeed(
                           ctx
                             .select(
                               STATUS_HISTORY.STATUS_HISTORY_ID,
                               STATUS_HISTORY.PREVIOUS_STATUS_ID,
                               STATUS_HISTORY.CURRENT_STATUS_ID,
                               STATUS_HISTORY.UPDATED,
                               STATUS_HISTORY.CHANGE_TYPE_ID
                             )
                             .from(STATUS_HISTORY)
                             .join(CHANGE_TYPE)
                             .on(STATUS_HISTORY.CHANGE_TYPE_ID.eq(CHANGE_TYPE.CHANGE_TYPE_ID))
                             .where(STATUS_HISTORY.APPLICATION_ID.eq(applicationId))
                             .fetch()
                             .into(classOf[StatusHistoryResponseDb])
                         )
      statusHistoryResult <- StatusHistoryMapper.toStatusHistory(historyResponse.asScala.toList)
    } yield statusHistoryResult

  override def insert(statusHistory: StatusHistory, dsl: JooqDsl): Unit = ???
}
