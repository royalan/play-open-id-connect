package an.royal.oidc

import javax.inject._

import an.royal.oidc.services.ISessionService
import io.jsonwebtoken.Claims
import play.api.cache.AsyncCacheApi
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

package object controllers {

  class UserRequest[A](
                        val userID: String,
                        request: Request[A]
                      ) extends WrappedRequest[A](request)

  @Singleton
  class UserInfoAction @Inject()(cache: AsyncCacheApi, playBodyParsers: PlayBodyParsers, sessionService: ISessionService)
                                (implicit val executionContext: ExecutionContext) extends ActionBuilder[UserRequest, AnyContent] with Results {

    import an.royal.oidc.constants.OpenIDConstants._

    override def parser: BodyParser[AnyContent] = playBodyParsers.anyContent

    override def invokeBlock[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {

      sessionService.checkSession(request.session.get(SESSION_ID), request.cookies.get(TOKEN).map(_.value))
        .flatMap {
          case Success(claims: Claims) => block(new UserRequest[A](claims.getSubject, request))
          // FIXME should valid previous request and now we only support GET request. If we want to support POST request, save whole request to cache might be the solution.
          case Failure(_) => Future.successful(TemporaryRedirect(routes.HomeController.index().url).withSession("preReq" -> request.uri))
        }
    }

  }

}
