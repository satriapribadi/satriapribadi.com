package repositories

import models.{OauthClient, OauthClientTable, User}
import repositories.impl.BaseRepositoryImpl

import scala.concurrent.Future

/**
  * Created by Satria Pribadi on 12/07/16.
  */
trait OauthClientRepository extends BaseRepositoryImpl[OauthClientTable, OauthClient] {
  def validate(clientId: String, clientSecret: String, grantType: String): Future[Boolean]

  def findByClientId(clientId: String): Future[Option[OauthClient]]

  def findClientCredentials(clientId: String, clientSecret: String): Future[Option[User]]
}
