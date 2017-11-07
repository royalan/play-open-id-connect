package an.royal.oidc.controllers

import javax.inject._

import akka.stream.Materializer
import an.royal.oidc.dtos.CreateSecretKeyReq
import an.royal.oidc.repositories._
import an.royal.oidc.services.RandomService
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AdminController @Inject()(scopeRepository: ScopeRepository, secretKeyRepository: SecretKeyRepository, clientRepository: ClientRepository, userRepository: UserRepository, userConsentRepository: UserConsentRepository, tokenRepository: TokenRepository,
                                randomService: RandomService, cc: ControllerComponents)
                               (implicit mat: Materializer, ec: ExecutionContext) extends AbstractController(cc) {

  def init = Action.async {
    Future.sequence(List(
      userRepository.createTable,
      userConsentRepository.createTable,
      tokenRepository.createTable,
      clientRepository.createTable,
      secretKeyRepository.createTable,
      scopeRepository.createTable
    )).map(_ => Ok)
  }

  def addSecretKey = Action.async(parse.json) { request =>
    import an.royal.oidc.dtos.AdminDTOs._

    val sKey = request.body.as[CreateSecretKeyReq]
    secretKeyRepository.insert(
      SecretKey(1, randomService.genNonUniqueRandomByteArray, sKey.`type`, Json.toJson(sKey.info), System.currentTimeMillis, System.currentTimeMillis, false)
    ).map(_ => Ok(sKey.`type`))
  }

}
