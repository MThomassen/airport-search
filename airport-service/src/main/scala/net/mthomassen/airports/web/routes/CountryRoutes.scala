package net.mthomassen.airports.web.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.{CacheDirectives, `Cache-Control`}
import akka.http.scaladsl.server.{Directives, Route}
import kamon.akka.http.TracingDirectives
import net.mthomassen.airports.data.{CountryCode, CountrySource}
import net.mthomassen.airports.web.resources.ResourceAdapter
import org.slf4j.{Logger, LoggerFactory}

class CountryRoutes(countrySource: CountrySource)(implicit system: ActorSystem) extends Directives
  with ResourceAdapter
  with HasRoute
  with TracingDirectives {
  private[this] val Log: Logger = LoggerFactory.getLogger(getClass)
  private[this] val DefaultCacheAge = 86400

  import CountryRoutes._
  import system.dispatcher

  val route: Route = respondWithHeaders(`Cache-Control`(CacheDirectives.`max-age`(DefaultCacheAge), CacheDirectives.public)) {
    pathPrefix(ResourceName) {
      pathPrefix(Segment) { countryCode =>
        pathEndOrSingleSlash {
          operationName(ResourceName) {
            get {
              complete(
                countrySource
                  .countryByCode(countryCode)
                  .map(_.map(toResource))
              )
            }
          }
        }
      }
    }
  }
}

object CountryRoutes {
  val ResourceName: String = "country"

  def resourceHref(code: CountryCode): String = {
    s"/$ResourceName/$code"
  }

}
