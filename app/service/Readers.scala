package service

trait CompanyReader {
  def readCompanies(): Seq[Company]
}

trait TruthReader {
  def readCompaniesTruth(): Seq[CompanyTruth]
}

case class Company(id: Int, company_name: String, website_url: String, foundation_year: String, city: String, country: String)

case class CompanyTruth(id_profiles: Int, id_entities: Int, name: String)
