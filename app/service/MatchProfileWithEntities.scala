package service

import models.MatchedCompanies

/* this just for testing purpose and see how matching works.
and it's not taken into account for API
 */

object MatchProfileWithEntities {
  def main(args: Array[String]): Unit = {

//    def similarText(text1: String, text2: String): Boolean = {
//      val maxLength = Math.max(text1.length, text2.length).toFloat
//      val levenshteinDist = StringUtils.getLevenshteinDistance(text1, text2).toFloat
//      (1.0 - levenshteinDist / maxLength >= 0.95)
//    }

    println("Prepare to read")

    val compEntities = new CompanyTSVReader("company_entities.tsv").readCompanies()
    val compProfiles = new CompanyTSVReader("company_profiles.tsv").readCompanies()
    val compTruth = new TruthTSVReader("ground_truth.tsv").readCompaniesTruth()

    //use the map method on the collection to convert it into a collection of tuples and then use the : _* trick to convert the result into a variable argument
    val compProfilesMap = Map(compProfiles map {s => (s.normalizedName, s)} : _*)
    val compEntitiesMap = Map(compEntities map {s => (s.normalizedName, s)} : _*)

    val list = compEntities.map(a => a.normalizedName)

    println("Reading is done")

    def matchComp(): Seq[MatchedCompanies] = for {
      compEntity <- compEntities
      compProfile <- compProfiles
      if (compEntity.normalizedName == compProfile.normalizedName
        || (compEntity.websiteUrl == compProfile.websiteUrl && compEntity.country == compProfile.country && !compEntity.websiteUrl.isEmpty))}
      yield MatchedCompanies(compProfile.id, compEntity.id, compEntity.companyName)

//    def matchComp(): Seq[MatchedCompanies] = for {
//      compEntity <- compEntities
//      compProfile <- compProfiles
//      if (similarText(compEntity.normalizedName, compProfile.normalizedName)
//        || (compEntity.websiteUrl == compProfile.websiteUrl && compEntity.country == compProfile.country && !compEntity.websiteUrl.isEmpty))}
//      yield MatchedCompanies(compProfile.id, compEntity.id, compEntity.companyName)

//    def matchComp(): Seq[MatchedCompanies] = for {
//      compEntity <- compEntities
//      (k, v) <- compProfilesMap
//      if (compEntity.normalizedName == k
//        || (compEntity.websiteUrl == v.websiteUrl && compEntity.country == v.country && !compEntity.websiteUrl.isEmpty))}
//      yield MatchedCompanies(v.id, compEntity.id, compEntity.companyName)

    val matchedComps = matchComp

    def getMatchedCompanies(amount: Int) = matchedComps.take(amount)

    def similar(e: MatchedCompanies, f: MatchedCompanies) = {
      e.idProfiles == f.idProfiles && e.idEntities == f.idEntities
    }

    val test = compTruth.filter(aa => {
      !matchedComps.exists { bb => similar(aa, bb) }
    })

    val false_pos = matchedComps.filter(aa => {
      !compTruth.exists { bb => similar(aa, bb) }
    })

    def matchByName(e: MatchedCompanies, name: String) = {
      e.name == name
    }

    def getMatches(name: String) = matchedComps.filter { aa => matchByName(aa, name) }

    val testMatch = getMatches("KiwiSecurity Software GmbH")

    println("Done")
  }

}



