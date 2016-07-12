package repositories

import slick.lifted.CanBeQueryCondition

import scala.concurrent.Future

/**
  * Created by Satria Pribadi on 12/07/16.
  */
trait BaseRepository[T, A] {
  def insert(row: A): Future[Long]

  def insert(rows: Seq[A]): Future[Seq[Long]]

  def update(row: A): Future[Int]

  def update(rows: Seq[A]): Future[Unit]

  def findAll: Future[Seq[A]]

  def findById(id: Option[Long]): Future[Option[A]]

  def findByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Seq[A]]

  def deleteById(id: Long): Future[Int]

  def deleteById(ids: Seq[Long]): Future[Int]

  def deleteByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Int]
}
