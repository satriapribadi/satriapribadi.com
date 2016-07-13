package models

import org.joda.time.DateTime
import play.api.libs.json.Json

/**
  * Created by Satria Pribadi on 30/06/16.
  */
case class ErrorResponse(errorMessage: String, createdAt: Option[DateTime] = Some(DateTime.now()))

object ErrorResponse extends utils.Json {
  implicit val jsonFormatErrorResponse = Json.format[ErrorResponse]
}