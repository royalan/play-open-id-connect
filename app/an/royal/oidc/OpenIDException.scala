package an.royal.oidc

import an.royal.oidc.constants.ErrorCodes.ErrorCode

case class OpenIDException(errorCode: ErrorCode, message: Option[String]) extends Exception