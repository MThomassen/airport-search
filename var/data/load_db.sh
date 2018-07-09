#!/bin/bash

sqlite3 `dirname $0`/airports.db < `dirname $0`/../../airport-data-sql/src/main/sql/init_db.sql

sqlite3 `dirname $0`/airports.db <<EOF
.mode csv
.separator ","

DROP TABLE IF EXISTS _countries;
DROP TABLE IF EXISTS _airports;
DROP TABLE IF EXISTS _runways;

.import `dirname $0`/countries.csv _countries
.import `dirname $0`/airports.csv _airports
.import `dirname $0`/runways.csv _runways

PRAGMA foreign_keys = OFF
;
INSERT INTO countries(code, name, continent, wikipedia_link, keywords)
SELECT code
,      name
,      continent
,      wikipedia_link
,      NULLIF(keywords, '')
FROM   _countries
;
DROP TABLE _countries
;
INSERT INTO airports(ident,type,name,latitude_deg,longitude_deg,elevation_ft,continent,iso_country,iso_region,municipality,scheduled_service,gps_code,iata_code,local_code,home_link,wikipedia_link,keywords)
SELECT ident
,      type,name
,      latitude_deg
,      longitude_deg
,      NULLIF(elevation_ft, '')
,      continent
,      iso_country
,      iso_region
,      NULLIF(municipality, '')
,      scheduled_service
,      NULLIF(gps_code, '')
,      NULLIF(iata_code, '')
,      NULLIF(local_code, '')
,      NULLIF(home_link, '')
,      NULLIF(wikipedia_link, '')
,      NULLIF(keywords, '')
FROM   _airports
;
DROP TABLE _airports
;
INSERT INTO runways(airport_ref,airport_ident,length_ft,width_ft,surface,lighted,closed,le_ident,le_latitude_deg,le_longitude_deg,le_elevation_ft,le_heading_degT,le_displaced_threshold_ft,he_ident,he_latitude_deg,he_longitude_deg,he_elevation_ft,he_heading_degT,he_displaced_threshold_ft)
SELECT airport_ref
,      airport_ident
,      NULLIF(length_ft, '')
,      NULLIF(width_ft, '')
,      NULLIF(surface, '')
,      lighted
,      closed
,      NULLIF(le_ident, '')
,      NULLIF(le_latitude_deg, '')
,      NULLIF(le_longitude_deg, '')
,      NULLIF(le_elevation_ft, '')
,      NULLIF(le_heading_degT, '')
,      NULLIF(le_displaced_threshold_ft, '')
,      NULLIF(he_ident, '')
,      NULLIF(he_latitude_deg, '')
,      NULLIF(he_longitude_deg, '')
,      NULLIF(he_elevation_ft, '')
,      NULLIF(he_heading_degT, '')
,      NULLIF(he_displaced_threshold_ft, '')
FROM   _runways
;
DROP TABLE _runways
;

PRAGMA foreign_keys = ON
;

ANALYZE countries
;
ANALYZE airports
;
ANALYZE runways
;

VACUUM
;
EOF
