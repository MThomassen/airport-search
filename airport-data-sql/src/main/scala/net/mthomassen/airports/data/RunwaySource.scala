package net.mthomassen.airports.data

import net.mthomassen.airports.data.entities.Runways

import scala.concurrent.{ExecutionContext, Future}

trait RunwaySource {

  def runwayById(id: RunwayIdentity)(implicit ec: ExecutionContext): Future[Option[Runways]]
}
