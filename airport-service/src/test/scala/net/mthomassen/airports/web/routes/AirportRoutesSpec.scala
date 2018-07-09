package net.mthomassen.airports.web.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import net.mthomassen.airports.data.entities.{Airports, Runways}
import net.mthomassen.airports.data._
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.{ExecutionContext, Future}

class AirportRoutesSpec extends WordSpecLike with Matchers with ScalatestRouteTest {

  "Airport Routes" should {
    val stubbedRoute = new AirportRoutes(new AirportSource with RunwaySource {
      override def airportsByCountryCode(code: CountryCode)(implicit ec: ExecutionContext): Future[Seq[(Airports, Seq[Runways])]] = Future.successful(Nil)
      override def airportByIdent(ident: AirportIdentity)(implicit ec: ExecutionContext): Future[Option[(Airports, Seq[Runways])]] = Future.successful(None)
      override def runwayById(id: RunwayIdentity)(implicit ec: ExecutionContext): Future[Option[Runways]] = Future.successful(None)
    }).route

    "find airports by country code" in {
      Get("/airport?country_code=NL") ~> stubbedRoute ~> check {
        status shouldBe StatusCodes.OK
      }
    }

    "find airport" in {
      Get("/airport/ABC") ~> stubbedRoute ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

    "find runway" in {
      Get("/airport/ABC/runway/123") ~> stubbedRoute ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

  }

}
