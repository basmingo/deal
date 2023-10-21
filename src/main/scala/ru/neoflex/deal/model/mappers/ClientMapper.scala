package ru.neoflex.deal.model.mappers

import org.jooq.Result
import ru.neoflex.deal.configuration.deal.tables.records.{ClientRecord, EmploymentRecord, PassportRecord}
import ru.neoflex.deal.model.Client
import ru.neoflex.deal.model.Gender
import ru.neoflex.deal.model.jsonb.{Employment, Passport}
import zio.Task
import zio.json.DecoderOps

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import scala.language.implicitConversions

object ClientMapper {
//  implicit def toModel(result: Task[(ClientRecord, Result[PassportRecord], Result[EmploymentRecord])]): Task[Client] = {
//    result.map(result =>
//      Client(
//        Some(result._1.getClientId),
//        result._1.getFirstName,
//        result._1.getLastName,
//        Option(result._1.getMiddleName),
//        result._1.getBirthDate,
//        result._1.getEmail,
//        result._1.getGenderId.toString,
//        result._1.getMaritalStatus.toString,
//        result._1.getDependentAmount.toLong,
//        result._1.getAccount,
//        result._2.map(_.getPassportData
//                        .toString
//                        .fromJson[Passport])
//                        .collect { case Right(value) => value }
//                        .toSeq,
//        result._3.map(_.getEmploymentData
//                        .toString
//                        .fromJson[Employment])
//                        .collect { case Right(value) => value }
//                        .toSeq
//      )
//    )
//  }
}
