package repositories

import models.{User, UserTable}
import repositories.impl.BaseRepositoryImpl

import scala.concurrent.Future

/**
  * Created by Satria Pribadi on 12/07/16.
  */
trait UserRepository extends BaseRepositoryImpl[UserTable, User] {

  def findAccountByEmail(email: String): Future[Option[User]]

  def authenticate(email: String, password: String): Future[Option[User]]
}
