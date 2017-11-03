package an.royal.oidc.controllers

import javax.inject._

import akka.stream.Materializer
import an.royal.oidc.repositories._
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class AdminController @Inject()(secretKeyRepository: SecretKeyRepository, clientRepository: ClientRepository, userRepository: UserRepository, userConsentRepository: UserConsentRepository, tokenRepository: TokenRepository, cc: ControllerComponents)
                               (implicit mat: Materializer, ec: ExecutionContext) extends AbstractController(cc) {

  def init = Action.async {
    val u = userRepository.createTable
    val uc = userConsentRepository.createTable
    val t = tokenRepository.createTable
    val c = clientRepository.createTable
    val sk = secretKeyRepository.createTable
    for {
      _ <- u
      _ <- uc
      _ <- t
      _ <- c
      _ <- sk
    } yield {
      Ok
    }
  }

}
