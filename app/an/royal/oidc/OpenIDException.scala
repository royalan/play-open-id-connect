package an.royal.oidc

import javax.management.openmbean.OpenDataException

import an.royal.oidc.constants.ErrorCodes.ErrorCode

case class OpenIDException(errorCode: ErrorCode, message: Option[String] = None) extends Exception

case class InvalidSessionException(errorCode: Option[ErrorCode] = None) extends OpenDataException