package an.royal.oidc.controllers

import javax.inject._

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import an.royal.oidc.OpenIDException
import an.royal.oidc.constants.ErrorCodes
import an.royal.oidc.dtos.CreateTokenReq
import an.royal.oidc.services.{TokenPayload, TokenService}
import play.api.Configuration
import play.api.cache.AsyncCacheApi
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

@Singleton
class TokenController @Inject()(clientInfoAction: ClientInfoAction, config: Configuration, tokenService: TokenService, cache: AsyncCacheApi, cc: ControllerComponents, actorSystem: ActorSystem)
                               (implicit mat: Materializer, exec: ExecutionContext) extends AbstractController(cc) {

  import an.royal.oidc.dtos.TokenDTOs._

  def create = clientInfoAction.async(parse.json) { request =>
    Source.single(request.body.as[CreateTokenReq]).mapAsync(1) { payload =>
      cache.get[TokenPayload](payload.code)
        .filter(_.exists(p => payload.grant_type == "authorization_code" && request.clientID == p.clientID && payload.redirect_uri == p.redirectURI))
        .flatMap(p => tokenService.createAccessToken(p.get, Some(config.get[Duration]("openid.duration.access-token.code-grant"))))
        .flatMap(t => cache.remove(payload.code).map(_ => Map("token" -> Seq(t))))
        .map(Redirect(payload.redirect_uri, _))
        .recover { case _: NoSuchElementException => throw OpenIDException(ErrorCodes.INVALID_GRANT_CODE) }
    }.runWith(Sink.head)
  }
}