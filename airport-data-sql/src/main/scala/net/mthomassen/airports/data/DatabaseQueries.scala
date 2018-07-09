package net.mthomassen.airports.data

import io.getquill.{SnakeCase, SqliteJdbcContext}
import net.mthomassen.airports.data.entities.{Airports, Countries, Runways}

trait DatabaseQueries {
  val ctx: SqliteJdbcContext[SnakeCase]

  import ctx._

  protected def countryByCodeQry(code: CountryCode) = quote {
    query[Countries]
      .filter(_.code == lift(code))
  }

  protected val countryTokensQry = quote {
    infix"""
               SELECT  'Name' AS "_1"
               ,       code   AS "_2"
               ,       name   AS "_3"
               FROM   countries
               UNION ALL
               SELECT  'Code'
               ,       code
               ,       code
               FROM   countries
               UNION ALL
               SELECT  'Synonym'
               ,       code
               ,       keywords
               FROM    countries
               WHERE   keywords IS NOT NULL
             """
      .as[Query[(String, CountryCode, CountryLabel)]]
  }

  protected def airportByIdentQry(ident: AirportIdentity) = quote {
    query[Airports]
      .filter(_.ident == lift(ident))
      .leftJoin(query[Runways])
      .on((a, r) => a.ident == r.airportIdent)
  }

  protected def airportByCountryCodeQry(code: CountryCode) = quote {
    query[Airports]
      .filter(_.isoCountry == lift(code))
      .leftJoin(query[Runways])
      .on((a, r) => a.ident == r.airportIdent)
  }

  protected def runwayByIdQry(id: RunwayIdentity) = quote {
    query[Runways]
      .filter(_.id == lift(id))
  }
}
