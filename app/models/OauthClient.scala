package models

import com.github.tototoshi.slick.MySQLJodaSupport._
import org.joda.time.DateTime
import slick.driver.MySQLDriver.api._

/**
  * Created by Satria Pribadi on 16/06/16.
  */
case class OauthClient(
                        id: Option[Long] = None,
                        ownerId: Long,
                        grantType: String,
                        clientId: String,
                        clientSecret: String,
                        redirectUri: Option[String],
                        createdAt: DateTime
                      ) extends BaseModel

class OauthClientTable(tag: Tag) extends BaseTable[OauthClient](tag, "oauth_clients") {
  def userId = column[Long]("user_id")

  def grantType = column[String]("grant_type")

  def clientId = column[String]("client_id")

  def clientSecret = column[String]("client_secret")

  def redirectUri = column[Option[String]]("redirect_uri")

  def * = (id.?, userId, grantType, clientId, clientSecret, redirectUri, createdAt) <>(OauthClient.tupled, OauthClient.unapply)

  def owner = foreignKey("oauth_client_user_fk", userId, UserTable.user)(_.id)
}

object OauthClientTable {
  implicit val oauthClient: TableQuery[OauthClientTable] = TableQuery[OauthClientTable]
}