package an.royal.oidc.repositories

import javax.inject._

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(@NamedDatabase("openid") protected val dbConfigProvider: DatabaseConfigProvider)
                              (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import PostgresProfile.api._

  private val users = TableQuery[UserTable]

  def insert(user: User): Future[Unit] = db.run(users += user).map { _ => () }

  def findByUserID(userID: String): Future[Option[User]] = db.run(users.filter(_.userID === userID).result.headOption)

  def checkUserPassword(email: String, password: String): Future[Boolean] = db.run(users.filter(u => u.email === email && u.password === password).exists.result)

  private class UserTable(tag: Tag) extends Table[User](tag, "user") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def userID = column[String]("user_id", O.Unique)

    def password = column[String]("password")

    def name = column[String]("name", O.Unique)

    def displayName = column[String]("display_name")

    def givenName = column[String]("given_name")

    def familyName = column[String]("family_name")

    def mobileCountryCode = column[String]("mobile_country_code")

    def mobilePhoneNumber = column[String]("mobile_phone_number")

    def email = column[String]("email")

    def emailVerified = column[Boolean]("email_verified")

    def avatar = column[String]("avatar")

    def homepageURI = column[String]("homepage_rui")

    def createdTime = column[Long]("created_time")

    def lastModifiedTime = column[Long]("last_modified_time")

    def isDeleted = column[Boolean]("is_deleted")

    def * = (id, userID, password, name, displayName, givenName, familyName, mobileCountryCode, mobilePhoneNumber,
      email, emailVerified, avatar, homepageURI, createdTime, lastModifiedTime, isDeleted) <> (User.tupled, User.unapply)
  }

}

case class User(
                 id: Long,
                 userId: String,
                 password: String,
                 name: String,
                 displayName: String,
                 givenName: String,
                 familyName: String,
                 mobileCountryCode: String,
                 mobilePhoneNumber: String,
                 email: String,
                 emailVerified: Boolean,
                 avatar: String,
                 homepageURI: String,
                 createdTime: Long,
                 lastModifiedTime: Long,
                 isDeleted: Boolean
               )

