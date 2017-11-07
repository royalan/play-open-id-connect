package an.royal.oidc.filters

import javax.inject._

import play.api.Logger
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class LoggingFilter @Inject()(implicit ec: ExecutionContext) extends EssentialFilter {


  override def apply(next: EssentialAction) = EssentialAction { request =>
    val startTime = System.currentTimeMillis

    next(request).map { result =>
      Logger.info(s"Open ID REST: ${result.header.status} ${request.method} ${request.uri.split("\\?")(0)} ${System.currentTimeMillis - startTime} ${request.headers}")
      result
    }
  }
}