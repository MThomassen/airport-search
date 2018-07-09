package net.mthomassen.airports.web.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.MissingQueryParamRejection
import akka.http.scaladsl.testkit.ScalatestRouteTest
import net.mthomassen.airports.data._
import net.mthomassen.airports.search.CountryCodeSearch
import org.scalatest.{Matchers, WordSpecLike}

class CompletionRoutesSpec extends WordSpecLike with Matchers with ScalatestRouteTest {

  "Completion Route" should {
    val stubbedRoute = new CompletionRoutes(new CountryCodeSearch {
      override def lookup(query: String, maxResults: RunwayIdentity): Seq[(CountryLabel, CountryCode)] = Seq(("Venezuela","VE"))
    }).route

    "Complete Query" in {
      Get("/complete/search?q=V") ~> stubbedRoute ~> check {
        status shouldBe StatusCodes.OK
        responseAs[String] shouldBe """[{"label":"Venezuela","code":"VE","_links":{"country":{"href":"/country/VE"}}}]"""
      }
    }

    "Fail when no query string" in {
      Get("/complete/search") ~> stubbedRoute ~> check {
        rejection shouldBe MissingQueryParamRejection("q")
      }
    }
  }

}
