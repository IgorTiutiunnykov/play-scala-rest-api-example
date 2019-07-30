package controllers

import javax.inject.Inject
import models._
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc._
import service.MatchCompanyData


class CompaniesController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  private val matchedCompaniesData = new MatchCompanyData()

  implicit val companyWrites: Writes[MatchedCompanies] = new Writes[MatchedCompanies] {
    override def writes(company: MatchedCompanies): JsValue = Json.obj(
      "idProfiles" -> company.idProfiles,
      "idEntities" -> company.idEntities,
      "name" -> company.name
    )
  }

  def show(companyName: String, limit: Int): Action[AnyContent] = Action {
    val json = Json.toJson(matchedCompaniesData.getMatches(companyName).take(limit))
    Ok(json)
  }

  def index(limit: Int): Action[AnyContent] = Action {
    val json = Json.toJson(matchedCompaniesData.getMatchedCompanies.take(limit))
    Ok(json)
  }
}
