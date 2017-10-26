package an.royal.oidc.repositories

import javax.inject._

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ClientRepository @Inject()(@NamedDatabase("openid") protected val dbConfigProvider: DatabaseConfigProvider)
                                (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import PostgresProfile.api._

  private val clients = TableQuery[ClientTable]

  def insert(client: Client): Future[Unit] = db.run(clients += client).map { _ => () }

  def findByClientID(clientID: String): Future[Option[Client]] = db.run(clients.filter(_.clientID === clientID).result.headOption)

  def existedByClientID(clientID: String): Future[Boolean] = db.run(clients.filter(_.clientID === clientID).exists.result)

  private class ClientTable(tag: Tag) extends Table[Client](tag, "clients") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def description = column[String]("description")

    def clientID = column[String]("client_id", O.Unique)

    def clientSecret = column[String]("client_secret")

    def homepageURI = column[String]("homepage_uri")

    def privacyPolicyURI = column[String]("privacy_policy_uri")

    def logoURI = column[String]("logo_uri")

    def termsOfServiceURI = column[String]("terms_of_service_uri")

    def jwkCertURI = column[String]("jwk_cert_uri")

    def jwkCert = column[String]("jwk_cert")

    def email = column[String]("email")

    def redirectURIs = column[List[String]]("redirect_uris")

    def scopes = column[List[String]]("scopes")

    def `type` = column[String]("type")

    def createdTime = column[Long]("created_time")

    def lastModifiedTime = column[Long]("last_modified_time")

    def isDeleted = column[Boolean]("is_deleted")

    def * = (id, name, description, clientID, clientSecret, homepageURI, privacyPolicyURI, logoURI, termsOfServiceURI,
      jwkCertURI, jwkCert, email, redirectURIs, scopes, `type`, createdTime, lastModifiedTime, isDeleted) <> (Client.tupled, Client.unapply)
  }

}

case class Client(
                   id: Long,
                   name: String,
                   description: String,
                   clientID: String,
                   clientSecret: String,
                   homepageURI: String,
                   privacyPolicyURI: String,
                   logoURI: String,
                   termsOfServiceURI: String,
                   jwkCertURI: String, // public key URI
                   jwkCert: String, // public key
                   email: String,
                   redirectURIs: List[String],
                   scopes: List[String],
                   `type`: String,
                   createdTime: Long,
                   lastModifiedTime: Long,
                   isDeleted: Boolean
                 )

case class Scope(
                  id: Long,
                  name: String,
                  description: String
                )