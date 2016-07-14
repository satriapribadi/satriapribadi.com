package models

import com.github.tototoshi.slick.MySQLJodaSupport._
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import slick.driver.MySQLDriver.api._
import utils.Random

/**
  * Created by Satria Pribadi on 12/07/16.
  */
case class User(
                 id: Option[Long] = None,
                 login: String,
                 password: String,
                 niceName: Option[String] = None,
                 email: String,
                 url: Option[String] = None,
                 registerDate: Option[DateTime] = Some(DateTime.now),
                 activationKey: Option[String] = Some(Random.randomString(60)),
                 status: Option[Int] = Some(0),
                 displayName: String,
                 createdAt: Option[DateTime] = Some(DateTime.now),
                 updatedAt: Option[DateTime] = Some(DateTime.now)
               ) extends BaseModel

object User extends utils.Json {
  implicit val userReads: Reads[User] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "login").read[String]
        (minLength[String](6) keepAnd maxLength[String](60)) and
      (JsPath \ "password").read[String]
        (minLength[String](8) keepAnd maxLength[String](64)) and
      (JsPath \ "niceName").readNullable[String]
        (minLength[String](6) keepAnd maxLength[String](50)) and
      (JsPath \ "email").read[String]
        (email keepAnd minLength[String](6) keepAnd maxLength[String](60)) and
      (JsPath \ "url").readNullable[String]
        (pattern("""^(http:\/\/|https:\/\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$""".r, "error.url")
          keepAnd minLength[String](6)) and
      (JsPath \ "registerDate").readNullable[DateTime]
        (readsDateTime) and
      (JsPath \ "activationKey").readNullable[String]
        (minLength[String](60) keepAnd maxLength[String](60)) and
      (JsPath \ "status").readNullable[Int] and
      (JsPath \ "displayName").read[String]
        (minLength[String](6) keepAnd maxLength[String](255)) and
      (JsPath \ "createdAt").readNullable[DateTime]
        (readsDateTime) and
      (JsPath \ "updatedAt").readNullable[DateTime]
        (readsDateTime)
    ) (User.apply _)
  implicit val userWrite = Json.writes[User]
}

class UserTable(tag: Tag) extends BaseTable[User](tag, "users") {
  def login = column[String]("user_login")

  def password = column[String]("user_pass")

  def niceName = column[String]("user_nicename")

  def email = column[String]("user_email")

  def url = column[String]("user_url")

  def registerDate = column[DateTime]("user_registered")

  def activationKey = column[String]("user_activation_key")

  def status = column[Int]("user_status")

  def displayName = column[String]("display_name")

  def * = (id.?, login, password, niceName.?, email, url.?, registerDate.?, activationKey.?, status.?, displayName, createdAt.?, updatedAt.?) <>((User.apply _).tupled, User.unapply)

  def loginIdx = index("user_login_key", login, unique = true)

  def niceNameIdx = index("user_nicename", niceName, unique = true)

  def emailIdx = index("user_email", email, unique = true)
}

object UserTable {
  implicit val user = TableQuery[UserTable]
}