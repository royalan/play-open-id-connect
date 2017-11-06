package an.royal.oidc.dtos

import an.royal.oidc.constants.ErrorCodes.ErrorCode
import play.api.libs.json.Json
import play.api.mvc.{Result, Results}

object HttpBaseResponse {

  def errorResponse(status: Int, errCode: ErrorCode, message: Option[String] = None): Result = {
    Results.Status(status)(Json.toJson(ErrorResponse(ErrorDetail(errCode.id, message.getOrElse(errCode.toString)))))
  }

  case class ErrorDetail(errNo: Int, message: String)
  case class ErrorResponse(error: ErrorDetail)

  implicit val errorDetailReads = Json.reads[ErrorDetail]
  implicit val errorDetailWrites = Json.writes[ErrorDetail]
  implicit val errorHttpResponseReads = Json.reads[ErrorResponse]
  implicit val errorHttpResponseWrites = Json.writes[ErrorResponse]

}
