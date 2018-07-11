package net.mthomassen.airports.web.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import net.mthomassen.airports.data._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.{ExecutionContext, Future}

class AirportRoutesSpec extends WordSpecLike with Matchers with MockFactory with ScalatestRouteTest {
  import net.mthomassen.airports.web.resources.TestResources._

  private[this] lazy val airportSourceStub = stub[AirportSource]
  private[this] lazy val runwaySourceStub = stub[RunwaySource]

  "Airport Routes" should {
    val stubbedRoute = new AirportRoutes(airportSourceStub, runwaySourceStub).route

    "find airports by country code" in {

      (airportSourceStub.airportsByCountryCode(_: CountryCode)(_: ExecutionContext)) when ("NL", *) returns Future.successful(Seq((testAirport, Seq(testRunway))))

      Get("/airport?country_code=NL") ~> stubbedRoute ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe
          """[{"name":"Amsterdam Airport Schiphol","iataCode":"AMS","elevationFt":-11,"scheduledService":"yes","wikipediaLink":"http://en.wikipedia.org/wiki/Amsterdam_Schiphol_Airport","_embedded":{"runway":[{"leLongitudeDeg":4.783,"heElevationFt":-14,"heLatitudeDeg":52.314,"surface":"ASP","closed":false,"airportRef":2513,"airportIdent":"EHAM","leHeadingDegt":41.0,"heLongitudeDeg":4.803,"heHeadingDegt":221.0,"leElevationFt":-13,"leIdent":"04","lengthFt":6608,"widthFt":148,"leLatitudeDeg":52.3,"lighted":true,"heIdent":"22","_links":{"self":{"href":"/airport/EHAM/runway/14676"}}}]},"isoRegion":"NL-NH","ident":"EHAM","longitudeDeg":4.764,"latitudeDeg":52.309,"homeLink":"http://www.schiphol.nl/","gpsCode":"EHAM","type":"large_airport","municipality":"Amsterdam","_links":{"self":{"href":"/airport/EHAM"}}}]"""
      }
    }

    "find airport" in {

      (airportSourceStub.airportByIdent(_: AirportIdentity)(_: ExecutionContext)) when ("EHAM", *) returns Future.successful(Some((testAirport, Seq(testRunway))))

      Get("/airport/EHAM") ~> stubbedRoute ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe
          """{"name":"Amsterdam Airport Schiphol","iataCode":"AMS","elevationFt":-11,"scheduledService":"yes","wikipediaLink":"http://en.wikipedia.org/wiki/Amsterdam_Schiphol_Airport","_embedded":{"runway":[{"leLongitudeDeg":4.783,"heElevationFt":-14,"heLatitudeDeg":52.314,"surface":"ASP","closed":false,"airportRef":2513,"airportIdent":"EHAM","leHeadingDegt":41.0,"heLongitudeDeg":4.803,"heHeadingDegt":221.0,"leElevationFt":-13,"leIdent":"04","lengthFt":6608,"widthFt":148,"leLatitudeDeg":52.3,"lighted":true,"heIdent":"22","_links":{"self":{"href":"/airport/EHAM/runway/14676"}}}]},"isoRegion":"NL-NH","ident":"EHAM","longitudeDeg":4.764,"latitudeDeg":52.309,"homeLink":"http://www.schiphol.nl/","gpsCode":"EHAM","type":"large_airport","municipality":"Amsterdam","_links":{"self":{"href":"/airport/EHAM"}}}"""
      }
    }

    "find nonexisting airport" in {

      (airportSourceStub.airportByIdent(_: AirportIdentity)(_: ExecutionContext)) when ("FAKE", *) returns Future.successful(None)

      Get("/airport/FAKE") ~> stubbedRoute ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }



    "find runway" in {

      (runwaySourceStub.runwayById(_: RunwayIdentity)(_: ExecutionContext)) when (14676, *) returns Future.successful(Some(testRunway))

      Get("/airport/EHAM/runway/14676") ~> stubbedRoute ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe
          """{"leLongitudeDeg":4.783,"heElevationFt":-14,"heLatitudeDeg":52.314,"surface":"ASP","closed":false,"airportRef":2513,"airportIdent":"EHAM","leHeadingDegt":41.0,"heLongitudeDeg":4.803,"heHeadingDegt":221.0,"leElevationFt":-13,"leIdent":"04","lengthFt":6608,"widthFt":148,"leLatitudeDeg":52.3,"lighted":true,"heIdent":"22","_links":{"self":{"href":"/airport/EHAM/runway/14676"}}}"""
      }
    }

    "find nonexisting runway" in {

      (runwaySourceStub.runwayById(_: RunwayIdentity)(_: ExecutionContext)) when (99999, *) returns Future.successful(None)

      Get("/airport/EHAM/runway/99999") ~> stubbedRoute ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

  }

}
