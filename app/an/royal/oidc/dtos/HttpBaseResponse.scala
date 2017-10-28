package an.royal.oidc.dtos

import an.royal.oidc.constants.ErrorCodes.ErrorCode
import play.api.libs.json.Json
import play.api.mvc.Results

object HttpBaseResponse {

  def errorResponse(status: Int, errCode: ErrorCode, message: Option[String] = None) = {
    Results.Status(status)(Json.toJson(ErrorResponse(ErrorDetail(errCode.id, message.getOrElse(errCode.toString)))))
  }

  case class ErrorDetail(errno: Int, message: String) {
    //    def toJsonString = Json.toJson(this)
  }

  implicit val errorDetailReads = Json.reads[ErrorDetail]
  implicit val errorDetailWrites = Json.writes[ErrorDetail]
  implicit val errorHttpResponseReads = Json.reads[ErrorResponse]
  implicit val errorHttpResponseWrites = Json.writes[ErrorResponse]

  case class ErrorResponse(error: ErrorDetail) {
    //    def toJsonString = Json.toJson(this)
  }
}
