package controllers

import javax.inject.Inject

import com.google.inject.Singleton
import exception.BusinessException
import models.{ErrorResponse, User}
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.Json
import play.api.mvc.{Controller, Result}
import security.OauthDataHandler
import services.UserService
import utils.Validation

import scala.concurrent.Future
import scalaoauth2.provider.OAuth2ProviderActionBuilders._

/**
  * Created by Satria Pribadi on 13/07/16.
  */
@Singleton
class UserController @Inject()
(userService: UserService,
 oauthDataHandler: OauthDataHandler) extends Controller {

  implicit val loggerUserController: Logger = LoggerFactory.getLogger(classOf[UserController])

  def getAllUser = AuthorizedAction(oauthDataHandler).async { implicit request =>
    userService.findAllUser().map(x => Ok(Json.toJson(x)))
  }

  def getUserById(id: Long) = AuthorizedAction(oauthDataHandler).async { implicit request =>
    userService.findById(id).map { x =>
      Ok(Json.toJson(x))
    } recover {
      case ex: BusinessException =>
        loggerUserController.debug(ex.message, ex)
        NotFound(Json.toJson(ErrorResponse(ex.message)))
    }
  }

  def createUser() = AuthorizedAction(oauthDataHandler).async(parse.json) { implicit request =>
    def create(): (User) => Future[Result] = u => {
      userService.createUser(u).map(x => Ok(Json.toJson(x))) recover {
        case ex: BusinessException =>
          loggerUserController.debug(ex.message, ex)
          BadRequest(Json.toJson(ErrorResponse(ex.message)))
      }
    }
    request.body.validate[User].fold(invalid = Validation.validationError(BAD_REQUEST), valid = create())
  }

  def updateUser() = AuthorizedAction(oauthDataHandler).async(parse.json) { implicit request =>
    def update(): (User) => Future[Result] = u => {
      userService.updateUser(u).map(x => Ok(Json.toJson(x))) recover {
        case ex: BusinessException =>
          loggerUserController.debug(ex.message, ex)
          NotFound(Json.toJson(ErrorResponse(ex.message)))
      }
    }
    request.body.validate[User].fold(invalid = Validation.validationError(BAD_REQUEST), valid = update())
  }

  def deleteUser(id: Long) = AuthorizedAction(oauthDataHandler).async {
    userService.deleteUser(id).map(x => Ok(Json.toJson(x))) recover {
      case ex: BusinessException =>
        loggerUserController.debug(ex.message, ex)
        NotFound(Json.toJson(ErrorResponse(ex.message)))
    }
  }

}
