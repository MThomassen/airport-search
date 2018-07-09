package net.mthomassen.airports.web.resources

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import net.mthomassen.airports.data.Tabular
import spray.json.DefaultJsonProtocol

trait TabularProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val tabularJsonFormat = jsonFormat3(Tabular)
}
