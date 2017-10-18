package an.royal.oidc.controllers

import javax.inject._

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TokenController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)
                               (implicit mat: Materializer, exec: ExecutionContext) extends AbstractController(cc) {
  def create(code: String, client_id: String, redirect_uri: String, grant_type: String) = Action.async {
    Future.successful(BadRequest("Not yet implemented"))
  }
}