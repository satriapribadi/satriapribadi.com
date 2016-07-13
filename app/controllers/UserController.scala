package controllers

import javax.inject.Inject

import com.google.inject.Singleton
import exception.BusinessException
import models.ErrorResponse
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.Json
import play.api.mvc.Controller
import security.OauthDataHandler
import services.UserService

import scalaoauth2.provider.OAuth2ProviderActionBuilders._

/**
  * Created by Satria Pribadi on 13/07/16.
  */
@Singleton
class UserController @Inject()
(userService: UserService,
 oauthDataHandler: OauthDataHandler) extends Controller {

  implicit val loggerUserController: Logger = LoggerFactory.getLogger(classOf[UserController])

  def getUserById(id: Long) = AuthorizedAction(oauthDataHandler).async { implicit request =>
    userService.findById(id).map { x =>
      Ok(Json.toJson(x))
    } recover {
      case ex: BusinessException =>
        loggerUserController.debug(ex.message, ex)
        NotFound(Json.toJson(ErrorResponse(ex.message)))
    }
  }

}
