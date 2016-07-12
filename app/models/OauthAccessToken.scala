package models

import com.github.tototoshi.slick.MySQLJodaSupport._
import org.joda.time.DateTime
import slick.driver.MySQLDriver.api._


/**
  * Created by Satria Pribadi on 16/06/16.
  */
case class OauthAccessToken(
                             id: Option[Long] = None,
                             userId: Long,
                             oauthClientId: Long,
                             accessToken: String,
                             refreshToken: String,
                             createdAt: DateTime
                           ) extends BaseModel

class OauthAccessTokenTable(tag: Tag) extends BaseTable[OauthAccessToken](tag, "oauth_access_tokens") {
  def userId = column[Long]("user_id")

  def oauthClientId = column[Long]("oauth_client_id")

  def accessToken = column[String]("access_token")

  def refreshToken = column[String]("refresh_token")

  def * = (id.?, userId, oauthClientId, accessToken, refreshToken, createdAt) <>(OauthAccessToken.tupled, OauthAccessToken.unapply)

  def account = foreignKey("oauth_access_token_user_fk", userId, UserTable.user)(_.id)

  def oauthClient = foreignKey("oauth_access_token_client_fk", oauthClientId, OauthAuthorizationCodeTable.oauthAuthorizationCode)(_.id)

}

object OauthAccessTokenTable {
  implicit val oauthAccessToken: TableQuery[OauthAccessTokenTable] = TableQuery[OauthAccessTokenTable]
}