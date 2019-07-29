package controllers

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc._
import service.MatchCompanyData


class CompaniesController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  private val matchedCompanies = new MatchCompanyData().getMatchedCompanies


  def listPlaces = Action {
    Ok("Hey hey")
  }

  def show(companyName: String): Action[AnyContent] = Action {
    Ok("It's under construction")
  }

  //  def index: Action[AnyContent] = Action {
  //    Ok(matchedCompanies.take(10).map(x => x.asJson))
  //  }

  def index: Action[AnyContent] = Action {
    Ok(
      Json.obj(
        "id" -> "babba",
        "anna" -> "aaa"
      )
    )
  }
}