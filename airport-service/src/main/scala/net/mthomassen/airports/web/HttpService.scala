package net.mthomassen.airports.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import net.mthomassen.airports.web.configuration.AirportsConfig
import net.mthomassen.airports.web.routes.HasRoute
import org.slf4j.LoggerFactory

import scala.concurrent.Future


class HttpService(routes: HasRoute*)(implicit val system: ActorSystem) extends Directives
  with CORSHandler
  with CacheHandler
  with ExceptionHandler
  with AirportsConfig {
  private[this] val Log = LoggerFactory.getLogger(getClass)

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private[this] val healthRoute: Route = path("health") {
    pathEndOrSingleSlash {
      get {
        complete(StatusCodes.OK)
      }
    }
  }

  val route: Route = Route.seal(pathPrefix("api") {
    pathPrefix("v01") {
      handleAllExceptions {
        handleUriCache {
          routes
            .foldLeft(healthRoute)((acc, routable) => acc ~ routable.route)
        }
      }
    }
  })

  /**
    * Starts the Web Server
    * @return Binding
    */
  def start: Future[ServerBinding] = {
    Log.info(s"Starting Web Service ont {}:{} ..", HttpInterface, HttpPort)

    Http().bindAndHandle(
      handler = corsHandler(route),
      interface = HttpInterface,
      port = HttpPort)
  }
}
