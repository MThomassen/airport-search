package net.mthomassen.airports.web.resources

import net.mthomassen.airports.data.entities.Airports

case class Airport(
    ident: String,
    `type`: String,
    name: String,
    latitudeDeg: Double,
    longitudeDeg: Double,
    elevationFt: Option[Int],
    isoRegion: String,
    municipality: Option[String],
    scheduledService: String,
    gpsCode: Option[String],
    iataCode: Option[String],
    localCode: Option[String],
    homeLink: Option[String],
    wikipediaLink: Option[String],
    keywords: Option[Seq[String]]
)

object Airport {
  def fromEntity(entity: Airports): Airport = {
    Airport(
      ident = entity.ident,
      `type` = entity.`type`,
      name = entity.name,
      latitudeDeg = entity.latitudeDeg,
      longitudeDeg = entity.longitudeDeg,
      elevationFt = entity.elevationFt,
      isoRegion = entity.isoRegion,
      municipality = entity.municipality,
      scheduledService = entity.scheduledService,
      gpsCode = entity.gpsCode,
      iataCode = entity.iataCode,
      localCode = entity.localCode,
      homeLink = entity.homeLink,
      wikipediaLink = entity.wikipediaLink,
      keywords = entity.keywords.map(_.split(", ").toSeq)
    )
  }

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
  import spray.json.DefaultJsonProtocol

  trait AirportProtocol extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val airportJsonFormat = jsonFormat15(Airport.apply)
  }
}
