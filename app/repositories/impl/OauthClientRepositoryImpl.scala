package repositories.impl

import javax.inject.Singleton

import com.google.inject.Inject
import models.{OauthClient, User}
import play.api.db.slick.DatabaseConfigProvider
import repositories.{OauthClientRepository, UserRepository}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Satria Pribadi on 12/07/16.
  */
@Singleton
class OauthClientRepositoryImpl @Inject()
(override protected val dbConfigProvider: DatabaseConfigProvider,
 userRepository: UserRepository)
(implicit ec: ExecutionContext) extends OauthClientRepository {

  import dbConfig.driver.api._

  override def validate(clientId: String, clientSecret: String, grantType: String): Future[Boolean] = {
    findByFilter(o => o.clientId === clientId && o.clientSecret === clientSecret).map(_.headOption.fold(false)(x => x.grantType == grantType || grantType == "refresh_token"))
  }

  override def findClientCredentials(clientId: String, clientSecret: String): Future[Option[User]] = {
    for {
      o <- findByFilter(x => x.clientId === clientId && x.clientSecret === clientSecret).map(_.headOption.map(_.ownerId))
      a <- userRepository.findById(o)
    } yield a
  }

  override def findByClientId(clientId: String): Future[Option[OauthClient]] = {
    findByFilter(o => o.clientId === clientId).map(_.headOption)
  }
}
