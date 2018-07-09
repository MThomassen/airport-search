PRAGMA foreign_keys = ON;

CREATE TABLE countries (
  id             INTEGER   PRIMARY KEY AUTOINCREMENT
, code           TEXT      NOT NULL UNIQUE
, name           TEXT      NOT NULL
, continent      TEXT      NOT NULL
, wikipedia_link TEXT      NOT NULL
, keywords       TEXT      NULL
, created        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE airports (
  id                 INTEGER   PRIMARY KEY AUTOINCREMENT
, ident              TEXT      NOT NULL UNIQUE
, type               TEXT      NOT NULL
, name               TEXT      NOT NULL
, latitude_deg       NUMERIC   NOT NULL
, longitude_deg      NUMERIC   NOT NULL
, elevation_ft       INTEGER   NULL
, continent          TEXT      NOT NULL
, iso_country        TEXT      NOT NULL
, iso_region         TEXT      NOT NULL
, municipality       TEXT      NULL
, scheduled_service  TEXT      NOT NULL
, gps_code           TEXT      NULL
, iata_code          TEXT      NULL
, local_code         TEXT      NULL
, home_link          TEXT      NULL
, wikipedia_link     TEXT      NULL
, keywords           TEXT      NULL
, created            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
, FOREIGN KEY(iso_country) REFERENCES countries(code)
);

CREATE TABLE runways (
  id                        INTEGER   PRIMARY KEY AUTOINCREMENT
, airport_ref               INTEGER   NOT NULL
, airport_ident             TEXT      NOT NULL
, length_ft                 INTEGER   NULL
, width_ft                  INTEGER   NULL
, surface                   TEXT      NULL
, lighted                   INTEGER   NOT NULL
, closed                    INTEGER   NOT NULL
, le_ident                  TEXT      NULL
, le_latitude_deg           NUMERIC   NULL
, le_longitude_deg          NUMERIC   NULL
, le_elevation_ft           INTEGER   NULL
, le_heading_degT           NUMERIC   NULL
, le_displaced_threshold_ft INTEGER   NULL
, he_ident                  TEXT      NULL
, he_latitude_deg           NUMERIC   NULL
, he_longitude_deg          NUMERIC   NULL
, he_elevation_ft           INTEGER   NULL
, he_heading_degT           NUMERIC   NULL
, he_displaced_threshold_ft INTEGER   NULL
, created                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
, FOREIGN KEY(airport_ident) REFERENCES airports(ident)
);