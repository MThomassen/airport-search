package net.mthomassen.airports.search

import net.mthomassen.airports.data._
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.search.suggest.analyzing.{AnalyzingInfixSuggester, FuzzySuggester}
import org.apache.lucene.store.RAMDirectory
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
  * Search/Suggestion Service
  *
  * @param tokens Country Tokes
  */
class LuceneCountryCodeSearch(tokens: Seq[CountryToken]) extends CountryCodeSearch {
  require(tokens.isTraversableAgain, "Tokens should be traversable mutliple times")

  private[this] val Log = LoggerFactory.getLogger(getClass)
  private[this] val MinPrefixCharacters = 1
  private[this] val MinFuzzyCharacters = 5
  private[this] val DefaultMaxResults = 5

  private[this] val analyzer = new StandardAnalyzer()
  private[this] val prefixSuggester = new AnalyzingInfixSuggester(new RAMDirectory, analyzer, analyzer, MinPrefixCharacters, true)
  private[this] val fuzzySuggester = new FuzzySuggester(new RAMDirectory, "fz", analyzer)

  Log.info(s"Building Search Indices ..")

  prefixSuggester.build(new LuceneCountryTokenIterator(tokens.iterator))
  fuzzySuggester.build(new LuceneCountryTokenIterator(tokens.iterator))

  Log.info(s"Search Indices build. Size:{}kb", (prefixSuggester.ramBytesUsed() + fuzzySuggester.ramBytesUsed()) / 1024)

  /**
    * Suggests matching labels
    *
    * @param query      query
    * @param maxResults max result set
    * @return Country Labels en ISO Code
    */
  def lookup(query: String, maxResults: Int = DefaultMaxResults): Seq[(CountryLabel, CountryCode)] = {
    (prefixLookup(query, maxResults) ++ fuzzyLookup(query))
      .sortBy { case (_, _, weight) => -weight }
      .map { case (label, code, _) => (label, code) }
      .distinct
      .take(maxResults)
  }

  private[search] def prefixLookup(query: String, maxResults: Int = DefaultMaxResults): Seq[(CountryLabel, CountryCode, Long)] = {
    if (query.length < MinPrefixCharacters) Nil
    else prefixSuggester
      .lookup(query, false, maxResults).asScala
      .map(lp => (lp.key.toString: CountryLabel, lp.payload.utf8ToString(): CountryCode, lp.value))
  }

  private[search] def fuzzyLookup(query: String, maxResults: Int = DefaultMaxResults): Seq[(CountryLabel, CountryCode, Long)] = {
    if (query.length < MinFuzzyCharacters) Nil
    else fuzzySuggester
      .lookup(query, false, maxResults).asScala
      .map(lp => (lp.key.toString: CountryLabel, lp.payload.utf8ToString(): CountryCode, lp.value))
  }
}
