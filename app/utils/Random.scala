package utils

import java.security.SecureRandom

import scala.util._

/**
  * Created by Satria Pribadi on 16/06/16.
  */
object Random {
  def randomString(length: Int) = new Random(new SecureRandom()).alphanumeric.take(length).mkString
}
