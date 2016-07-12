package services.impl

import javax.inject.{Inject, Singleton}

import models.User
import play.api.cache.CacheApi
import repositories.UserRepository
import services.UserService
import utils.Constants

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Satria Pribadi on 12/07/16.
  */
@Singleton
class UserServiceImpl @Inject()
(userRepository: UserRepository,
 cache: CacheApi)
(implicit ec: ExecutionContext) extends UserService {
  override def findById(id: Long): Future[Option[User]] = {
    cache.getOrElse[Future[Option[User]]](Constants.User.CACHE_PREFIX_ID(id), 5.minutes) {
      userRepository.findById(Some(id)).map { x =>
        if (x.isDefined) x else throw new Exception(Constants.User.USER_NOT_FOUND)
      }
    }
  }
}
