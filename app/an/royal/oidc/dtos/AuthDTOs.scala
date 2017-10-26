package an.royal.oidc.dtos

import an.royal.oidc.constants.OpenIDDisplay.Display
import an.royal.oidc.constants.OpenIDPrompt.Prompt
import an.royal.oidc.constants.OpenIDResponseType.ResponseType

case class UserLoginReq(
                         email: String,
                         password: String
                       )

case class ClientAuthReq(
                          clientID: String,
                          responseTypes: Set[ResponseType],
                          scopes: Set[String],
                          redirectURI: String,
                          nonce: String,
                          state: Option[String],
                          prompt: Option[Prompt],
                          display: Option[Display]
                        )
