package utils

import play.api.Play.current
import play.api.data.validation.ValidationError
import play.api.i18n.Messages
import play.api.i18n.Messages.Implicits._
import play.api.libs.json.Json._
import play.api.libs.json.{JsObject, JsPath, Json}
import play.api.mvc._

import scala.concurrent.Future

/**
  * Created by Satria Pribadi on 13/07/16.
  */
object Validation extends Controller {

  def validationError(code: Int)(errors: Seq[(JsPath, Seq[ValidationError])]): Future[Result] = {
    def messageFormat: JsObject = {
      errors.foldLeft(Json.obj()) { (obj, error) =>
        obj ++ Json.obj(error._1.toString().replaceFirst(Constants.Global.SLASH, Constants.Global.EMPTY_STRING) -> error._2.foldLeft(Json.arr()) { (arr, err) =>
          arr :+ Json.obj(Constants.Global.MESSAGE -> Messages(err.message, if (err.args.nonEmpty) err.args.head).toString)
        })
      }
    }
    Future.successful(BadRequest(obj(Constants.Global.STATUS -> code, Constants.Global.ERROR_MESSAGE -> messageFormat)))
  }
}
