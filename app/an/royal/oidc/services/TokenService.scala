package an.royal.oidc.services

import java.security.SecureRandom
import java.util.{Base64, Calendar, UUID}
import javax.inject._

import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import an.royal.oidc.OpenIDException
import an.royal.oidc.constants.ErrorCodes
import an.royal.oidc.repositories.{SecretKeyRepository, User, UserRepository}
import io.jsonwebtoken.{Claims, Jwts, SignatureAlgorithm}
import play.api.Configuration
import play.api.cache.AsyncCacheApi

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TokenService @Inject()(userRepository: UserRepository, secretKeyRepository: SecretKeyRepository, randomService: RandomService,
                             cache: AsyncCacheApi, config: Configuration)
                            (implicit mat: Materializer, ec: ExecutionContext) {

  val random = new SecureRandom()

  def createAccessToken(clientID: String): Future[String] = {
    val token = Base64.getEncoder.encodeToString(UUID.randomUUID().toString.getBytes())
    cache.set(token, clientID).map(_ => token)
  }

  def createIDToken(userID: String, clientID: String, nonce: String): Future[String] = {
    secretKeyRepository.findByType("id_token").flatMap {
      case Some(s) => createJWT(userID, clientID, s.key, nonce)
      case None => Future.failed(OpenIDException(ErrorCodes.SECRET_KEY_NOT_FOUND, Some("Can not found secret key for id_token")))
    }
  }

  def createJWT(userID: String, clientID: String, key: Array[Byte], nonce: String): Future[String] = {
    val calendar = Calendar.getInstance
    val now = calendar.getTime
    calendar.add(Calendar.DAY_OF_YEAR, config.get[Int]("jwt.duration"))
    val exp = calendar.getTime

    userRepository.findByUserID(userID).map {
      case Some(u) =>
        Jwts.builder()
          .signWith(SignatureAlgorithm.HS256, key)
          .setSubject(userID)
          .setAudience(clientID)
          .setIssuer(config.get[String]("jwt.issuer"))
          .setIssuedAt(now)
          .setExpiration(exp)
          .setClaims(buildCustomerClaims(u, nonce))
          .compact()
      case None => throw OpenIDException(ErrorCodes.USER_NOT_FOUND)
    }
  }

  def createGrantCode(clientID: String): Future[String] = {
    Source.single(randomService.newUniqueRandomValue(randomService.genNonUniqueRandomString, cache.sync.get))
      .mapAsync(1)(code => cache.set(code, clientID).map(_ => code))
      .runWith(Sink.head)
  }


  private def buildCustomerClaims(user: User, nonce: String): Claims = {
    val claims = Jwts.claims()
    claims.put("name", user.name)
    claims.put("display_name", user.displayName)
    claims.put("avatar", user.avatar)
    claims.put("email", user.email)
    claims.put("email_verified", user.emailVerified.toString)
    claims.put("nonce", nonce)
    claims
  }

}
