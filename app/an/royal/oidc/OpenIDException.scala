package an.royal.oidc

import an.royal.oidc.constants.ErrorCodes.ErrorCode

case class OpenIDException(errorCode: ErrorCode, message: String) extends Exception