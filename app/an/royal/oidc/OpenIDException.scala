package an.royal.oidc

import an.royal.oidc.constants.ErrorCodes.ErrorCode

case class OpenIDException(errorCode: ErrorCode, message: Option[String] = None) extends Exception
case class InvalidSessionException() extends Exception