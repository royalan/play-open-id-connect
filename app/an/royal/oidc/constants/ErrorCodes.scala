package an.royal.oidc.constants

object ErrorCodes extends Enumeration {
  type ErrorCode = Value

  val INVALID_OPEN_ID_RESPONSE_TYPE = Value(10001)

  val UNKNOWN_ERROR = Value(99999)
}
