package ch2

import util.Util._

/**
 * @author Got Hug
 */
class SyncVar[T] {
  var value: Option[T] = None

  val lock = new AnyRef

  val lockPut = new AnyRef
  val lockGet = new AnyRef

  def getWait: T = {
    lock.synchronized {
      while (isEmpty) lock.wait()

      val t = value.get
      value = None

      lock.notifyAll()
      t
    }
  }

  def putWait(x: T)(n: Int): Unit = {
    lock.synchronized {
      while (nonEmpty) lock.wait()

      value = Some(x)

      lock.notifyAll()
    }
  }

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
    value match {
      case Some(_) => false
      case _       => true
    }
  }

  def nonEmpty: Boolean = {
    !isEmpty
  }

  def isEmptySync: Boolean = {
    lock.synchronized {
      value match {
        case Some(_) => false
        case _       => true
      }
    }
  }

  def nonEmptySync: Boolean = {
    !isEmptySync
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
//  numbersTranferEx4()
  for (i <- 1 until 1000) {
    println(s"$i:\n")
    numbersTranferEx5()
    println("\n")
  }

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

  def numbersTranferEx5() = {
    val store = new SyncVar[Int]

    val producer1 = thread("Producer-1") {
      for (i <- 0 until 10) {
        store.putWait(i)(1)
      }
    }

    val producer2 = thread("Producer-2") {
      for (i <- 10 until 20) {
        store.putWait(i)(2)
      }
    }

    val consumer = thread("Consumer-1") {
      def consume(cnt: Int): Unit = {
        val i = store.getWait

        println(i)

        if (cnt < 20) {
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
