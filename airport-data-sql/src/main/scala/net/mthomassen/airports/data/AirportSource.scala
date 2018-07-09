package net.mthomassen.airports.data

import net.mthomassen.airports.data.entities.{Airports, Runways}

import scala.concurrent.{ExecutionContext, Future}

trait AirportSource {

  def airportByIdent(ident: AirportIdentity)(implicit ec: ExecutionContext): Future[Option[(Airports, Seq[Runways])]]

  def airportsByCountryCode(code: CountryCode)(implicit ec: ExecutionContext): Future[Seq[(Airports, Seq[Runways])]]

}
