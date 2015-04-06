package ch4

import java.util._

import scala.collection.immutable.List
import scala.concurrent.{CancellationException, Promise, Future}
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @author Got Hug
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

  type Cancellable[T] = (Promise[Unit], Future[T])

  def cancellable[T](b: Future[Unit] => T): Cancellable[T] = {
    val cancel = Promise[Unit]()
    val f = Future {
      val r = b(cancel.future)
      if (!cancel.tryFailure(new Exception))
        throw new CancellationException
      r
    }
    (cancel, f) }

  private val timer = new Timer(true)

//  val url = scala.io.StdIn.readLine("Enter url please:\n")
  val url = "http://tools.ietf.org/rfc/rfc4357.txt"

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

  def getContentLines(url: String, cancel: Promise[Unit]): Future[List[String]] = Future {
    Thread.sleep(3000)
    val f = Source.fromURL(url, "iso-8859-1")
    val lines = try f.getLines().toList finally f.close()

    cancel trySuccess ()

    lines
  }

  def progressBar(): Promise[Unit] = {
    // progress bar
    val (cancel, future) =
      cancellable {
        cl: Future[Unit] =>
          while (!cl.isCompleted) {
            print('.')
            Thread.sleep(400)
          }
      }

    cancel
  }

  val cancel = progressBar()

  val f: Future[List[String]] =
    timeout(5000).map {
      _ =>
        cancel trySuccess ()
        List[String]("timeout")
    } or getContentLines(url, cancel)

  f foreach {
    case l: List[String] => println(l.mkString("\n"))
  }

  Thread.sleep(6000)
}
