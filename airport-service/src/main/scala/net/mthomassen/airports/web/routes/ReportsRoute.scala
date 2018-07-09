package net.mthomassen.airports.web.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import net.mthomassen.airports.data.{Database, Report, ReportSource}
import net.mthomassen.airports.web.resources.TabularProtocol

import scala.util.{Failure, Success, Try}

class ReportsRoute(reportSourse: ReportSource)(implicit system: ActorSystem) extends Directives with HasRoute with TabularProtocol {

  import ReportsRoute._
  import system.dispatcher

  val route: Route = pathPrefix(ResourceName) {
    pathEndOrSingleSlash {
      get {
        parameters('r) { r =>
          complete(
            Try(Report.withName(r)) match {
              case Failure(ex) => (StatusCodes.BadRequest, s"ReportType should be one of ${Report.values}")
              case Success(report) => Database.report(report)
            }
          )
        }
      }
    }
  }
}

object ReportsRoute {
  val ResourceName: String = "report"
}
