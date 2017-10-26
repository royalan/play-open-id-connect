package an.royal.oidc.repositories

import javax.inject._

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserConsentRepository @Inject()(@NamedDatabase("openid") protected val dbConfigProvider: DatabaseConfigProvider)
                                     (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import PostgresProfile.api._

  private val userConsents = TableQuery[UserConsentTable]

  def createTable = db.run(DBIO.seq(userConsents.schema.create))

  def insert(consent: UserConsent): Future[Unit] = db.run(userConsents += consent).map { _ => () }

  def findByUserIDAndClientID(userID: String, clientID: String): Future[Option[UserConsent]] =
    db.run(userConsents.filter(c => c.userID === userID && c.clientID === clientID).result.headOption)

  private class UserConsentTable(tag: Tag) extends Table[UserConsent](tag, "user_consents") {
    // TODO add foreign key?

    def userID = column[String]("user_id")

    def clientID = column[String]("client_id")

    def scopes = column[List[String]]("scopes")

    def createdTime = column[Long]("created_time")

    def lastModifiedTime = column[Long]("last_modified_time")

    def isDeleted = column[Boolean]("is_deleted", O.Default(false))

    def * = (userID, clientID, scopes, createdTime, lastModifiedTime, isDeleted) <> (UserConsent.tupled, UserConsent.unapply)

    def pk = primaryKey("pk_user_consents", (userID, clientID))
  }

}

case class UserConsent(
                        userID: String,
                        clientID: String,
                        scopes: List[String],
                        createdTime: Long,
                        lastModifiedTime: Long,
                        isDeleted: Boolean
                      )


