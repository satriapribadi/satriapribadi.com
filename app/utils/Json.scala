package utils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.Reads._
import play.api.libs.json.{JsString, Reads, Writes}

/**
  * Created by Satria Pribadi on 13/07/16.
  */
trait Json {
  implicit val readsDateTime = Reads[DateTime](js =>
    js.validate[String].map[DateTime](dtString =>
      DateTime.parse(dtString, DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss"))))

  implicit val writesDateTime = Writes[DateTime](dt =>
    JsString(DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss").print(dt)))

  def url(implicit reads: Reads[String]): Reads[String] =
    pattern("""^(http:\/\/|https:\/\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$""".r, "error.url")
}
