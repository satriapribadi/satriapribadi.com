package services.impl

import javax.inject.{Inject, Singleton}

import exception.BusinessException
import models.User
import org.joda.time.DateTime
import play.api.cache.CacheApi
import repositories.UserRepository
import services.UserService
import utils.{Constants, Encrypt, Validation}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Satria Pribadi on 12/07/16.
  */
@Singleton
class UserServiceImpl @Inject()
(userRepository: UserRepository, cache: CacheApi)
(implicit ec: ExecutionContext) extends UserService {

  override def findAllUser(): Future[Seq[User]] = {
    cache.getOrElse[Future[Seq[User]]](Constants.User.CACHE_PREFIX_ALL, 5.minutes) {
      userRepository.findAll
    }
  }

  override def findById(id: Long): Future[Option[User]] = {
    cache.getOrElse[Future[Option[User]]](Constants.User.CACHE_PREFIX_ID(id), 5.minutes) {
      userRepository.findById(Some(id)).map { x =>
        if (x.isDefined) x else throw BusinessException(Validation.NOT_FOUND, Constants.User.USER_NOT_FOUND)
      }
    }
  }

  override def createUser(u: User): Future[Option[User]] = {
    for {
      id <- userRepository.insert(u.copy(password = Encrypt.digestSHA(u.password), createdAt = Some(DateTime.now), updatedAt = Some(DateTime.now)))
      x <- findById(id)
    } yield {
      cache.remove(Constants.User.CACHE_PREFIX_ALL)
      cache.set(Constants.User.CACHE_PREFIX_ID(id), x, 5.minutes)
      x
    }
  }

  override def updateUser(u: User): Future[Option[User]] = {
    for {
      e <- userRepository.findById(u.id).map(ex => if (ex.isEmpty) throw new BusinessException(Validation.NOT_FOUND, Constants.User.USER_NOT_FOUND))
      id <- userRepository.update(u.copy(updatedAt = Some(DateTime.now)))
      x <- findById(id)
    } yield {
      cache.remove(Constants.User.CACHE_PREFIX_ALL)
      cache.set(Constants.User.CACHE_PREFIX_ID(id), x, 5.minutes)
      x
    }
  }

  override def deleteUser(id: Long): Future[Option[User]] = {
    for {
      x <- findById(id).map(ex => if (ex.isEmpty) throw new BusinessException(Validation.NOT_FOUND, Constants.User.USER_NOT_FOUND) else ex)
      _ <- userRepository.deleteById(id).map(x => if (x == 0) throw new BusinessException(Validation.BAD_REQUEST, Constants.User.DELETE_USER_NOT_ALLOWED))
    } yield {
      cache.remove(Constants.User.CACHE_PREFIX_ID(id))
      cache.remove(Constants.User.CACHE_PREFIX_ALL)
      x
    }
  }
}
