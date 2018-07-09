package net.mthomassen.airports.web.resources

import net.mthomassen.airports.data.{CountryCode, CountryLabel}

case class CountryCompletionResult(
    label: CountryLabel,
    code: CountryCode
)

object CountryCompletionResult {
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
  import spray.json.DefaultJsonProtocol

  trait CountryCompletionResultProtocol extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val countryCompletionResultJsonFormat = jsonFormat2(CountryCompletionResult.apply)
  }
}
