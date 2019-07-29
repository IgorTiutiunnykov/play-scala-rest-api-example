package models

import play.api.libs.json.{JsValue, Json}

trait CompanyReader {
  def readCompanies(): Vector[Company]
}

trait TruthReader {
  def readCompaniesTruth(): Vector[MatchedCompanies]
}

case class Company(id: Int, companyName: String, websiteUrl: String, foundationYear: String, city: String, country: String, normalizedName: String)

case class MatchedCompanies(idProfiles: Int, idEntities: Int, name: String) {
  def asJson: JsValue = {
    Json.obj(
      "idProfiles" -> idProfiles,
      "idEntities" -> idEntities,
      "name" -> name
    )
  }
}