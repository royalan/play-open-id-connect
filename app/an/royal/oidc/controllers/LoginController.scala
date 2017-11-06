package an.royal.oidc.controllers

import javax.inject._

import an.royal.oidc.repositories.UserRepository
import an.royal.oidc.services.RandomService
import io.jsonwebtoken.{Jwts, SignatureAlgorithm}
import play.api.Logger
import play.api.cache.SyncCacheApi
import play.api.data.Form
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LoginController @Inject()(userInfoAction: UserInfoAction, cache: SyncCacheApi, randomService: RandomService, userRepository: UserRepository, cc: ControllerComponents)
                               (implicit ec: ExecutionContext) extends AbstractController(cc) {

  import an.royal.oidc.constants.OpenIDConstants._
  import an.royal.oidc.dtos.UserLoginReq


  def login = Action.async { implicit req =>
    val successFunc = { loginReq: UserLoginReq =>
      userRepository.checkUserPassword(loginReq.email, loginReq.password).map {
        // verify successfully
        case Some(userID) =>
          val sid = randomService.newUniqueRandomValue(randomService.genNonUniqueRandomString, cache.get)
          val key = randomService.genNonUniqueRandomByteArray
          cache.set(sid, key)

          // if there is previous request store in session, redirect to it. otherwise, just redirect to root path
          Logger.debug(s"get preReq ${req.session.get("preReq")}")
          req.session.get("preReq").map(Redirect(_))
            .getOrElse(Redirect(routes.HomeController.landing()))
            .withSession(req.session + (SESSION_ID -> sid))
            .withCookies(
              // Now we only set user ID to token, we can set any other values like `email`, `avatar` if need.
              Cookie(TOKEN, Jwts.builder().signWith(SignatureAlgorithm.HS256, key).setSubject(userID).compact(), None))
        case None =>
          // FIXME consider about the password will return to view?
          BadRequest(views.html.login(loginForm.fill(loginReq).withError("LOGIN_FAIL", "Login fail, please check your email or password")))
      }
    }

    val errorFunc = { form: Form[UserLoginReq] =>
      form.errors.foreach(err => Logger.warn(s"${err.key}: ${err.message}"))
      Future.successful {
        BadRequest(views.html.login(form))
      }
    }

    loginForm.bindFromRequest().fold(errorFunc, successFunc)
  }

}
