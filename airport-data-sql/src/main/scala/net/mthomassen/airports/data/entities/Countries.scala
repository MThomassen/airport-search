package net.mthomassen.airports.data.entities

case class Countries(
    id: Int,
    code: String,
    name: String,
    continent: String,
    wikipediaLink: String,
    keywords: Option[String] = None,
    created: String
)
