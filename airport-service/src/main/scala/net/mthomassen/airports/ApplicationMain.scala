package net.mthomassen.airports

import akka.actor.ActorSystem
import kamon.Kamon
import kamon.influxdb.InfluxDBReporter
import net.mthomassen.airports.data.Database
import net.mthomassen.airports.search.LuceneCountryCodeSearch
import net.mthomassen.airports.web.HttpService
import net.mthomassen.airports.web.routes.{AirportRoutes, CompletionRoutes, CountryRoutes, ReportsRoute}
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ApplicationMain extends App {
  private[this] val Log = LoggerFactory.getLogger(getClass)

  Log.info("Started application")

  Kamon.addReporter(new InfluxDBReporter())

  implicit val system: ActorSystem = ActorSystem("airports")

  val service = new HttpService(
    new CountryRoutes(Database),
    new AirportRoutes(Database),
    new ReportsRoute(Database),
    new CompletionRoutes(new LuceneCountryCodeSearch(Database.countryTokens))
  )

  val binding = service.start

  Await.result(system.whenTerminated, Duration.Inf)

  Log.info("Application stopped")
}
