package net.mthomassen.airports.data.entities

case class Runways(
    id: Int,
    airportRef: Int,
    airportIdent: String,
    lengthFt: Option[Int] = None,
    widthFt: Option[Int] = None,
    surface: Option[String] = None,
    lighted: Boolean,
    closed: Boolean,
    leIdent: Option[String] = None,
    leLatitudeDeg: Option[Double] = None,
    leLongitudeDeg: Option[Double] = None,
    leElevationFt: Option[Int] = None,
    leHeadingDegt: Option[Double] = None,
    leDisplacedThresholdFt: Option[Int] = None,
    heIdent: Option[String] = None,
    heLatitudeDeg: Option[Double] = None,
    heLongitudeDeg: Option[Double] = None,
    heElevationFt: Option[Int] = None,
    heHeadingDegt: Option[Double] = None,
    heDisplacedThresholdFt: Option[Int] = None,
    created: String
)
