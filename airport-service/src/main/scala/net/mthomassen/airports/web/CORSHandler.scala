package net.mthomassen.airports.web

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Route}
import net.mthomassen.airports.web.configuration.AirportsConfig

/**
  * From http://agrajmangal.in/blog/akka-http/handling-cors-in-akka-http/
  */
trait CORSHandler extends AirportsConfig {
  private[this] val corsResponseHeaders = List(
    `Access-Control-Allow-Origin`.*,
    `Access-Control-Allow-Credentials`(true),
    `Access-Control-Allow-Headers`("Authorization", "Content-Type", "X-Requested-With")
  )

  //this directive adds access control headers to normal responses
  private[this] def addAccessControlHeaders: Directive0 = {
    respondWithHeaders(corsResponseHeaders)
  }

  //this handles preflight OPTIONS requests.
  private[this] def preflightRequestHandler: Route = options {
    complete(HttpResponse(StatusCodes.OK).
      withHeaders(`Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)))
  }

  // Wrap the Route with this method to enable adding of CORS headers
  def corsHandler(r: Route): Route = addAccessControlHeaders {
    preflightRequestHandler ~ r
  }

  // Helper method to add CORS headers to HttpResponse
  // preventing duplication of CORS headers across code
  def addCORSHeaders(response: HttpResponse): HttpResponse =
    response.withHeaders(corsResponseHeaders)

}
