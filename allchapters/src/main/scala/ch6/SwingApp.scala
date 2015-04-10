package ch6

import rx.lang.scala.{Observable, Subscription}

import scala.swing._
import scala.swing.event._

/**
 * @author Got Hug
 */

object SwingApp extends SimpleSwingApplication {
  def log(msg: String): Unit =
    println(s"${Thread.currentThread.getName}: $msg")

  def top = new MainFrame {
    title = "Swing Observables"

    val button = new Button {
      text = "Click"
    }

    contents = button

    val buttonClicks = Observable.create[Button] { obs =>
      button.reactions += {
        case ButtonClicked(_) => obs.onNext(button)
      }
      Subscription()
    }

    buttonClicks.subscribe(_ => { Thread.sleep(1000); log("button clicked"); })
  }
}
