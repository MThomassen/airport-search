package net.mthomassen.airports.web.resources

import net.mthomassen.airports.data.entities.{Airports, Runways}

object TestResources {

  val testAirport = Airports(16766, "EHAM", "large_airport", "Amsterdam Airport Schiphol", 52.309, 4.764, Some(-11), "EU", "NL", "NL-NH", Some("Amsterdam"), "yes", Some("EHAM"), Some("AMS"), None, Some("http://www.schiphol.nl/"), Some("http://en.wikipedia.org/wiki/Amsterdam_Schiphol_Airport"), None, "2018-07-08 21:20:15")

  val testRunway = Runways(14676, 2513, "EHAM", Some(6608), Some(148), Some("ASP"), true, false, Some("04"), Some(52.300), Some(4.783), Some(-13), Some(41), None, Some("22"), Some(52.314), Some(4.803), Some(-14), Some(221), None, "2018-07-08 21:20:16")

}
