package ch6

import rx.lang.scala.{Observer, Subscription, Observable}
import scala.collection.concurrent.TrieMap

/**
 * @author Got Hug
 */
object Ex6 extends App {
  def log(msg: String): Unit =
    println(s"${Thread.currentThread.getName}: $msg")

  val rMap = new RMap[Int, String]()

  rMap.update(0, "a")
  rMap.update(1, "b")
  rMap.update(2, "c")

  rMap.apply(2).subscribe(x => log(x))

  rMap.update(2, "e")
  rMap.update(2, "f")
  rMap.update(2, "g")
}

class RMap[K, V] {
  val map = new TrieMap[K, V]()
  val observersMap = new TrieMap[K, Observer[V]]()

  def update(k: K, v: V): Unit = {
    map += (k -> v)
    if (observersMap.isDefinedAt(k)) {
      observersMap(k).onNext(v)
    }
  }

  def apply(k: K): Observable[V] =
    Observable.create[V] { obs: Observer[V] =>
      observersMap += (k -> obs)
      Subscription()
    }
}

