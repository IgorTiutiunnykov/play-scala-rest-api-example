package controllers

import play.api.mvc._
import service.MatchCompanyData


class CompaniesController extends BaseController{
private val matchedCompanies = new MatchCompanyData

  def index: Action[AnyContent] =TODO
  def show(company_name: String): Action[AnyContent] = TODO

}
