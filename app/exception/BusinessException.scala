package exception

/**
  * Created by Satria Pribadi on 30/06/16.
  */
case class BusinessException(message: String, cause: Throwable = null) extends BaseException(message, cause)
