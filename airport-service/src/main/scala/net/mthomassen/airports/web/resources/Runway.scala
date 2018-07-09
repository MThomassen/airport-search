package net.mthomassen.airports.web.resources

import net.mthomassen.airports.data.entities.Runways

case class Runway(
    airportRef: Int,
    airportIdent: String,
    lengthFt: Option[Int],
    widthFt: Option[Int],
    surface: Option[String],
    lighted: Boolean,
    closed: Boolean,
    leIdent: Option[String],
    leLatitudeDeg: Option[Double],
    leLongitudeDeg: Option[Double],
    leElevationFt: Option[Int],
    leHeadingDegt: Option[Double],
    leDisplacedThresholdFt: Option[Int],
    heIdent: Option[String],
    heLatitudeDeg: Option[Double],
    heLongitudeDeg: Option[Double],
    heElevationFt: Option[Int],
    heHeadingDegt: Option[Double],
    heDisplacedThresholdFt: Option[Int]
)

object Runway {
  val ResourceName: String = "runway"

  def resourceHref(entityId: Int): String = {
    s"/$ResourceName/$entityId"
  }

  def fromEntity(entity: Runways): Runway = {
    Runway(
      airportRef = entity.airportRef,
      airportIdent = entity.airportIdent,
      lengthFt = entity.lengthFt,
      widthFt = entity.widthFt,
      surface = entity.surface,
      lighted = entity.lighted,
      closed = entity.closed,
      leIdent = entity.leIdent,
      leLatitudeDeg = entity.leLatitudeDeg,
      leLongitudeDeg = entity.leLongitudeDeg,
      leElevationFt = entity.leElevationFt,
      leHeadingDegt = entity.leHeadingDegt,
      leDisplacedThresholdFt = entity.leDisplacedThresholdFt,
      heIdent = entity.heIdent,
      heLatitudeDeg = entity.heLatitudeDeg,
      heLongitudeDeg = entity.heLongitudeDeg,
      heElevationFt = entity.heElevationFt,
      heHeadingDegt = entity.heHeadingDegt,
      heDisplacedThresholdFt = entity.heDisplacedThresholdFt
    )
  }

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
  import spray.json.DefaultJsonProtocol

  trait RunwayProtocol extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val runwayJsonFormat = jsonFormat19(Runway.apply)
  }

}
