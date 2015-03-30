package ch4

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @author Vasily Kozhukhovskiy <vn.kozhukhovskiy@jet.msk.su>
 */
object Ex3 {
  implicit class FutureOps[T](val self: Future[T]) {
    def exists(p: T => Boolean): Future[Boolean] = {
      if (self.isCompleted) {
        self map { x => p(x) }
      } else {
        Future { false }
      }
    }
  }
}

object Ex3Test extends App {
  import Ex3._

  val f1: Future[Int] = Future { 1 }
  Thread.sleep(1000)
  f1.exists(_ > 0) foreach {println(_)}

  val f2: Future[Int] = Future { Thread.sleep(100); 1 }
  f2.exists(_ > 0) foreach {println(_)}
  Thread.sleep(200)

  val f3: Future[Int] = Future { Thread.sleep(100); -1 }
  f3.exists(_ > 0) foreach {println(_)}
  Thread.sleep(200)
}
