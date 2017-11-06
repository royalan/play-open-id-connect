package an.royal.oidc.services

import javax.inject._

import an.royal.oidc.OpenIDException
import an.royal.oidc.constants.ErrorCodes
import io.jsonwebtoken.{Claims, Jwts}
import play.api.Logger
import play.api.cache.AsyncCacheApi

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

@Singleton
class SessionService @Inject()(cache: AsyncCacheApi)(implicit ec: ExecutionContext) extends{

  def checkSession(sessionID: Option[String], token: Option[String]): Future[Try[Claims]] = {
    val validSession =
      for {
        sid <- sessionID
        t <- token
      } yield {
        // get secret key of token from cache
        Logger.info(s"Got session ID: $sid")
        cache.get[Array[Byte]](sid)
          .map {
            case Some(key) =>
              // trying to decode and valid token by setting signing key.
              Success(Jwts.parser().setSigningKey(key).parseClaimsJws(t).getBody)
            case _ => Failure(OpenIDException(ErrorCodes.SESSION_NOT_FOUND))
          }
      }

    validSession.getOrElse(Future.successful(Failure(OpenIDException(ErrorCodes.SESSION_NOT_FOUND))))
  }

}
