package net.mthomassen.airports

package object data {
  type CountryCode = String
  type CountryLabel = String

  type AirportIdentity = String

  type RunwayIdentity = Int

  object CountryTokenType extends Enumeration {
    val Name, Synonym, Code = Value
  }

  object Report extends Enumeration {
    val CountriesHighestNumAirports,
    CountriesLowestNumAirports,
    CountriesRunwayTypes,
    MostCommonRunwayIdentifiactions = Value
  }
}
