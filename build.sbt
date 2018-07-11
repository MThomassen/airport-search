lazy val akkaHttpVersion = "10.1.3"
lazy val akkaVersion     = "2.5.13"
lazy val luceneVersion   = "7.4.0"

lazy val commonSettings = Seq(
  organization := "net.mthomassen",
  scalaVersion := "2.12.6",
  version      := "1.0.0-SNAPSHOT",
  libraryDependencies ++= Seq(
    "org.slf4j"     %  "slf4j-api" % "1.7.25",
    "com.typesafe"  %  "config"    % "1.3.3",
    "org.scalatest" %% "scalatest" % "3.0.5"  % Test,
    "org.scalamock" %% "scalamock" % "4.1.0"  % Test
  )
)

lazy val data_sql = (project in file("airport-data-sql"))
  .settings(commonSettings:_*)
  .settings(
    name := "Airport Data SQL",
    libraryDependencies ++= Seq(
      "org.xerial"  %  "sqlite-jdbc" % "3.23.1",
      "io.getquill" %% "quill-jdbc"  % "2.5.4"
    )
  )

lazy val search = (project in file("airport-search"))
  .settings(commonSettings:_*)
  .settings(
    name := "Airport Search",
    libraryDependencies ++= Seq(
      "org.apache.lucene" % "lucene-suggest" % luceneVersion
    )
  )
  .dependsOn(data_sql)

lazy val service = (project in file("airport-service"))
  .settings(commonSettings:_*)
  .settings(
    name := "Airport Service",
    fork in run := true,
    mainClass in run := Some("net.mthomassen.airports.ApplicationMain"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-caching"    % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,

      "ch.qos.logback"    %  "logback-classic"      % "1.2.3",

      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,

      "io.kamon"          %% "kamon-akka-http-2.5"  % "1.0.1",
      "io.kamon"          %% "kamon-influxdb"       % "1.0.1"
    )
  )
  .dependsOn(search)

lazy val airports = (project in file("."))
  .aggregate(data_sql, search, service)
