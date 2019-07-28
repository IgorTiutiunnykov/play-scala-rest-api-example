package service

trait CompanyReader {
  def readCompanies(): Vector[Company]
}

trait TruthReader {
  def readCompaniesTruth(): Vector[MatchedCompanies]
}

case class Company(id: Int, company_name: String, website_url: String, foundation_year: String, city: String, country: String)

case class MatchedCompanies(id_profiles: Int, id_entities: Int, name: String)
