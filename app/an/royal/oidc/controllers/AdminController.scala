package an.royal.oidc.controllers

import javax.inject._

import akka.stream.Materializer
import an.royal.oidc.repositories.{TokenRepository, UserConsentRepository, UserRepository}
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class AdminController @Inject()(userRepository: UserRepository, userConsentRepository: UserConsentRepository, tokenRepository: TokenRepository, cc: ControllerComponents)
                               (implicit mat: Materializer, ec: ExecutionContext) extends AbstractController(cc) {

  def init = Action.async {
    val u = userRepository.createTable
    val uc = userConsentRepository.createTable
    val t = tokenRepository.createTable
    for {
      _ <- u
      _ <- uc
      _ <- t
    } yield {
      Ok
    }
  }

}
