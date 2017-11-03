package an.royal.oidc.services

import java.security.SecureRandom
import java.util.Base64
import javax.inject._

@Singleton
class RandomService @Inject()() {
  private val random = new SecureRandom()

  // TODO could be enhanced to save to cache directly
  def newUniqueRandomValue[A](gen: => A, checkExisting: A => Option[A]): A = {
    val value = gen
    if (checkExisting(value).isDefined)
      gen
    else
      value
  }

  // Might be collision
  def genNonUniqueRandomByteArray: Array[Byte] = {
    val arr = new Array[Byte](8)
    random.nextBytes(arr)
    arr
  }

  def genNonUniqueRandomString: String =
    Base64.getEncoder.encodeToString(genNonUniqueRandomByteArray)
}