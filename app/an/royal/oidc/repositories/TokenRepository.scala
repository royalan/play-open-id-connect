package an.royal.oidc.repositories

import javax.inject._

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.JsValue
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TokenRepository @Inject()(@NamedDatabase("openid") protected val dbConfigProvider: DatabaseConfigProvider)
                               (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import PostgresProfile.api._

  private val tokens = TableQuery[TokenTable]

  def createTable: Future[Unit] = db.run(DBIO.seq(tokens.schema.create))

  def insert(token: Token): Future[Int] = db.run(tokens += token)

  def findById(id: String): Future[Option[Token]] = db.run(tokens.filter(_.id === id).result.headOption)

  private class TokenTable(tag: Tag) extends Table[Token](tag, "tokens") {

    def id = column[String]("id", O.PrimaryKey)

    def info = column[JsValue]("client_id")

    def createdTime = column[Long]("created_time", O.SqlType("bigint default extract(epoch from now()) * 1000"))

    def lastModifiedTime = column[Long]("last_modified_time", O.SqlType("bigint default extract(epoch from now()) * 1000"))

    def isDeleted = column[Boolean]("is_deleted", O.Default(false))

    def * = (id, info, createdTime, lastModifiedTime, isDeleted) <> (Token.tupled, Token.unapply)
  }

}

case class Token(
                  id: String,
                  info: JsValue,
                  createdTime: Long,
                  lastModifiedTime: Long,
                  isDeleted: Boolean
                )


