package an.royal.oidc.controllers

import javax.inject._

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserInfoController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)
                                  (implicit mat: Materializer, exec: ExecutionContext) extends AbstractController(cc) {
  def getUserInfo = Action.async {
    Future.successful(BadRequest("Not yet implemented"))
  }
}