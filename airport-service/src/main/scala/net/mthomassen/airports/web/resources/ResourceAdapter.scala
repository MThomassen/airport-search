package net.mthomassen.airports.web.resources

import net.mthomassen.airports.data.entities.{Airports, Countries, Runways}
import net.mthomassen.airports.web.hal.{Link, ResourceBuilder}
import net.mthomassen.airports.web.resources.Airport.AirportProtocol
import net.mthomassen.airports.web.resources.Country.CountryProtocol
import net.mthomassen.airports.web.resources.CountryCompletionResult.CountryCompletionResultProtocol
import net.mthomassen.airports.web.resources.Runway.RunwayProtocol
import net.mthomassen.airports.web.routes.{AirportRoutes, CountryRoutes}
import spray.json._

trait ResourceAdapter extends AirportProtocol with RunwayProtocol with CountryProtocol with CountryCompletionResultProtocol {

//  TODO(Michiel): ResourceBuilder Marshaller (application/hal+json)
//  implicit val halMarshaller: ToResponseMarshaller[ResourceBuilder] = {
//
//  }

  def toResource(airport: Airports, runways: Seq[Runways]): JsValue = {

    val embeddedData = if (runways.isEmpty) None
    else Some(Map("runway" -> runways.map(toResource)))

    ResourceBuilder(
      withData = Some(Airport.fromEntity(airport).toJson),
      withEmbedded = embeddedData,
      withLinks = Some(Map(
        "self" -> Link(href = AirportRoutes.airportResourceHref(airport.ident))
      ))
    )
      .build

  }

  def toResource(runway: Runways): JsValue = {
    ResourceBuilder(
      withData = Some(Runway.fromEntity(runway).toJson),
      withLinks = Some(Map(
        "self" -> Link(href = AirportRoutes.runwayResourceHref(runway.airportIdent, runway.id))
      ))
    )
      .build
  }

  def toResource(country: Countries): JsValue = {
    ResourceBuilder(
      withData = Some(Country.fromEntity(country).toJson),
      withLinks = Some(Map(
        "self" -> Link(href = CountryRoutes.resourceHref(country.code))
      ))
    )
      .build
  }

  def toResource(complResult: CountryCompletionResult): JsValue = {
    ResourceBuilder(
      withData = Some(CountryCompletionResult(complResult.label, complResult.code).toJson),
      withLinks = Some(Map(
        "country" -> Link(href = CountryRoutes.resourceHref(complResult.code))
      ))
    )
      .build
  }

  def notFoundResource: JsValue = {
    ResourceBuilder(
      withData = Some("Requested Resource Not Found".toJson)
    ).build
  }
}
