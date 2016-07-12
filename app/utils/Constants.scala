package utils

/**
  * Created by Satria Pribadi on 12/07/16.
  */
object Constants {

  object Global {
    val EMPTY_STRING = ""
  }

  object User {
    val USER_NOT_FOUND = "User Not Found"

    def CACHE_PREFIX_ID(id: Long): String = {
      s"user_$id"
    }
  }

}
