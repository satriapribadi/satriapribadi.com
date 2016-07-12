package repositories.impl

import javax.inject.Singleton

import models.{BaseModel, BaseTable}
import play.api.db.slick.HasDatabaseConfigProvider
import repositories.BaseRepository
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import slick.lifted.CanBeQueryCondition

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by Satria Pribadi on 12/07/16.
  */
@Singleton
abstract class BaseRepositoryImpl[T <: BaseTable[A], A <: BaseModel](implicit val tableQ: TableQuery[T]) extends BaseRepository[T, A] with HasDatabaseConfigProvider[JdbcProfile] {

  import dbConfig.driver.api._

  def insert(row: A): Future[Long] = {
    insert(Seq(row)).map(_.head)
  }

  def insert(rows: Seq[A]): Future[Seq[Long]] = {
    db.run(tableQ returning tableQ.map(_.id) ++= rows.filter(_.isValid))
  }

  def update(row: A): Future[Int] = {
    if (row.isValid) db.run(tableQ.filter(_.id === row.id).update(row))
    else Future {
      0
    }
  }

  def update(rows: Seq[A]): Future[Unit] = {
    db.run(DBIO.seq(rows.filter(_.isValid).map(r => tableQ.filter(_.id === r.id).update(r)): _*))
  }

  def findAll: Future[Seq[A]] = {
    db.run(tableQ.result)
  }

  def findById(id: Option[Long]): Future[Option[A]] = {
    db.run(tableQ.filter(_.id === id.getOrElse(0L)).result.headOption)
  }

  def findByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Seq[A]] = {
    db.run(tableQ.withFilter(f).result)
  }

  def deleteById(id: Long): Future[Int] = {
    deleteById(Seq(id))
  }

  def deleteById(ids: Seq[Long]): Future[Int] = {
    db.run(tableQ.filter(_.id.inSet(ids)).delete)
  }

  def deleteByFilter[C: CanBeQueryCondition](f: (T) => C): Future[Int] = {
    db.run(tableQ.withFilter(f).delete)
  }
}
