
package an.royal.oidc.dtos

import play.api.libs.json.Json

case class CreateTokenReq(
                           code: String,
                           redirect_uri: String,
                           grant_type: String
                         )

object TokenDTOs {
  implicit val createTokenReqReads = Json.reads[CreateTokenReq]
}