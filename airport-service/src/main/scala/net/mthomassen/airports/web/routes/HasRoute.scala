package net.mthomassen.airports.web.routes

import akka.http.scaladsl.server.Route

trait HasRoute {
  def route: Route
}
