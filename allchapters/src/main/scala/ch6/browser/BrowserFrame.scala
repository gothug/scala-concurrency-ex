package ch6.browser

import rx.lang.scala.{Subscription, Observable}

import scala.swing._
import scala.swing.event.{ValueChanged, ButtonClicked}

/**
 * @author Got Hug
 */
abstract class BrowserFrame extends MainFrame {
  implicit class ButtonOps(val self: Button) {
    def clicks = Observable.create[Unit] { obs =>
      self.reactions += {
        case ButtonClicked(_) => obs.onNext(())
      }
      Subscription()
    }
  }

  implicit class TextFieldOps(val self: TextField) {
    def texts = Observable.create[String] { obs =>
      self.reactions += {
        case ValueChanged(_) => obs.onNext(self.text)
      }
      Subscription()
    }
  }

  title = "MiniBrowser"

  val specUrl = "http://www.w3.org/Addressing/URL/url-spec.txt"
  val urlfield = new TextField(specUrl)
  val pagefield = new TextArea
  val button = new Button {
    text = "Feeling Lucky"
  }

  contents = new BorderPanel {
    import BorderPanel.Position._
    layout(new BorderPanel {
      layout(new Label("URL:")) = West
      layout(urlfield) = Center
      layout(button) = East
    }) = North
    layout(pagefield) = Center
  }

  size = new Dimension(1024, 768)
}

