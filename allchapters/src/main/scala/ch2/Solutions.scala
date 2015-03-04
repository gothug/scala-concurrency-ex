package ch2

import util.Util._

/**
 * @author Got Hug
 */
object Solutions {
  def parallel[A, B](a: =>A, b: =>B): (A, B) = {
    var aa: A
    var bb: B

    val t1 = thread(a)
    val t2 = thread(b)

    (t1.join(), t2.join())
  }
}
