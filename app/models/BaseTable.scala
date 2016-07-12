package models

import com.github.tototoshi.slick.MySQLJodaSupport._
import org.joda.time.DateTime
import slick.driver.MySQLDriver.api._

/**
  * Created by Satria Pribadi on 12/07/16.
  */
abstract class BaseTable[T](tag: Tag, name: String) extends Table[T](tag, name) {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def createdAt = column[DateTime]("created_at")

  def updatedAt = column[DateTime]("updated_at")
}