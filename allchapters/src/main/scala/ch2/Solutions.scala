package ch2

import util.Util._

/**
 * @author Got Hug
 */
object Solutions {

  def parallel[A, B](a: =>A, b: =>B): (A, B) = {
    def runAndGetResult[A](a: =>A): A = {
      var aa: A = null.asInstanceOf[A]

      val t =
        thread {
          aa = a
        }

      t.join()

      aa
    }

    val aa = runAndGetResult(a)
    val bb = runAndGetResult(b)

    (aa, bb)
  }

  def periodically(duration: Long)(b: =>Unit): Unit = {
    val t = thread {
      while (true) {
        b
        Thread.sleep(duration)
      }
    }

    t.start()
    t.join()
  }
}
