package models

import com.github.tototoshi.slick.MySQLJodaSupport._
import org.joda.time.DateTime
import slick.driver.MySQLDriver.api._

/**
  * Created by Satria Pribadi on 12/07/16.
  */
case class User(
                 id: Option[Long] = None,
                 login: String,
                 password: String,
                 niceName: String,
                 email: String,
                 url: Option[String] = None,
                 registerDate: DateTime = DateTime.now,
                 activationKey: String,
                 status: Int = 0,
                 displayName: String,
                 createdAt: Option[DateTime] = Some(DateTime.now),
                 updatedAt: Option[DateTime] = Some(DateTime.now)
               ) extends BaseModel


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

  def * = (id.?, login, password, niceName, email, url.?, registerDate, activationKey, status, displayName, createdAt.?, updatedAt.?) <>(User.tupled, User.unapply)

  def loginIdx = index("user_login_key", login, unique = true)

  def niceNameIdx = index("user_nicename", niceName, unique = true)

  def emailIdx = index("user_email", email, unique = true)
}

object UserTable {
  implicit val user = TableQuery[UserTable]
}