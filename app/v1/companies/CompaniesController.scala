package v1.companies

import javax.inject.Inject

import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class PostFormInput(title: String, body: String)

/**
  * Takes HTTP requests and produces JSON.
  */
class CompaniesController @Inject()(implicit ec: ExecutionContext) {

  private val logger = Logger(getClass)

  private val form: Form[PostFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "title" -> nonEmptyText,
        "body" -> text
      )(PostFormInput.apply)(PostFormInput.unapply)
    )
  }


//
//  def show(company_name: String): Action[AnyContent] = { implicit request =>
//    logger.trace(s"get's data for Company = $company_name")
//    println("Test")
//  }

}
