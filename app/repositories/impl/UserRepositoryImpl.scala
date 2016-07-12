package repositories.impl

import javax.inject.{Inject, Singleton}

import models.User
import play.api.db.slick.DatabaseConfigProvider
import repositories.UserRepository
import utils.Encrypt

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Satria Pribadi on 12/07/16.
  */
@Singleton
class UserRepositoryImpl @Inject()(override protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends UserRepository {

  import dbConfig.driver.api._

  override def findAccountByEmail(email: String): Future[Option[User]] = {
    findByFilter(_.email === email).map(_.headOption)
  }

  override def authenticate(email: String, password: String): Future[Option[User]] = {
    findByFilter(x => x.password === Encrypt.digestSHA(password) && x.email === email).map(_.headOption)
  }
}
