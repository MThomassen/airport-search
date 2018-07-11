package net.mthomassen.airports.web.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.MissingQueryParamRejection
import akka.http.scaladsl.testkit.ScalatestRouteTest
import net.mthomassen.airports.search.CountryCodeSearch
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpecLike}

class CompletionRoutesSpec extends WordSpecLike with Matchers with MockFactory with ScalatestRouteTest {

  private[this] lazy val searchStub = stub[CountryCodeSearch]

  "Completion Route" should {
    val stubbedRoute = new CompletionRoutes(searchStub).route

    "Complete Query" in {
      searchStub.lookup _ when ("V", *) returns Seq(("Venezuela","VE"))

      Get("/complete/search?q=V") ~> stubbedRoute ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe
          """[{"label":"Venezuela","code":"VE","_links":{"country":{"href":"/country/VE"}}}]"""
      }
    }

    "Fail when no query string" in {
      Get("/complete/search") ~> stubbedRoute ~> check {
        rejection shouldBe MissingQueryParamRejection("q")
      }
    }
  }

}
