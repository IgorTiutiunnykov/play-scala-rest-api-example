package controllers

import javax.inject.Inject
import models._
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc._
import service.MatchCompanyData


class CompaniesController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  private val matchedCompanies = new MatchCompanyData().getMatchedCompanies

  implicit val companyWrites: Writes[MatchedCompanies] = new Writes[MatchedCompanies] {
    override def writes(company: MatchedCompanies): JsValue = Json.obj(
      "idProfiles" -> company.idProfiles,
      "idEntities" -> company.idEntities,
      "name" -> company.name
    )
  }

  def show(companyName: String): Action[AnyContent] = Action {
    Ok("It's under construction")
  }

  def index: Action[AnyContent] = Action {
    val json = Json.toJson(matchedCompanies.take(10))
    Ok(json)
  }
}
