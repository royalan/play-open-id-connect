package an.royal.oidc.services

import javax.inject._

import an.royal.oidc.InvalidSessionException
import an.royal.oidc.constants.ErrorCodes
import io.jsonwebtoken.{Claims, Jwts}
import play.api.Logger
import play.api.cache.AsyncCacheApi

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

sealed trait ISessionService {

  def checkSession(sessionID: Option[String], token: Option[String]): Future[_]

  def createSession(userID: String): Unit

  def revokeSession(sessionID: String): Unit
}

@Singleton
class SessionCacheService @Inject()(cache: AsyncCacheApi)(implicit ec: ExecutionContext) extends ISessionService {

  override def checkSession(sessionID: Option[String], token: Option[String]): Future[Try[Claims]] = {
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
              Logger.info(s"key: $key")
              // trying to decode and valid token by setting signing key.
              Success(Jwts.parser().setSigningKey(key).parseClaimsJws(t).getBody)
            case _ => Failure(InvalidSessionException())
          }
      }

    validSession.getOrElse(Future.successful(Failure(InvalidSessionException(Some(ErrorCodes.SESSION_NOT_FOUND)))))
  }


  override def createSession(userID: String): Unit = ???

  override def revokeSession(sessionID: String): Unit = ???
}
