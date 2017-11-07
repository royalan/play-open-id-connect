package an.royal.oidc.repositories

import javax.inject.{Inject, Singleton}

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ScopeRepository @Inject()(@NamedDatabase("openid") protected val dbConfigProvider: DatabaseConfigProvider)
                               (implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  import PostgresProfile.api._

  private val scopes = TableQuery[ScopeTable]

  def createTable: Future[Unit] = db.run(DBIO.seq(scopes.schema.create))

  def insert(scope: Scope): Future[Int] = db.run(scopes += scope)

  def findByName(name: String): Future[Option[Scope]] = db.run(scopes.filter(_.name === name).result.headOption)

  private class ScopeTable(tag: Tag) extends Table[Scope](tag, "scopes") {

    def id = column[String]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def description = column[String]("description")

    def createdTime = column[Long]("created_time", O.SqlType("bigint default extract(epoch from now()) * 1000"))

    def lastModifiedTime = column[Long]("last_modified_time", O.SqlType("bigint default extract(epoch from now()) * 1000"))

    def isDeleted = column[Boolean]("is_deleted", O.Default(false))

    def * = (id, name, description, createdTime, lastModifiedTime, isDeleted) <> (Scope.tupled, Scope.unapply)
  }

}

case class Scope(
                  id: String,
                  name: String,
                  description: String,
                  createdTime: Long,
                  lastModifiedTime: Long,
                  isDeleted: Boolean
                )



