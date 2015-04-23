package ch6

import rx.lang.scala.{Subscription, Observable}

/**
 * @author Vasily Kozhukhovskiy <vn.kozhukhovskiy@jet.msk.su>
 */
object Ex4 extends App {
  def log(msg: String): Unit =
    println(s"${Thread.currentThread.getName}: $msg")

  implicit class ObservableExtend[T](val self: Observable[T]) {
    def toSignal: Signal[T] = {
      new Signal[T](self)
    }
  }

  val obs = Observable.create[Int] {
    obs =>
      obs.onNext(1)
      obs.onNext(2)
      obs.onNext(3)
      Subscription()
  }

  val signal: Signal[Int] = obs.toSignal

  println(signal.apply())
  println(signal.map(x => List(x)).apply())
}

class Signal[T](val obs: Observable[T]) {
  var lastEvent: Option[T] = None

  obs.subscribe(x => lastEvent = Some(x))

  def apply(): T = lastEvent.get

  def map[S](f: T => S): Signal[S] = {
    new Signal[S](obs.map(x => f(x)))
  }

  def zip[S](that: Signal[S]): Signal[(T, S)] = {
    val newObs: Observable[(T, S)] =
      for {
        t <- obs
        s <- that.obs
      } yield (t, s)

    new Signal[(T, S)](newObs)
  }
}
