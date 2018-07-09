package net.mthomassen.airports.web

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server._
import org.slf4j.LoggerFactory

trait ExceptionHandler extends Directives {
  private[this] val Log = LoggerFactory.getLogger(getClass)

  private[this] val exceptionHandler = ExceptionHandler {
    case ex: Throwable =>
      extractUri { uri =>
        Log.error(s"Request to $uri could not be handled normally", ex)
        complete(StatusCodes.InternalServerError)
      }
  }

  def handleAllExceptions: Directive0 = handleExceptions(exceptionHandler)
}
