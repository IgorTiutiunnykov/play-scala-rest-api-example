package models

case class Company(id: Int, companyName: String, websiteUrl: String, foundationYear: String, city: String, country: String, normalizedName: String)

case class MatchedCompanies(idProfiles: Int, idEntities: Int, name: String)
