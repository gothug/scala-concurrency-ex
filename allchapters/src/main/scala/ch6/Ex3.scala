package ch6

import rx.lang.scala.{Subscription, Observable}

import scala.io.Source

/**
 * @author Got Hug
 */
object Ex3 extends App {
  def log(msg: String): Unit =
    println(s"${Thread.currentThread.getName}: $msg")

  def randomQuote = Observable.create[String] { obs =>
    val url = "http://www.iheartquotes.com/api/v1/random?" +
      "show_permalink=false&show_source=false"
    obs.onNext(Source.fromURL(url).getLines().mkString)
    obs.onCompleted()
    Subscription()
  }

  randomQuote.repeat(10).scan(0.0) {
    (n, q) => (n + q.length) / 2
  } subscribe(x => log(x.toString))
}
