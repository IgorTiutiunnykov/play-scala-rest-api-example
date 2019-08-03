package service

import models.MatchedCompanies

class MatchCompanyData(val amount: Int = 10) {
  private val compEntities = new CompanyTSVReader("company_entities.tsv").readCompanies()
  lazy private val compProfiles = new CompanyTSVReader("company_profiles.tsv").readCompanies()

  def getMatchedCompanies: Vector[MatchedCompanies] = matchedComps

  def matchCompany(id: Int, companyName: String, websiteUrl: String, foundationYear: String, city: String, country: String): Vector[MatchedCompanies] = for {
    compEntity <- compEntities
    if (compEntity.normalizedName == Normalizer.companyName(companyName)
      || (!compEntity.websiteUrl.isEmpty && compEntity.websiteUrl == Normalizer.webUrl(websiteUrl) && compEntity.country == country))
  }
    yield MatchedCompanies(id, compEntity.id, compEntity.companyName)

  lazy private val matchedComps: Vector[MatchedCompanies] = for {
    compEntity <- compEntities
    compProfile <- compProfiles
    if (compEntity.normalizedName == compProfile.normalizedName
      || (compEntity.websiteUrl == compProfile.websiteUrl && compEntity.country == compProfile.country && !compEntity.websiteUrl.isEmpty))
  }
    yield MatchedCompanies(compProfile.id, compEntity.id, compEntity.companyName)
}
