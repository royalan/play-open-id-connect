package an.royal.oidc.repositories

import javax.inject._

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.JsValue
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SecretKeyRepository @Inject()(@NamedDatabase("openid") protected val dbConfigProvider: DatabaseConfigProvider)
                                   (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import PostgresProfile.api._

  private val secretKeys = TableQuery[SecretKeyTable]

  def createTable: Future[Unit] = db.run(DBIO.seq(secretKeys.schema.create))

  def insert(key: SecretKey): Future[Int] = db.run(secretKeys += key)

  def findByType(t: String): Future[Option[SecretKey]] = db.run(secretKeys.filter(_.`type` === t).result.headOption)

  private class SecretKeyTable(tag: Tag) extends Table[SecretKey](tag, "secret_keys") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def key = column[Array[Byte]]("key")

    def `type` = column[String]("type")

    def info = column[JsValue]("client_id")

    def createdTime = column[Long]("created_time", O.SqlType("bigint default extract(epoch from now()) * 1000"))

    def lastModifiedTime = column[Long]("last_modified_time", O.SqlType("bigint default extract(epoch from now()) * 1000"))

    def isDeleted = column[Boolean]("is_deleted", O.Default(false))

    def * = (id, key, `type`, info, createdTime, lastModifiedTime, isDeleted) <> (SecretKey.tupled, SecretKey.unapply)
  }

}

case class SecretKey(
                       id: Int,
                       key: Array[Byte],
                       `type`: String,
                       info: JsValue,
                       createdTime: Long,
                       lastModifiedTime: Long,
                       isDeleted: Boolean
                     )


