package repositories.impl

import javax.inject.Singleton

import com.github.tototoshi.slick.MySQLJodaSupport._
import com.google.inject.Inject
import models.{OauthAccessToken, OauthClient, User}
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import repositories.{OauthAccessTokenRepository, OauthClientRepository}
import utils.Random

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Satria Pribadi on 12/07/16.
  */
@Singleton
class OauthAccessTokenRepositoryImpl @Inject()
(override protected val dbConfigProvider: DatabaseConfigProvider,
 oauthClientRepository: OauthClientRepository)
(implicit ec: ExecutionContext) extends OauthAccessTokenRepository {

  import dbConfig.driver.api._

  override def create(user: User, client: OauthClient): Future[OauthAccessToken] = {
    val o = OauthAccessToken(
      userId = user.id.get,
      oauthClientId = client.id.get,
      accessToken = Random.randomString(40),
      refreshToken = Random.randomString(40),
      createdAt = DateTime.now
    )
    insert(o).map(id => o.copy(id = Some(id)))
  }

  override def delete(user: User, client: OauthClient): Future[Int] = {
    deleteByFilter(o => o.userId === user.id && o.oauthClientId === client.id)
  }

  override def findByAuthorized(user: User, clientId: String): Future[Option[OauthAccessToken]] = {
    val query = for {
      oc <- oauthClientRepository.tableQ
      t <- tableQ if oc.id === t.oauthClientId && oc.clientId === clientId && t.userId === user.id
    } yield t
    db.run(query.result).map(_.headOption)
  }

  override def findByAccessToken(accessToken: String): Future[Option[OauthAccessToken]] = {
    findByFilter(_.accessToken === accessToken).map(_.headOption)
  }

  override def refresh(user: User, client: OauthClient): Future[OauthAccessToken] = {
    delete(user, client)
    create(user, client)
  }

  override def findByRefreshToken(refreshToken: String): Future[Option[OauthAccessToken]] = {
    findByFilter(t => t.refreshToken === refreshToken && t.createdAt >= DateTime.now().minusMonths(1)).map(_.headOption)
  }
}
