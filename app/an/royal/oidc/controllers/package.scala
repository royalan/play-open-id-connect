package an.royal.oidc

import javax.inject._

import io.jsonwebtoken.{Jwts, SignatureException}
import play.api.Logger
import play.api.cache.AsyncCacheApi
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

package object controllers {

  class UserRequest[A](
                        userID: Option[String],
                        request: Request[A]
                      ) extends WrappedRequest[A](request)

  @Singleton
  class UserInfoAction @Inject()(cache: AsyncCacheApi, playBodyParsers: PlayBodyParsers)
                                (implicit val executionContext: ExecutionContext) extends ActionBuilder[UserRequest, AnyContent] with Results {

    import an.royal.oidc.constants.OpenIDConstants._

    override def parser: BodyParser[AnyContent] = playBodyParsers.anyContent

    override def invokeBlock[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {
      val userReq: Option[Future[Result]] = for {
        sessionID <- request.session.get(SESSION_ID)
        tokenCookie <- request.cookies.get(TOKEN)
      } yield {
        cache.get(sessionID).mapTo[String]
          .flatMap { key =>
            try {
              val jwtClaims = Jwts.parser().setSigningKey(key).parseClaimsJws(tokenCookie.value).getBody

              block(new UserRequest[A](Some(jwtClaims.getSubject), request))
            } catch {
              case _: SignatureException =>
                Logger.debug(s"Got invalid token: ${tokenCookie.value}")
                Future.successful(Results.TemporaryRedirect(routes.HomeController.login.url))
            }
          }
      }
      userReq.getOrElse(block(new UserRequest[A](None, request)))
    }
  }

}
