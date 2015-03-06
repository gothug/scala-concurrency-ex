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
    lockGet.synchronized {
//      println("getWait got lock..")
      while (isEmpty) lockGet.wait()

      val t = value.get
      value = None

      lockPut.notify()
//      println(s"getWait got value: $t")
      t
    }
  }

  def putWait(x: T)(n: Int): Unit = {
    lockPut.synchronized {
//      println(s"putWait($n) got lock..")
      while (nonEmpty) lockPut.wait()

      value = Some(x)

      lockGet.notify()
//      println(s"putWait($n) put value: $x")
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
  numbersTranferEx5()

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

    val producer1 = thread {
      for (i <- 0 until 10) {
        store.putWait(i)(1)
      }
    }

//    val producer2 = thread {
//      for (i <- 10 until 20) {
//        store.putWait(i)(2)
//      }
//    }

    val consumer = thread {
      def consume(cnt: Int): Unit = {
        val i = store.getWait

        println(i)

        if (cnt < 10) {
          consume(cnt + 1)
        }
      }

      consume(1)
    }

    producer1.join()
//    producer2.join()
    consumer.join()
  }
}
