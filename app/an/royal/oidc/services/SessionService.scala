package an.royal.oidc.services

import javax.inject._

import an.royal.oidc.InvalidSessionException
import io.jsonwebtoken.{Claims, Jwts}
import play.api.Logger
import play.api.cache.AsyncCacheApi

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SessionService @Inject()(cache: AsyncCacheApi)(implicit ec: ExecutionContext) extends{

  def checkSession(sessionID: Option[String], token: Option[String]): Future[Claims] = {
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
              Jwts.parser().setSigningKey(key).parseClaimsJws(t).getBody
            case _ => throw InvalidSessionException()
          }
      }

    validSession.getOrElse(Future.failed(InvalidSessionException()))
  }

}
