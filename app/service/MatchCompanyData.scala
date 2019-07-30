package service

import models.MatchedCompanies

class MatchCompanyData(val amount: Int = 10) {
  //        || (compEntity.company_name.contains(compProfile.company_name) && compEntity.country == compProfile.country)

  val compEntities = new CompanyTSVReader("company_entities.tsv").readCompanies()
  val compProfiles = new CompanyTSVReader("company_profiles.tsv").readCompanies()
  val compTruth = new TruthTSVReader("ground_truth.tsv").readCompaniesTruth()

  def matchComp(): Vector[MatchedCompanies] = for {
    compEntity <- compEntities
    compProfile <- compProfiles
    if (compEntity.normalizedName == compProfile.normalizedName
      || (compEntity.websiteUrl == compProfile.websiteUrl && compEntity.country == compProfile.country && !compEntity.websiteUrl.isEmpty))
  }
    yield MatchedCompanies(compProfile.id, compEntity.id, compEntity.companyName)

  private val matchedComps = matchComp

  def getMatchedCompanies: Vector[MatchedCompanies] = matchedComps

  def similar(companiesA: MatchedCompanies, companiesB: MatchedCompanies): Boolean = {
    companiesA.idProfiles == companiesB.idProfiles && companiesA.idEntities == companiesB.idEntities
  }

  def matchByName(company: MatchedCompanies, name: String): Boolean = {
    company.name.contains(name)
  }

  def getMatches(name: String): Vector[MatchedCompanies] = matchedComps.filter { company => matchByName(company, name) }
}
