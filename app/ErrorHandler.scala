import javax.inject._

import an.royal.oidc.constants.ErrorCodes
import an.royal.oidc.controllers.routes
import an.royal.oidc.dtos.HttpBaseResponse
import an.royal.oidc.{InvalidSessionException, OpenIDException}
import play.api._
import play.api.http.DefaultHttpErrorHandler
import play.api.mvc._
import play.api.routing.Router

import scala.concurrent._

@Singleton
class ErrorHandler @Inject()(
                              env: Environment,
                              config: Configuration,
                              sourceMapper: OptionalSourceMapper,
                              router: Provider[Router]
                            ) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  override def onServerError(request: RequestHeader, t: Throwable): Future[Result] = Future.successful {
    t match {
      case _: InvalidSessionException => Results.Redirect(routes.HomeController.landing()).withSession("preReq" -> request.uri)
      case OpenIDException(code, msg) => HttpBaseResponse.errorResponse(400, code, msg)
      case t: Throwable =>
        Logger.error(s"Unexpected exception", t)
        HttpBaseResponse.errorResponse(500, ErrorCodes.UNKNOWN_ERROR)
    }

  }
}