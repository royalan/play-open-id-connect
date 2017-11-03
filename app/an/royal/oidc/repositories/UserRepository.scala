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

  def createTable = db.run(DBIO.seq(users.schema.create))

  def insert(user: User): Future[Unit] = db.run(users += user).map { _ => () }

  def findByUserID(userID: String): Future[Option[User]] = db.run(users.filter(_.userID === userID).result.headOption)

  def checkUserPassword(email: String, password: String): Future[Option[String]] = db.run(users.filter(u => u.email === email && u.password === password).map(_.userID).result.headOption)

  def findByEmailAndPassword(email: String, password: String): Future[Option[User]] = db.run(users.filter(u => u.email === email && u.password === password).result.headOption)

  private class UserTable(tag: Tag) extends Table[User](tag, "users") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def userID = column[String]("user_id", O.Unique)

    def password = column[String]("password")

    def name = column[String]("name", O.Unique)

    def displayName = column[String]("display_name")

    def givenName = column[Option[String]]("given_name")

    def familyName = column[Option[String]]("family_name")

    def mobileCountryCode = column[Option[String]]("mobile_country_code")

    def mobilePhoneNumber = column[Option[String]]("mobile_phone_number")

    def email = column[String]("email")

    def emailVerified = column[Boolean]("email_verified", O.Default(false))

    def avatar = column[Option[String]]("avatar")

    def homepageURI = column[Option[String]]("homepage_rui")

    def createdTime = column[Long]("created_time", O.SqlType("bigint default extract(epoch from now()) * 1000"))

    def lastModifiedTime = column[Long]("last_modified_time", O.SqlType("bigint default extract(epoch from now()) * 1000"))

    def isDeleted = column[Boolean]("is_deleted", O.Default(false))

    def * = (id, userID, password, name, displayName, givenName, familyName, mobileCountryCode, mobilePhoneNumber,
      email, emailVerified, avatar, homepageURI, createdTime, lastModifiedTime, isDeleted) <> (User.tupled, User.unapply)
  }

}

case class User(
                 id: Long,
                 userID: String,
                 password: String,
                 name: String,
                 displayName: String,
                 givenName: Option[String],
                 familyName: Option[String],
                 mobileCountryCode: Option[String],
                 mobilePhoneNumber: Option[String],
                 email: String,
                 emailVerified: Boolean,
                 avatar: Option[String],
                 homepageURI: Option[String],
                 createdTime: Long,
                 lastModifiedTime: Long,
                 isDeleted: Boolean
               )

