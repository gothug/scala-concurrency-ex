package ch4

import scala.concurrent.{Future, blocking}
import scala.sys.process._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @author Vasily Kozhukhovskiy <vn.kozhukhovskiy@jet.msk.su>
 */
object Ex6 {
  def spawn(command: String): Future[Int] = Future {
    blocking {
      command.!
    }
  }
}

object Ex6Test extends App {
  import Ex6._

  val f = spawn("sleep 2")

  f.foreach(println(_))

  Thread.sleep(2500)
}
