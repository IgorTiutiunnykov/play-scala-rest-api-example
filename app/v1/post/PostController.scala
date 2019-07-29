package v1.post

import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class PostFormInput(title: String, body: String)

/**
  * Takes HTTP requests and produces JSON.
  */
class PostController @Inject()(cc: PostControllerComponents)(implicit ec: ExecutionContext)
    extends PostBaseController(cc) {


  def index: Action[AnyContent] = PostAction.async { implicit request =>
    postResourceHandler.find.map { posts =>
      Ok(Json.toJson(posts))
    }
  }

  def show(id: String): Action[AnyContent] = PostAction.async { implicit request =>
    postResourceHandler.lookup(id).map { post =>
      Ok(Json.toJson(post))
    }
  }
}
