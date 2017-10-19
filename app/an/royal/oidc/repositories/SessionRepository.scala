package an.royal.oidc.repositories

import javax.inject._

import akka.Done
import com.hazelcast.config.Config
import com.hazelcast.core.{Hazelcast, HazelcastInstance}
import play.api.cache.AsyncCacheApi

import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

sealed trait ISessionRepository {
  // return user ID
  def findByID(sessionID: String): Option[String]

  def insert(sessionID: String, userID: String): Unit

  def delete(sessionID: String): Unit
}

class HazelcastSessionRepository() extends ISessionRepository {
  import HazelcastSessionRepository._

  override def findByID(sessionID: String): Option[String] = {
    Option(getInstance.getMap(SESSION_USER_ID_MAP).get(sessionID))
  }

  override def insert(sessionID: String, userID: String): Unit = {
  }

  override def delete(sessionID: String): Unit = ???
}

object HazelcastSessionRepository {
  implicit val SESSION_USER_ID_MAP = "session-userID-map"

  private val instance: HazelcastInstance = Hazelcast.newHazelcastInstance(new Config())

  implicit def getInstance: HazelcastInstance = instance
}

@Singleton
class HazelcastCachImpl @Inject()(hazelcastSessionRepository: HazelcastSessionRepository) extends AsyncCacheApi {
  override def set(key: String, value: Any, expiration: Duration): Future[Done] = ???

  override def remove(key: String): Nothing = ???

  override def getOrElseUpdate[A: ClassTag](key: String, expiration: Duration = Duration.Inf)(orElse: => Future[A]): Future[A] = ???

  override def get[T: ClassTag](key: String): Future[Option[T]] = ???

  override def removeAll(): Future[Done] = ???
}