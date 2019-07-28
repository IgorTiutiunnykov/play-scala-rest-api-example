package service

import java.util.regex.Pattern
import java.util.regex.Pattern.compile

object Normalizer {

  def webUrl(in: String): String = {
    val cleaned = in.trim.toLowerCase.replaceAll("http://", "")
    cleaned.split("/").head
  }

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

  val afterStopWordRegex = compile(
    companyStopWords map Pattern.quote mkString("""\b(""", "|", """)\b.*"""),
    Pattern.CASE_INSENSITIVE
  )

  val yearRegex = compile("""\b201\d\b""")

  val symbolsRegex = compile("""[.,\-\&\_\'\/\?\!\(\)\[\]\:\\;|\+\"\*\$%#<=@·•]+""")

  def companyName(companyName: String): String = {
    // Remove everything after gmbh, e.g. "IBM GmbH, Deutschland" --> "IBM GmbH"
    val removedInfoText = afterStopWordRegex.matcher(companyName).replaceFirst("$1").trim()
    cleanCompanyName(removedInfoText)
  }

  val contiguousSpacesRegex = """\s+""".r

  private def cleanCompanyName(companyName: String): String = {
    val step1 = yearRegex.matcher(companyName).replaceAll("")
    val step2 = symbolsRegex.matcher(step1).replaceAll(" ")
    val step3 = contiguousSpacesRegex.replaceAllIn(step2, " ")
    step3.trim().toUpperCase
  }
}
