package repositories

import models.{OauthAccessToken, OauthAccessTokenTable, OauthClient, User}
import repositories.impl.BaseRepositoryImpl

import scala.concurrent.Future

/**
  * Created by Satria Pribadi on 12/07/16.
  */
trait OauthAccessTokenRepository extends BaseRepositoryImpl[OauthAccessTokenTable, OauthAccessToken] {
  def create(account: User, client: OauthClient): Future[OauthAccessToken]

  def delete(account: User, client: OauthClient): Future[Int]

  def refresh(account: User, client: OauthClient): Future[OauthAccessToken]

  def findByAccessToken(accessToken: String): Future[Option[OauthAccessToken]]

  def findByAuthorized(account: User, clientId: String): Future[Option[OauthAccessToken]]

  def findByRefreshToken(refreshToken: String): Future[Option[OauthAccessToken]]
}
