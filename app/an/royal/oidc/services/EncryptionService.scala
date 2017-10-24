package an.royal.oidc.services

import java.security.SecureRandom
import javax.inject._

@Singleton
class EncryptionService @Inject()() {
  private val random = new SecureRandom()

  def newSecretKey: Array[Byte] = {
    val buf = new Array[Byte](32)
    random.nextBytes(buf)
    buf
  }
}