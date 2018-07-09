package net.mthomassen.airports.search

import net.mthomassen.airports.data.{CountryToken, CountryTokenType}
import org.scalatest.{Matchers, WordSpecLike}

class LuceneCountryCodeSearchSpec extends WordSpecLike with Matchers {

  "Country Code Search" should {

    val svc = new LuceneCountryCodeSearch(Seq(
      CountryToken(CountryTokenType.Name, "NL", "Netherlands"),
      CountryToken(CountryTokenType.Synonym, "NL", "Holland"),
      CountryToken(CountryTokenType.Code, "NL", "NL")
    ))

    "suggest completion country label" in {
      svc.lookup("Neth") shouldBe Seq(("Netherlands", "NL"))
    }

    "suggest completion country code" in {
      svc.lookup("NL") shouldBe Seq(("NL","NL"))
    }

    "suggest fuzzy completion misspelled country label" in {
      svc.lookup("Neterlands") shouldBe Seq(("Netherlands", "NL"))
    }

    "not suggest anyting for empty queries" in {
      svc.lookup("") shouldBe Nil
    }

  }

}
