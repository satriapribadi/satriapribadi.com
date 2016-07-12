package models

/**
  * Created by Satria Pribadi on 12/07/16.
  */
trait BaseModel {
  val id : Option[Long]
  def isValid = true
}


