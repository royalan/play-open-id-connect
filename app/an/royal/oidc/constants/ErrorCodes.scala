package an.royal.oidc.constants

object ErrorCodes extends Enumeration {
  type ErrorCode = Value

  val INVALID_OPEN_ID_RESPONSE_TYPE = Value(10001)
  val INVALID_CLIENT_ID = Value(10002)
  val INVALID_RESPONSE_TYPE = Value(10003)
  val INVALID_SCOPE = Value(10004)
  val INVALID_REDIRECT_URI = Value(10005)
  val INVALID_PROMPT = Value(10006)
  val INVALID_DISPLAY = Value(10007)
  val ILLEGAL_STATE_OF_PROMPT_VALUE = Value(10008)
  val SESSION_NOT_FOUND = Value(10009)
  val USER_NOT_FOUND = Value(10010)
  val SECRET_KEY_NOT_FOUND = Value(10011)


  // open id auth, ref: http://openid.net/specs/openid-connect-core-1_0.html#AuthError
  val INTERACTION_REQUIRED = Value(11001)
  val LOGIN_REQUIRED = Value(11002)
  val CONSENT_REQUIRED = Value(11003)
  val INVALID_REQUEST_URI = Value(11004)
  val INVALID_REQUEST_OBJECT = Value(11005)
  val REQUEST_NOT_SUPPORTED = Value(11006)
  val REQUEST_URI_NOT_SUPPORTED = Value(11007)
  val REGISTRATION_NOT_SUPPORTED = Value(11008)
  // not use before implementation
  val ACCOUNT_SELECTION_REQUIRED = Value(11009)

  val INVALID_SESSION = Value(99997)
  val NOT_YET_IMPLEMENTED = Value(99998)
  val UNKNOWN_ERROR = Value(99999)
}