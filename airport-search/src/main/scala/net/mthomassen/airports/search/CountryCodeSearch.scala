package net.mthomassen.airports.search

import net.mthomassen.airports.data._
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.search.suggest.analyzing.{AnalyzingInfixSuggester, FuzzySuggester}
import org.apache.lucene.store.RAMDirectory
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
  * Search/Suggestion Service
  */
trait CountryCodeSearch {
  private[this] val DefaultMaxResults = 5
  /**
    * Suggests matching labels
    * @param query query
    * @param maxResults max result set
    * @return Country Labels en ISO Code
    */
  def lookup(query: String, maxResults: Int = DefaultMaxResults): Seq[(CountryLabel, CountryCode)]
}
