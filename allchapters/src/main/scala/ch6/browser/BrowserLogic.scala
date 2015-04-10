package ch6.browser

import java.util.concurrent.Executor
import javax.swing.SwingUtilities.invokeLater

import rx.lang.scala.{Observable, Scheduler}
import rx.schedulers.Schedulers.{from => fromExecutor}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * @author Got Hug
 */
trait BrowserLogic {
  self: BrowserFrame =>

  val swingScheduler = new Scheduler {
    val asJavaScheduler = fromExecutor(new Executor {
      def execute(r: Runnable) = invokeLater(r)
    })
  }

  def suggestRequest(term: String): Observable[String] = {
    val url = "http://suggestqueries.google.com/" +
      s"complete/search?client=firefox&q=$term"
    val request = Future { Source.fromURL(url).mkString }
    Observable.from(request).timeout(0.5.seconds).onErrorReturn(e => "(no suggestion)")
  }

  def pageRequest(url: String): Observable[String] = {
    val request = Future { Source.fromURL(url).mkString }
    Observable.from(request)
      .timeout(4.seconds)
      .onErrorReturn(e => s"Could not load page: $e")
  }

  urlfield.texts.map(suggestRequest).concat.observeOn(swingScheduler)
    .subscribe(response => pagefield.text = response)

  button.clicks.map(_ => pageRequest(urlfield.text)).concat.observeOn(swingScheduler)
    .subscribe(response => pagefield.text = response)
}
