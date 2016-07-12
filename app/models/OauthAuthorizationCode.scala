package models

import com.github.tototoshi.slick.MySQLJodaSupport._
import org.joda.time.DateTime
import slick.driver.MySQLDriver.api._

/**
  * Created by Satria Pribadi on 16/06/16.
  */
case class OauthAuthorizationCode(
                                   id: Option[Long] = None,
                                   accountId: Long,
                                   oauthClientId: Long,
                                   code: String,
                                   redirectUri: Option[String],
                                   createdAt: DateTime
                                 ) extends BaseModel

class OauthAuthorizationCodeTable(tag: Tag) extends BaseTable[OauthAuthorizationCode](tag, "oauth_authorization_codes") {
  def userId = column[Long]("user_id")

  def oauthClientId = column[Long]("oauth_client_id")

  def code = column[String]("code")

  def redirectUri = column[Option[String]]("redirect_uri")

  def * = (id.?, userId, oauthClientId, code, redirectUri, createdAt) <>(OauthAuthorizationCode.tupled, OauthAuthorizationCode.unapply)

  def user = foreignKey("oauth_authorization_code_user_fk", userId, UserTable.user)(_.id)

  def oauthClient = foreignKey("oauth_authorization_code_client_fk", oauthClientId, OauthClientTable.oauthClient)(_.id)
}

object OauthAuthorizationCodeTable {
  implicit val oauthAuthorizationCode: TableQuery[OauthAuthorizationCodeTable] = TableQuery[OauthAuthorizationCodeTable]
}