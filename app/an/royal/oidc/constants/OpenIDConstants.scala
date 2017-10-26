package an.royal.oidc.constants

/**
  * Enumeration reference: http://openid.net/specs/openid-connect-core-1_0.html#AuthRequest
  */

object OpenIDConstants {
  val TOKEN = "token"
  val SESSION_ID = "sessionID"
}



/**
  * page
  *   The Authorization Server SHOULD display the authentication and consent UI consistent with a full User Agent page view. If the display parameter is not specified, this is the default display mode.
  * popup
  *   The Authorization Server SHOULD display the authentication and consent UI consistent with a popup User Agent window. The popup User Agent window should be of an appropriate size for a login-focused dialog and should not obscure the entire window that it is popping up over.
  * touch
  *   The Authorization Server SHOULD display the authentication and consent UI consistent with a device that leverages a touch interface.
  * wap
  *   The Authorization Server SHOULD display the authentication and consent UI consistent with a "feature phone" type display.
  */
object OpenIDDisplay extends Enumeration {
  type Display = Value

  val PAGE = Value("page")
  val POPUP = Value("popup")
  val TOUCH = Value("touch")
  val WAP = Value("wap")
}

/**
  * none
  *   The Authorization Server MUST NOT display any authentication or consent user interface pages. An error is returned if an End-User is not already authenticated or the Client does not have pre-configured consent for the requested Claims or does not fulfill other conditions for processing the request. The error code will typically be login_required, interaction_required, or another code defined in Section 3.1.2.6. This can be used as a method to check for existing authentication and/or consent.
  * login
  *   The Authorization Server SHOULD prompt the End-User for reauthentication. If it cannot reauthenticate the End-User, it MUST return an error, typically login_required.
  * consent
  *   The Authorization Server SHOULD prompt the End-User for consent before returning information to the Client. If it cannot obtain consent, it MUST return an error, typically consent_required.
  * select_account
  *   The Authorization Server SHOULD prompt the End-User to select a user account. This enables an End-User who has multiple accounts at the Authorization Server to select amongst the multiple accounts that they might have current sessions for. If it cannot obtain an account selection choice made by the End-User, it MUST return an error, typically account_selection_required.
  */
object OpenIDPrompt extends Enumeration {
  type Prompt = Value

  val NONE = Value("none")
  val LOGIN = Value("login")
  val CONSENT = Value("consent")
  val SELECT_ACCOUNT = Value("select_account")
}

object OpenIDResponseType extends Enumeration {
  type ResponseType = Value

  val CODE = Value("code")
  val TOKEN = Value("token")
  val ID_TOKEN = Value("id_token")
  val NONE = Value("none")

  def fromName(name: String): Option[Value] = values.find(_.toString == name).orElse(None)
}