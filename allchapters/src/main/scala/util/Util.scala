package util

/**
 * @author Got Hug
 */
object Util {
  def thread(body: =>Unit): Thread = {
    val t = new Thread {
      override def run() = body
    }
    t.start()
    t
  }
}
