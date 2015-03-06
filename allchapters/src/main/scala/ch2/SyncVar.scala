package ch2

import util.Util._

/**
 * @author Got Hug
 */
class SyncVar[T] {
  var value: Option[T] = None

  val lock = new AnyRef

  def get: T = {
    lock.synchronized {
      getVal
    }
  }

  def put(x: T): Unit = {
    lock.synchronized {
      putVal(x)
    }
  }

  def isEmpty: Boolean = {
    lock.synchronized {
      value match {
        case Some(_) => false
        case _       => true
      }
    }
  }

  def nonEmpty: Boolean = {
    !isEmpty
  }

  private def getVal: T = value match {
    case Some(t) =>
      value = None
      t
    case None =>
      throw new Exception("Object is empty")
  }

  private def putVal(x: T): Unit = value match {
    case Some(_) =>
      throw new Exception("Put to non-empty object")
    case None =>
      value = Some(x)
  }
}

object SyncVar extends App {
  numbersTranferEx4()

  def numbersTranferEx4() = {
    val store = new SyncVar[Int]

    val producer = thread {
      def produce(i: Int): Unit = {
        if (store.isEmpty) {
          store.put(i)
        } else {
          produce(i)
        }
      }

      for (i <- 0 until 15) {
        produce(i)
      }
    }

    val consumer = thread {
      def consume(): Unit = {
        if (store.isEmpty) {
          consume()
        } else {
          val i = store.get
          println(i)

          if (i < 14) {
            consume()
          }
        }
      }

      consume()
    }

    producer.join()
    consumer.join()
  }
}
