package ch6

import rx.lang.scala._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Got Hug
 */
object Ex1 extends App {
  def log(msg: String): Unit =
    println(s"${Thread.currentThread.getName}: $msg")

//  val o = Observable.items("Pascal", "Java", "Scala")
//  o.subscribe(name => log(s"learned the $name language"))
//  o.subscribe(name => log(s"forgot the $name language"))

  @volatile var s = 0

  val monitor = Observable.create[Int] {
    obs =>
      Future {
        var sCur = s
        while (true) {
          Thread.sleep(200)
          if (s != sCur) {
            obs.onNext(s)
            sCur = s
          }
        }
      }

      Subscription()
  }

  monitor.subscribe(i => log(s"Value changed: $i"))

  s = 1
  Thread.sleep(1000)
  s = 2
  Thread.sleep(1000)
  s = 3
  Thread.sleep(1000)
  s = 2
  Thread.sleep(1000)
  s = 1
  Thread.sleep(1000)
}
