package ch3

import java.util.concurrent.atomic.AtomicReference

/**
 * @author Got Hug
 */
class ConcurrentSortedList[T](implicit val ord: Ordering[T]) {

  trait Element
  case class Node(el: T, next: AtomicReference[Element]) extends Element
  case object End extends Element

  private val head = new AtomicReference[Element](End)

  def add(x: T): Unit = {
    def loop(el: Element) = el match {
      case End =>


    }

    loop(head)
  }

//  def iterator: Iterator[T] = ???
}
