package utils

import exception.BusinessException
import models.ErrorResponse
import play.api.libs.json.Json
import play.api.mvc._

/**
  * Created by Satria Pribadi on 14/07/16.
  */
object Response extends Controller {

  def errorResponse(ex: BusinessException): Result = ex.code match {
    case NOT_FOUND => NotFound(Json.toJson(ErrorResponse(ex.message)))
    case BAD_REQUEST => BadRequest(Json.toJson(ErrorResponse(ex.message)))
  }
}
