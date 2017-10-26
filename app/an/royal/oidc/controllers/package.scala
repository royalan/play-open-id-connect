package an.royal.oidc

import javax.inject._

import io.jsonwebtoken.{Jwts, SignatureException}
import play.api.Logger
import play.api.cache.AsyncCacheApi
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

package object controllers {

  class UserRequest[A](
                        val userID: String,
                        request: Request[A]
                      ) extends WrappedRequest[A](request)

  @Singleton
  class UserInfoAction @Inject()(cache: AsyncCacheApi, playBodyParsers: PlayBodyParsers)
                                (implicit val executionContext: ExecutionContext) extends ActionBuilder[UserRequest, AnyContent] with Results {

    import an.royal.oidc.constants.OpenIDConstants._

    override def parser: BodyParser[AnyContent] = playBodyParsers.anyContent

    override def invokeBlock[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {
      def authFail = {
        // FIXME should valid previous request and now we only support GET request. If we want to support POST request, save whole request to cache might be the solution.
        Future.successful(TemporaryRedirect(routes.HomeController.index().url)
          .withSession("preReq" -> request.uri))
      }

      val userReq: Option[Future[Result]] = for {
        sessionID <- request.session.get(SESSION_ID)
        tokenCookie <- request.cookies.get(TOKEN)
      } yield {
        // get secret key of token from cache
        Logger.info(s"Got session ID: $sessionID")
        cache.get[Array[Byte]](sessionID)
          .flatMap {
            case Some(key) =>
              try {
                Logger.info(s"key: $key")
                // trying to decode and valid token by setting signing key.
                val jwtClaims = Jwts.parser().setSigningKey(key).parseClaimsJws(tokenCookie.value).getBody
                block(new UserRequest[A](jwtClaims.getSubject, request))
              } catch {
                case _: SignatureException => authFail
              }
            case _ => authFail
          }
      }

      userReq.getOrElse(authFail)
    }

  }

}
