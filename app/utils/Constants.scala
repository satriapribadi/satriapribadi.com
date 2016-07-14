package utils

/**
  * Created by Satria Pribadi on 12/07/16.
  */
object Constants {

  object Global {
    val EMPTY_STRING = ""
    val SLASH = "/"
    val STATUS = "status"
    val MESSAGE = "message"
    val ERROR_MESSAGE = "errorMessage"
  }

  object User {
    val USER_NOT_FOUND = "User not found"
    val DELETE_USER_NOT_ALLOWED = "User cannot be delete!"

    val CACHE_PREFIX_ALL = "user_all"

    def CACHE_PREFIX_ID(id: Long): String = {
      s"user_id_$id"
    }
  }

}
