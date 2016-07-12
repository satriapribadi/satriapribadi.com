package utils

import java.security.MessageDigest

/**
  * Created by Satria Pribadi on 16/06/16.
  */
object Encrypt {
  def digestSHA(s: String): String = {
    val md = MessageDigest.getInstance("SHA-1")
    md.update(s.getBytes)
    md.digest.foldLeft("") { (s, b) =>
      s + "%02x".format(if (b < 0) b + 256 else b)
    }
  }
}
