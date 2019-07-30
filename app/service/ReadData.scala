package service

import models.MatchedCompanies

object ReadData {
  def main(args: Array[String]): Unit = {

    println("Prepare to read")

    //        || (compEntity.company_name.contains(compProfile.company_name) && compEntity.country == compProfile.country)

    val compEntities = new CompanyTSVReader("company_entities.tsv").readCompanies()
    val compProfiles = new CompanyTSVReader("company_profiles.tsv").readCompanies()
    val compTruth = new TruthTSVReader("ground_truth.tsv").readCompaniesTruth()

    def matchComp(): Seq[MatchedCompanies] = for {
      compEntity <- compEntities
      compProfile <- compProfiles
      if (compEntity.normalizedName == compProfile.normalizedName
        || (compEntity.websiteUrl == compProfile.websiteUrl && compEntity.country == compProfile.country && !compEntity.websiteUrl.isEmpty))}
      yield MatchedCompanies(compProfile.id, compEntity.id, compEntity.companyName)

    println("Done1")

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

    def getMatches(name: String) = matchedComps.filter{aa => matchByName(aa, name)}

    val testMatch = getMatches("KiwiSecurity Software GmbH")

    println("Done")
  }

}
