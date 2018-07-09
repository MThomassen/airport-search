package net.mthomassen.airports.web.configuration

import com.typesafe.config.ConfigFactory

trait AirportsConfig {
  private[this] val config = ConfigFactory.load()

  val HttpInterface: String = config.getString("airports.http.interface")
  val HttpPort: Int = config.getInt("airports.http.port")

}
