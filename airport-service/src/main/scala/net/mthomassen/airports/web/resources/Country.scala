package net.mthomassen.airports.web.resources

import net.mthomassen.airports.data.entities.Countries

case class Country(
    code: String,
    name: String,
    continent: String,
    wikipediaLink: String,
    keywords: Option[Seq[String]]
)

object Country {
  val ResourceName: String = "country"

  def resourceHref(countyCode: String): String = {
    s"/$ResourceName/$countyCode"
  }

  def fromEntity(entity: Countries): Country = {
    Country(
      code = entity.code,
      name = entity.name,
      continent = entity.continent,
      wikipediaLink = entity.wikipediaLink,
      keywords = entity.keywords.map(_.split(", ").toSeq)
    )
  }

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
  import spray.json.DefaultJsonProtocol

  trait CountryProtocol extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val countryJsonFormat = jsonFormat5(Country.apply)
  }
}


