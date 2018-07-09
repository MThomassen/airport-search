Airports
------

This application serves an HTTP endpoint for the following functionalities;

1. Query Completion: For completion or fuzzy matching country of country names (or synonyms)
2. Airport Resources: Serves data in [Hypertext Application Language](http://stateless.co/hal_specification.html) format*
3. Reporting: Serves predifined reports

_*: Note that currently, the Media type is actually `application/json`, this is the only Content-Type that can be negotiated on all service endpoints_

#### Query Completion

Example usage:
```bash
$ curl localhost:8080/api/v01/complete/search?q=bu | jq
```

Response:
```json
[
  {
    "label": "Burkina Faso",
    "code": "BF",
    "_links": {
      "country": {
        "href": "/country/BF"
      }
    }
  },
  {
    "label": "Bulgaria",
    "code": "BG",
    "_links": {
      "country": {
        "href": "/country/BG"
      }
    }
  },
  {
    "label": "Burundi",
    "code": "BI",
    "_links": {
      "country": {
        "href": "/country/BI"
      }
    }
  },
  {
    "label": "Burma",
    "code": "MM",
    "_links": {
      "country": {
        "href": "/country/MM"
      }
    }
  }
]
```

#### Airport Resources

The Web API serves 3 distinct resources;

* Countries: `/country/{countryCode}`
* Airports: `/airport/{ident}`
* Runways: `/airport/{ident}/runway/{id}`

Furthermore; the _Airport_ base uri supports filtering _Airports_ on _Country Code_: `/airport?country_code={countryCode}`

Example usages:
```bash
$ curl localhost:8080/api/v01/country/NL | jq
$ curl localhost:8080/api/v01/airport?country_code=NL | jq
$ curl localhost:8080/api/v01/airport/EHGR | jq
$ curl localhost:8080/api/v01/airport/EHGG/runway/14687 | jq
```

#### Reporting

The Web API serves 4 report types;
* CountriesHighestNumAirports
* CountriesLowestNumAirports
* CountriesRunwayTypes
* MostCommonRunwayIdentifiactions

The reports can be accessed on `/report?r={reportType}`

Currently, data is only provided in `application/json` encoding

Example usage:
```bash
$ curl localhost:8080/api/v01/report?r=MostCommonRunwayIdentifiactions | jq -c
```

Response
```json
{"name":"CountriesHighestNumAirports","headers":["country_name","num_airports"],"data":[["United States","21501"],["Brazil","3839"],["Canada","2454"],["Australia","1908"],["Russia","920"],["France","789"],["Argentina","713"],["Germany","703"],["Colombia","700"],["Venezuela","592"]]}
```

#### Starting the application

The application uses an embedded SQLite database. To load this database with data, a script is supplied that will initialize the database schema and import the data `var/data/load_db.sh`. _SQLite3_ should be available on the _PATH_

Currently, no packaging or deployment strategy is provided for the application. To build and run the application, use [SBT](https://www.scala-sbt.org/)

```bash
$ sbt "service/run"
```

The application defaults to hosting its HTTP endpoint on `localhost:8080`

Dependencies;
* JRE 8+
* SBT
* SQLite3 (_only for initial loading of database_)
* _Optional_: InfluxDB

#### Telemetry

Telemetry is logged to an InfluxDB instance. See `application.conf`

#### Note for Windows users

The default application configuration depends on the existence of symlinks to `$projectRoot/var` in the submodule. These links won't work on Windows systems; update configuration key `airports.db.jdbcUrl` accordingly  