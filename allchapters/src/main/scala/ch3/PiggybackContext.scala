package ch3

import scala.concurrent._
import scala.util.{Failure, Try}

/**
 * @author Got Hug
 */
class PiggybackContext extends ExecutionContext {
  def execute(r: Runnable): Unit = {
    Try(
      r.run()
    ) match {
      case Failure(throwable) => reportFailure(throwable)
      case _                  =>
    }
  }

  override def reportFailure(cause: Throwable): Unit = {
    println("Error occurred: " + cause.getMessage)
  }
}

object PiggybackContext extends App {
  val pc = new PiggybackContext

  val rIn = new Runnable {
    override def run(): Unit = {
      Thread.sleep(3000)
      println("Inner Runnable..")
      throw new Exception("Inner Runnable exception")
    }
  }

  val rOut = new Runnable {
    override def run(): Unit = {
      Thread.sleep(3000)
      println("Outer Runnable..")

      pc.execute(rIn)
    }
  }

  pc.execute(rOut)

  println("Finished")
}
