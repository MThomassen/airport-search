package net.mthomassen.airports.data

import net.mthomassen.airports.data.entities.Countries

import scala.concurrent.{ExecutionContext, Future}

trait CountrySource {

  def countryByCode(code: CountryCode)(implicit ec: ExecutionContext): Future[Option[Countries]]

  def countryTokens: Seq[CountryToken]

}
