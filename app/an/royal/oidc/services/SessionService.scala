package an.royal.oidc.services

import javax.inject._

sealed trait ISessionService {

  def checkSession(sessionID: String): Boolean

  def createSession(userID: String): Unit

  def revokeSession(sessionID: String): Unit
}

@Singleton
class SessionCacheService() extends ISessionService {

  override def checkSession(sessionID: String): Boolean = ???

  override def createSession(userID: String): Unit = ???

  override def revokeSession(sessionID: String): Unit = ???
}
