package ch4

import java.util._
import scala.collection.immutable.List

import scala.concurrent.{Promise, Future}
import scala.io.Source
//import collection.JavaConversions._

/**
 * @author Vasily Kozhukhovskiy <vn.kozhukhovskiy@jet.msk.su>
 */
object Ex1 extends App {

  implicit class FutureOps[T](val self: Future[T]) {
    def or(that: Future[T]): Future[T] = {
      val p = Promise[T]()
      self onComplete { case x => p tryComplete x }
      that onComplete { case y => p tryComplete y }
      p.future
    }
  }

  private val timer = new Timer(true)

//  val url = scala.io.StdIn.readLine("Enter url please:\n")
  val url = "http://tools.ietf.org/rfc/rfc4357.txt"


//  println(contentLines.mkString("\n"))

  def timeout(t: Long): Future[Unit] = {
    val p = Promise[Unit]()
    timer.schedule(new TimerTask {
      def run() = {
        p success ()
        timer.cancel()
      }
    }, t)
    p.future
  }

  def getContentLines(url: String): Future[List[String]] = Future {
    val f = Source.fromURL(url, "iso-8859-1")
    try f.getLines().toList finally f.close()
  }
}
