package an.royal.oidc.controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class HealthController @Inject()(cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def heartbeat = Action(Ok)

  def status = Action {
    Ok(Json.obj("status" -> "OK"))
  }

}
