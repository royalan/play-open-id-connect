package an.royal.oidc.dtos

import play.api.libs.json.Json

case class CreateSecretKeyReq(
                               `type`: String,
                               info: String
                             )

object AdminDTOs {
  implicit val createSecretKeyReqReads = Json.reads[CreateSecretKeyReq]
}
