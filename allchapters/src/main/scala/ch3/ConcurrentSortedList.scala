package ch3

import java.util.concurrent.atomic.AtomicReference

/**
 * @author Got Hug
 */
class ConcurrentSortedList[T](implicit val ord: Ordering[T]) {

  trait Element
  case class Node(value: T, next: AtomicReference[Element]) extends Element
  case object End extends Element

  private val head = new AtomicReference[Element](End)

  def add(x: T): Unit = {
    def loop(ref: AtomicReference[Element]): Unit = {
      val element = ref.get

      element match {
        case Node(value, next) =>
          if (x > value) {
            loop(next)
          } else {
            val newElement = Node(x, new AtomicReference[Element](next.get))
            if (!ref.compareAndSet(element, newElement)) loop(ref)
          }
      }
    }

    loop(head)
  }

//  def iterator: Iterator[T] = ???
}
