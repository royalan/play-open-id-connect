package an.royal.oidc

import java.util.Base64
import javax.inject._

import an.royal.oidc.dtos.UserLoginReq
import an.royal.oidc.repositories.ClientRepository
import an.royal.oidc.services.SessionService
import io.jsonwebtoken.Claims
import play.api.cache.AsyncCacheApi
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

package object controllers {

  class UserRequest[A](
                        val userID: String,
                        request: Request[A]
                      ) extends WrappedRequest[A](request)

  class ClientRequest[A](
                          val clientID: String,
                          request: Request[A]
                        ) extends WrappedRequest[A](request)


  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> text
    )(UserLoginReq.apply)(UserLoginReq.unapply)
  )

  @Singleton
  class UserInfoAction @Inject()(cache: AsyncCacheApi, playBodyParsers: PlayBodyParsers, sessionService: SessionService)
                                (implicit val executionContext: ExecutionContext) extends ActionBuilder[UserRequest, AnyContent] {

    import an.royal.oidc.constants.OpenIDConstants._

    override def parser: BodyParser[AnyContent] = playBodyParsers.anyContent

    override def invokeBlock[A](request: Request[A], block: (UserRequest[A]) => Future[Result]): Future[Result] = {

      sessionService.checkSession(request.session.get(SESSION_ID), request.cookies.get(TOKEN).map(_.value))
        .flatMap { claims: Claims => block(new UserRequest[A](claims.getSubject, request)) }
        // FIXME should valid previous request and now we only support GET request. If we want to support POST request, save whole request to cache might be the solution.
        .recover { case _: InvalidSessionException => Results.Redirect("/").withSession("preReq" -> request.uri) }
    }

  }

  @Singleton
  class ClientInfoAction @Inject()(clientRepository: ClientRepository, playBodyParsers: PlayBodyParsers)
                                  (implicit val executionContext: ExecutionContext) extends ActionBuilder[ClientRequest, AnyContent] {

    override def parser: BodyParser[AnyContent] = playBodyParsers.anyContent

    override def invokeBlock[A](request: Request[A], block: (ClientRequest[A]) => Future[Result]): Future[Result] = {
      request.headers.get("Authorization") match {
        case Some(au) =>
          val idAndSecret = new String(Base64.getDecoder.decode(au.drop("Basic ".length))).split(":")
          clientRepository.existedByClientIDAndSecret(idAndSecret(0), idAndSecret(1))
              .flatMap(_ => block(new ClientRequest[A](idAndSecret(0), request)))
        case None => Future.successful(Results.Status(401))
      }
    }

  }

}
