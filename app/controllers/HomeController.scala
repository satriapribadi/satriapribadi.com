package controllers

import javax.inject._

import play.api.mvc._
import security.OauthDataHandler
import services.UserService

import scalaoauth2.provider.OAuth2ProviderActionBuilders._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()
(userService: UserService, oauthDataHandler: OauthDataHandler) extends Controller {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def getUserById(id: Long) = AuthorizedAction(oauthDataHandler).async { implicit request =>
    userService.findById(id).map { x =>
      Ok(views.html.index(x.get.displayName))
    }.recover {
      case ex: Exception => Ok(views.html.index(ex.getMessage))
    }
  }

}
