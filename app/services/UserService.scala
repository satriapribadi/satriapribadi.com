package services

import models.User

import scala.concurrent.Future

/**
  * Created by Satria Pribadi on 12/07/16.
  */
trait UserService {
  def deleteUser(id: Long): Future[Option[User]]

  def updateUser(u: User): Future[Option[User]]

  def createUser(u: User): Future[Option[User]]

  def findAllUser(): Future[Seq[User]]

  def findById(id: Long): Future[Option[User]]
}
