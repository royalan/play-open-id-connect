package an.royal.oidc.controllers

import javax.inject._

import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(userInfoAction: UserInfoAction, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def landing = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

}