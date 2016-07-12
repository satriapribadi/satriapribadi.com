package services

import models.User

import scala.concurrent.Future

/**
  * Created by Satria Pribadi on 12/07/16.
  */
trait UserService {
  def findById(id: Long): Future[Option[User]]
}
