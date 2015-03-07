package ch2

import scala.collection.mutable
import util.Util._

/**
 * @author Got Hug
 */
class SyncQueue[T](n: Int) {
  var queue: mutable.Queue[T] = mutable.Queue()

  val lock = new AnyRef

  def getWait: T = {
    lock.synchronized {
      while (queue.isEmpty) lock.wait()

      val t = queue.dequeue()

      lock.notifyAll()
      t
    }
  }

  def putWait(x: T): Unit = {
    lock.synchronized {
      while (queue.size == n) lock.wait()

      queue enqueue x

      lock.notifyAll()
    }
  }
}

object SyncQueue extends App {
  for (i <- 1 until 1000) {
    println(s"$i:\n")
    numbersTranferEx6()
    println("\n")
  }

  def numbersTranferEx6() = {
    val store = new SyncQueue[Int](5)

    val producer1 = thread("Producer-1") {
      for (i <- 0 until 100) {
        store.putWait(i)
      }
    }

    val producer2 = thread("Producer-2") {
      for (i <- 100 until 200) {
        store.putWait(i)
      }
    }

    val consumer = thread("Consumer-1") {
      def consume(cnt: Int): Unit = {
        val i = store.getWait

        println(i)

        if (store.queue.size > 5) {
          throw new Exception("Queue size exceeded")
        }

        if (cnt < 200) {
          consume(cnt + 1)
        }
      }

      consume(1)
    }

    producer1.join()
    producer2.join()
    consumer.join()
  }
}
