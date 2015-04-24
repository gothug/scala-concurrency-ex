package ch6

import rx.lang.scala.Observable

import scala.concurrent.duration._

/**
 * @author Got Hug
 */
object Ex2 extends App {
  def log(msg: String): Unit =
    println(s"${Thread.currentThread.getName}: $msg")

  val o = Observable.interval(1.seconds).filter(x => ((x % 5 == 0) || (x % 12 == 0)) && (x % 30 != 0)).map(x => s"num $x")

  o.subscribe(x => log(x))

  println("Sleeping..")
  Thread.sleep(60000)
}
