package net.mthomassen.airports.data.entities

case class Airports(
    id: Int,
    ident: String,
    `type`: String,
    name: String,
    latitudeDeg: Double,
    longitudeDeg: Double,
    elevationFt: Option[Int] = None,
    continent: String,
    isoCountry: String,
    isoRegion: String,
    municipality: Option[String] = None,
    scheduledService: String,
    gpsCode: Option[String] = None,
    iataCode: Option[String] = None,
    localCode: Option[String] = None,
    homeLink: Option[String] = None,
    wikipediaLink: Option[String] = None,
    keywords: Option[String] = None,
    created: String
)
