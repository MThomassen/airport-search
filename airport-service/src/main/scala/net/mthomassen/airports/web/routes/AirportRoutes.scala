package net.mthomassen.airports.web.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{CacheDirectives, `Cache-Control`}
import akka.http.scaladsl.server.{Directives, Route}
import kamon.akka.http.TracingDirectives
import net.mthomassen.airports.data.{AirportIdentity, AirportSource, RunwayIdentity, RunwaySource}
import net.mthomassen.airports.web.resources.ResourceAdapter
import org.slf4j.{Logger, LoggerFactory}

class AirportRoutes(airportSource: AirportSource, runwaySource: RunwaySource)(implicit system: ActorSystem) extends Directives
    with ResourceAdapter
    with HasRoute
    with TracingDirectives {

  def this(sources: AirportSource with RunwaySource)(implicit system: ActorSystem) {
    this(sources, sources)
  }

  private[this] val Log: Logger = LoggerFactory.getLogger(getClass)
  private[this] val DefaultCacheAge = 86400

  import AirportRoutes._
  import system.dispatcher

  val route: Route = respondWithHeaders(`Cache-Control`(CacheDirectives.`max-age`(DefaultCacheAge), CacheDirectives.public)) {
    pathPrefix(AirportResourceName) {
      pathEndOrSingleSlash {
        get {
          parameters('country_code) { countryCode =>
            operationName(s"${AirportResourceName}ByCountryCode") {
              complete(
                airportSource
                  .airportsByCountryCode(countryCode)
                  .map(_.map{ case (airportEnt, runwaysEnts) =>
                    toResource(airportEnt, runwaysEnts)
                  })
              )
            }
          }
        }
      } ~
        pathPrefix(Segment) { airportIdent =>
          pathEndOrSingleSlash {
            get {
              operationName(AirportResourceName) {
                complete(
                  airportSource
                    .airportByIdent(airportIdent)
                    .map {
                      case None => ToResponseMarshallable((StatusCodes.NotFound, notFoundResource))
                      case Some((airportEnt, runwaysEnts)) =>
                        ToResponseMarshallable(toResource(airportEnt, runwaysEnts))
                    }
                )
              }
            }
          } ~
            pathPrefix(RunwayResourceName) {
              pathPrefix(IntNumber) { runwayId =>
                pathEndOrSingleSlash {
                  get {
                    operationName(RunwayResourceName) {
                      complete(
                        runwaySource
                          .runwayById(runwayId)
                          .map {
                            case None => ToResponseMarshallable((StatusCodes.NotFound, notFoundResource))
                            case Some(runwayEnt) => ToResponseMarshallable(toResource(runwayEnt))
                          }
                      )
                    }
                  }
                }
              }
            }
        }
    }
  }


}

object AirportRoutes {
  val AirportResourceName: String = "airport"
  val RunwayResourceName: String = "runway"

  def airportResourceHref(ident: AirportIdentity): String = {
    s"/$AirportResourceName/$ident"
  }

  def runwayResourceHref(ident: AirportIdentity, runwayId: RunwayIdentity): String = {
    s"/$AirportResourceName/$ident/$RunwayResourceName/$runwayId"
  }
}
