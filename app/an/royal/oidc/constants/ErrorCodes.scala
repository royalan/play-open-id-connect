package an.royal.oidc.constants

object ErrorCodes extends Enumeration {
  type ErrorCode = Value

  val INVALID_OPEN_ID_RESPONSE_TYPE = Value(10001)
  val INVALID_CLIENT_ID = Value(10002)
  val INVALIDE_RESPONSE_TYPE = Value(10003)
  val INVALID_SCOPE = Value(10004)
  val INVALID_REDIRECT_URI = Value(10005)
  val INVALID_PROMPT = Value(10006)
  val INVALID_DISPLAY = Value(10007)
  val ILLEGAL_STATE_OF_PROMPT_VALUE = Value(10008)
  val SESSION_NOT_FOUND = Value(10009)

  val NOT_YET_IMPLEMENTED = Value(99998)
  val UNKNOWN_ERROR = Value(99999)
}
