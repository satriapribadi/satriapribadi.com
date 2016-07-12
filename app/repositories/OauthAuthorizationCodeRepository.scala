package repositories

import models.{OauthAuthorizationCode, OauthAuthorizationCodeTable}
import repositories.impl.BaseRepositoryImpl

import scala.concurrent.Future

/**
  * Created by Satria Pribadi on 12/07/16.
  */
trait OauthAuthorizationCodeRepository extends BaseRepositoryImpl[OauthAuthorizationCodeTable, OauthAuthorizationCode] {
  def findByCode(code: String): Future[Option[OauthAuthorizationCode]]

  def delete(code: String): Future[Int]
}
