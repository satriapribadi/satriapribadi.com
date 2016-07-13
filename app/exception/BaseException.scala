package exception

/**
  * Created by Satria Pribadi on 30/06/16.
  */
abstract class BaseException(message: String, cause: Throwable = null) extends RuntimeException(message, cause)
