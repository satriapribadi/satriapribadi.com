package services.impl

import javax.inject.{Inject, Singleton}

import models.User
import repositories.UserRepository
import services.UserService

import scala.concurrent.Future

/**
  * Created by Satria Pribadi on 12/07/16.
  */
@Singleton
class UserServiceImpl @Inject()(userRepository: UserRepository) extends UserService {
  override def findById(id: Long): Future[Option[User]] = {
    userRepository.findById(Some(id))
  }
}
