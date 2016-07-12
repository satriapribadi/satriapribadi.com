package repositories.impl

import javax.inject.Singleton

import com.github.tototoshi.slick.MySQLJodaSupport._
import com.google.inject.Inject
import models.OauthAuthorizationCode
import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import repositories.{OauthAuthorizationCodeRepository, UserRepository}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Satria Pribadi on 12/07/16.
  */
@Singleton
class OauthAuthorizationCodeRepositoryImpl @Inject()
(override protected val dbConfigProvider: DatabaseConfigProvider,
 userRepository: UserRepository)
(implicit ec: ExecutionContext) extends OauthAuthorizationCodeRepository {

  import dbConfig.driver.api._

  override def findByCode(code: String): Future[Option[OauthAuthorizationCode]] = {
    findByFilter(o => o.code === code && o.createdAt > DateTime.now.minusMinutes(30)).map(_.headOption)
  }

  override def delete(code: String): Future[Int] = deleteByFilter(_.code === code)
}
