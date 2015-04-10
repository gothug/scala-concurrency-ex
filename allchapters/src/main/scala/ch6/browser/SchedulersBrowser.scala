package ch6.browser

import scala.swing.SimpleSwingApplication

/**
 * @author Got Hug
 */
object SchedulersBrowser extends SimpleSwingApplication {
  def top = new BrowserFrame with BrowserLogic
}
