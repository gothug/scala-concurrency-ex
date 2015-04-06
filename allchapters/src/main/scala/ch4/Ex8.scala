package ch4

import scala.concurrent.Promise
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @author Got Hug
 */
object Ex8 {
  implicit class PromiseOps[T](val self: Promise[T]) {
    def compose[S](f: S => T): Promise[S] = {
      val p = Promise[S]()

      p.future onComplete {
        case Success(x)             => self.trySuccess(f(x))
        case Failure(th: Throwable) => self.tryFailure(th)
      }

      p
    }
  }
}

object Ex8Test extends App {
  import Ex8._

  val p1: Promise[Int] = Promise[Int]()
  val p2: Promise[String] = p1.compose((s: String) => s.length)

  p2 trySuccess "abc"
  Thread.sleep(1000)
  println(p1.future.value)

//  p2.tryFailure(new Throwable(new Exception("error")))
//  Thread.sleep(1000)
//  println(p1.future.value)
}
