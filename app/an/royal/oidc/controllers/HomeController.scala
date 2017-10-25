package an.royal.oidc.controllers

import java.util.Base64
import javax.inject._

import an.royal.oidc.repositories.UserRepository
import an.royal.oidc.services.EncryptionService
import io.jsonwebtoken.{Jwts, SignatureAlgorithm}
import play.api.Logger
import play.api.cache.AsyncCacheApi
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(encryptionService: EncryptionService, cache: AsyncCacheApi, userRepository: UserRepository, userInfoAction: UserInfoAction, cc: ControllerComponents)
                              (implicit ec: ExecutionContext) extends AbstractController(cc) {

  import an.royal.oidc.constants.OpenIDConstants._
  import an.royal.oidc.dtos.UserLoginReq


  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> text
    )(UserLoginReq.apply)(UserLoginReq.unapply)
  )

  def index = Action { implicit req =>
    Ok(views.html.login(loginForm))
  }

  def init = Action.async {
    userRepository.createTable.map(_ => Ok("done!"))
  }

  def login = Action.async { implicit req =>
    val successFunc = { loginReq: UserLoginReq =>
      userRepository.checkUserPassword(loginReq.email, loginReq.password).map {
        // verify successfully
        case Some(userID) =>
          val sid = Base64.getEncoder.encodeToString(Random.nextString(4).getBytes)
          val key = encryptionService.newSecretKey
          cache.set(sid, key)

          // if there is previous request store in session, redirect to it. otherwise, just redirect to root path
          Logger.debug(s"get preReq ${req.session.get("preReq")}")
          Redirect(req.session.get("preReq").getOrElse("/hello"))
            .withSession(SESSION_ID -> sid)
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