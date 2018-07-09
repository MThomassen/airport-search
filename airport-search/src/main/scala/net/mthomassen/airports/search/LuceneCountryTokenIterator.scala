package net.mthomassen.airports.search

import java.util

import net.mthomassen.airports.data.{CountryCode, CountryToken, CountryTokenType}
import org.apache.lucene.search.suggest.InputIterator
import org.apache.lucene.util.BytesRef

import scala.collection.JavaConverters._

private[search] class LuceneCountryTokenIterator(tokens: Iterator[CountryToken]) extends InputIterator {
  private[this] var currCountryCode: CountryCode = _
  private[this] var currWeight: Long = _

  override def next(): BytesRef = {
    if (!tokens.hasNext) null
    else {
      tokens.next() match {
        case CountryToken(CountryTokenType.Code, code, label) =>
          currCountryCode = code
          currWeight = 2
          new BytesRef(label)
        case CountryToken(_, code, label) =>
          currCountryCode = code
          currWeight = 1
          new BytesRef(label)
      }
    }
  }

  override def weight(): Long = currWeight

  override def hasPayloads: Boolean = true

  override def contexts(): util.Set[BytesRef] = Set.empty[BytesRef].asJava

  override def payload(): BytesRef = new BytesRef(currCountryCode)

  override def hasContexts: Boolean = false
}
