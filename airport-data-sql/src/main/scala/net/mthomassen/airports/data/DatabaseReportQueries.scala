package net.mthomassen.airports.data

import io.getquill.{SnakeCase, SqliteJdbcContext}

trait DatabaseReportQueries {
  val ctx: SqliteJdbcContext[SnakeCase]

  import ctx._

  protected val countryHighestNumAirportsQry = quote {
    infix"""
               SELECT cty.name  AS "_1"
               ,      COUNT(*)  AS "_2"
               FROM   countries AS cty
               JOIN   airports  AS apt
                      ON (cty.code = apt.iso_country)
               GROUP  BY cty.name
               ORDER  BY COUNT(*) DESC
               LIMIT  10
             """
      .as[Query[(CountryLabel, Int)]]
  }

  protected val countryLowestNumAirportsQry = quote {
    infix"""
               SELECT cty.name                    AS "_1"
               ,      COALESCE(COUNT(apt.id), 0)  AS "_2"
               FROM   countries AS cty
               LEFT OUTER JOIN   airports  AS apt
                      ON (cty.code = apt.iso_country)
               GROUP  BY cty.name
               ORDER  BY COALESCE(COUNT(apt.id), 0) ASC
               LIMIT  10
             """
      .as[Query[(CountryLabel, Int)]]
  }

  /**
    * A decent DB would require less convoluted techniques
    */
  protected val countryRunwayTypesQry = quote {
    infix"""
            WITH country_runways
            AS ( SELECT DISTINCT cty.name
                 ,      rwy.surface
                 FROM   countries AS cty
                 JOIN   airports  AS apt ON (cty.code  = apt.iso_country)
                 JOIN   runways   AS rwy ON (apt.ident = rwy.airport_ident)
               )
            SELECT name                       AS "_1"
            ,      GROUP_CONCAT(surface, ',') AS "_2"
            FROM   country_runways
            GROUP  BY name
             """
      .as[Query[(CountryLabel, String)]]
  }

  protected val mostCommonRunwayIdentificationsQry = quote {
    infix"""
            SELECT le_ident AS "_1"
            FROM   runways
            GROUP  BY le_ident
            ORDER  BY COUNT(*) DESC
            LIMIT  10

             """
      .as[Query[(String)]]
  }
}
