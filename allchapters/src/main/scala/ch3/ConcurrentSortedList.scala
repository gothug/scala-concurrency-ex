package ch3

import java.util.concurrent.atomic.AtomicReference
import scala.concurrent.forkjoin.ForkJoinPool

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
        case Node(value, next) if ord.gt(x, value) =>
          loop(next)
        case _ =>
          val newElement = Node(x, new AtomicReference[Element](element))
          if (!ref.compareAndSet(element, newElement)) loop(ref)
      }
    }

    loop(head)
  }

  def iterator: Iterator[T] = {
    new Iterator[T] {
      var ptr: Element = head.get

      def hasNext = {
        ptr match {
          case End => false
          case _   => true
        }
      }

      def next(): T = {
        ptr match {
          case Node(value, next) =>
            ptr = next.get
            value
          case End =>
            throw new Exception("empty iterator")
        }
      }
    }
  }
}

object ConcurrentSortedList extends App {
  val list = new ConcurrentSortedList[Int]

  val pool = new ForkJoinPool(16)

  for (i <- 1 to 16) {
    val r = new Runnable {
      override def run(): Unit = {
        for (i <- 1 to 1000) {
          list.add(scala.util.Random.nextInt(100))
        }
//        println("run")
//        Thread.sleep(1000)
      }
    }

    pool.execute(r)
//    ExecutionContext.global.execute(r)
  }

  Thread.sleep(5000)

  val nums = list.iterator.toList

  println(nums.size)
  assert(nums == nums.sorted)
}
