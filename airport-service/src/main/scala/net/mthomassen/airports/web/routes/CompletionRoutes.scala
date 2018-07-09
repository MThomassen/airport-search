package net.mthomassen.airports.web.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.{CacheDirectives, `Cache-Control`}
import akka.http.scaladsl.server.{Directives, Route}
import kamon.akka.http.TracingDirectives
import net.mthomassen.airports.search.CountryCodeSearch
import net.mthomassen.airports.web.resources.{CountryCompletionResult, ResourceAdapter}
import org.slf4j.{Logger, LoggerFactory}

class CompletionRoutes(countryCodeSearch: CountryCodeSearch)(implicit system: ActorSystem) extends Directives
  with HasRoute
  with ResourceAdapter
  with TracingDirectives {
  private[this] val Log: Logger = LoggerFactory.getLogger(getClass)
  private[this] val DefaultCacheAge = 86400

  val route: Route = respondWithHeaders(`Cache-Control`(CacheDirectives.`max-age`(DefaultCacheAge), CacheDirectives.public)) {
    pathPrefix("complete" / "search") {
      pathEndOrSingleSlash {
        get {
          parameters('q, 'max_results.as[Int] ? 5) { (query, maxResults) =>
            operationName("queryCompletion") {
              complete(
                countryCodeSearch
                  .lookup(query, maxResults)
                  .map{ case (label, code) =>
                    toResource(CountryCompletionResult(label, code))
                  }
              )
            }
          }
        }
      }
    }
  }
}
