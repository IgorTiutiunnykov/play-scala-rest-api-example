package controllers

/**
  * Performs standard Java/unicode normalization on the trimmed and lowercased form
  * of the input String and then adds a few extra tricks for dealing with special
  * characters.
  *
  * JVM/Unicode normalization references (warning: learning curve black hole, beware!):
  *
  * - http://docs.oracle.com/javase/7/docs/api/java/text/Normalizer.html
  * - http://stackoverflow.com/questions/5697171/regex-what-is-incombiningdiacriticalmarks
  * - http://stackoverflow.com/questions/1453171/%C5%84-%C7%B9-%C5%88-%C3%B1-%E1%B9%85-%C5%86-%E1%B9%87-%E1%B9%8B-%E1%B9%89-%CC%88-%C9%B2-%C6%9E-%E1%B6%87-%C9%B3-%C8%B5-n-or-remove-diacritical-marks-from-unicode-cha
  * - http://lipn.univ-paris13.fr/~cerin/BD/unicode.html
  * - http://www.unicode.org/reports/tr15/tr15-23.html
  * - http://www.unicode.org/reports/tr44/#Properties
  *
  * Some special cases, like "ø" and "ß" are not being stripped/replaced by the
  * Java/Unicode normalizer so we have to replace them ourselves.
  */
trait NormalizeSupport {
  import java.text.Normalizer.{ normalize ⇒ jnormalize, _ }

  def normalize(in: String): String = {
    val cleaned = in.trim.toLowerCase
//    val normalized = jnormalize(cleaned, Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}\\p{IsM}\\p{IsLm}\\p{IsSk}]+", "")
    val normalized = jnormalize(cleaned, Form.NFD)

    normalized
      .replaceAll("'s", "")
      .replaceAll("ß", "ss")
      .replaceAll("ü", "ue")
      .replaceAll("ö", "oe")
      .replaceAll("ä", "ae")
      .replaceAll("ø", "oa")
      .replaceAll("ø", "oa")
      .replaceAll("http://", "")
      .replaceAll("[^a-zA-Z0-9-]+", "-")
      .replaceAll("-+", "-")
      .stripSuffix("-")
  }

  def web_url_normalize(in: String): String = {
    val cleaned = in.trim.toLowerCase
    val normalized = jnormalize(cleaned, Form.NFD)

    normalized
      .replaceAll("http://", "")
      .replaceAll("-", "")
  }
}

object NormalizeSupport extends NormalizeSupport


import java.util.regex.Pattern
import java.util.regex.Pattern.compile

object CompanyNameCleaner {
  //TODO: Check what can be removed from here whenever we use the Lucene token filter. We may reuse also tagindexer code
  val companyStopWords = Seq(
    "gmbh",
    "a.g.",
    "ag",
    "kg",
    "kgaa",
    "co",
    "sarl",
    "ab",
    "s.a.",
    "se",
    "sa",
    "ltd",
    "sl",
    "s.l.",
    "inc",
    "srl",
    "s.r.l.",
    "kft",
    "ggmbh",
    "gesmbh",
    "group",
    "m.b.h.",
    "mbh",
    "ges.m.b.H.",
    "sàrl",
    "aktiengesellschaft",
    "handelsgesellschaft"
  )

  lazy val afterStopWordRegex = compile(
    companyStopWords map Pattern.quote mkString ("""\b(""", "|", """)\b.*"""),
    Pattern.CASE_INSENSITIVE
  )

  lazy val yearRegex = compile("""\b201\d\b""")

  lazy val symbolsRegex = compile("""[.,\-\&\_\'\/\?\!\(\)\[\]\:\\;|\+\"\*\$%#<=@·•]+""")

  def cleaningTrailingStrategy(companyName: String): Option[String] = {
    // Remove everything after gmbh, e.g. "IBM GmbH, Deutschland" --> "IBM GmbH"
    val removedInfoText = afterStopWordRegex.matcher(companyName).replaceFirst("$1").trim()
    val cleanedInfoText = cleanCompanyName(removedInfoText)
    if (cleanedInfoText.length >= 3 && !cleanedInfoText.equals(companyName)) {
      Some(cleanedInfoText)
    } else {
      None
    }
  }

  final val ContiguousSpacesRegex = """\s+""".r

  private def cleanCompanyName(companyName: String): String = {
    val step1 = yearRegex.matcher(companyName).replaceAll("")
    val step2 = symbolsRegex.matcher(step1).replaceAll(" ")
    val step3 = ContiguousSpacesRegex.replaceAllIn(step2, " ")
    step3.trim()
  }
}