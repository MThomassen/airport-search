package net.mthomassen.airports.data

import io.getquill.{SnakeCase, SqliteJdbcContext}
import net.mthomassen.airports.data.entities.{Airports, Countries, Runways}
import org.slf4j.LoggerFactory

import scala.concurrent._

object Database extends DatabaseQueries with DatabaseReportQueries
  with CountrySource
  with AirportSource
  with RunwaySource
  with ReportSource {
  private[this] val Log = LoggerFactory.getLogger(getClass)

  override val ctx: SqliteJdbcContext[SnakeCase] = new SqliteJdbcContext(SnakeCase, "airports.db")

  Log.info("Initialized DB")

  import ctx._

  override def countryByCode(code: CountryCode)(implicit ec: ExecutionContext): Future[Option[Countries]] = Future {
    blocking {
      ctx
        .run(countryByCodeQry(code))
        .headOption
    }
  }

  override def countryTokens: Seq[CountryToken] = ctx
    .run(countryTokensQry)
    .map{
      case (tpe, code, lbl) if CountryTokenType.withName(tpe) == CountryTokenType.Code && lbl.contains(", ")=>
        CountryToken(CountryTokenType.Synonym, code, lbl)
      case (tpe, code, lbl) =>
        CountryToken(CountryTokenType.withName(tpe), code, lbl)
    }

  override def airportByIdent(ident: AirportIdentity)(implicit ec: ExecutionContext): Future[Option[(Airports, Seq[Runways])]] = Future {
    blocking {
      normalize(ctx
        .run(airportByIdentQry(ident)))
        .headOption
    }
  }

  override def airportsByCountryCode(code: CountryCode)(implicit ec: ExecutionContext): Future[Seq[(Airports, Seq[Runways])]] = Future {
    blocking {
      normalize(ctx
        .run(airportByCountryCodeQry(code)))
    }
  }

  override def runwayById(id: RunwayIdentity)(implicit ec: ExecutionContext): Future[Option[Runways]] = Future{
    blocking {
      ctx
        .run(runwayByIdQry(id))
        .headOption
    }
  }

  def report(report: Report.Value): Tabular = {
    def tupleToDataRow(product: Product): Seq[String] = {
      product.productIterator.map(_.toString).toList
    }
    report match {
      case r @ Report.CountriesHighestNumAirports =>
        Tabular(
          reportName = r.toString,
          headers = Seq("country_name", "num_airports"),
          data = ctx.run(countryHighestNumAirportsQry).map(tupleToDataRow)
        )
      case r @ Report.CountriesLowestNumAirports =>
        Tabular(
          reportName = r.toString,
          headers = Seq("country_name", "num_airports"),
          data = ctx.run(countryLowestNumAirportsQry).map(tupleToDataRow)
        )
      case r @ Report.CountriesRunwayTypes =>
        Tabular(
          reportName = r.toString,
          headers = Seq("country_name", "runway_types"),
          data = ctx.run(countryRunwayTypesQry).map(tupleToDataRow)
        )
      case r @ Report.MostCommonRunwayIdentifiactions =>
        Tabular(
          reportName = r.toString,
          headers = Seq("le_ident"),
          data = ctx.run(mostCommonRunwayIdentificationsQry).map(Seq(_))
        )
    }
  }

  private[this] def normalize(qryResults: Iterable[(Airports, Option[Runways])]): Seq[(Airports, Seq[Runways])] = {
    qryResults
      .groupBy{ case (airports, _) =>  airports }
      .mapValues(_.flatMap(_._2).toSeq)
      .toSeq
  }
}
