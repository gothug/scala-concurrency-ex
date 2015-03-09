package ch2

import scala.annotation.tailrec
import scala.collection.mutable

/**
 * @author Got Hug
 */
class PriorityTaskPool(n: Int, important: Int) {
  case class PrioritizedTask(priority: Int, task: () => Unit)

  private val tasks = new mutable.PriorityQueue[PrioritizedTask]()(Ordering.by(_.priority))

  var terminated = false

  for (i <- 1 to n) {
    val name = s"Worker - $i"

    println(s"Starting $name")

    val worker = new Thread {
      def poll(): Option[() => Unit] = tasks.synchronized {
        while (tasks.isEmpty && !terminated) tasks.wait()

        if (tasks.isEmpty) {
          None
        }
        else {
          val task = tasks.dequeue()

          if (!terminated || task.priority >= important) {
            Some(task.task)
          } else {
            None
          }
        }
      }

      @tailrec
      override def run() = poll() match {
        case Some(task) => task(); run()
        case None =>
      }
    }

    worker.setName(name)
//    worker.setDaemon(true)
    worker.start()
  }

  def shutdown() = tasks.synchronized {
    terminated = true
    tasks.notifyAll()
  }

  def asynchronous(priority: Int)(task: =>Unit): Unit = tasks.synchronized {
    tasks enqueue PrioritizedTask(priority, () => task)
    tasks.notifyAll()
  }
}

object Ex8 extends App {
  val taskPool = new PriorityTaskPool(5, 100)

  println("Started")
  taskPool.asynchronous(1000) { println("Warm up task"); Thread.sleep(2000) }
  taskPool.asynchronous(1000) { println("Warm up task"); Thread.sleep(2000) }
  taskPool.asynchronous(1000) { println("Warm up task"); Thread.sleep(2000) }
  taskPool.asynchronous(1000) { println("Warm up task"); Thread.sleep(2000) }
  taskPool.asynchronous(1000) { println("Warm up task"); Thread.sleep(2000) }

  taskPool.asynchronous(1) { println("Hello 1") }
  taskPool.asynchronous(100) { println("Hello 100") }
  taskPool.asynchronous(2) { println("Hello 2") }
  taskPool.asynchronous(3) { println("Hello 3") }
  taskPool.asynchronous(4) { println("Hello 4") }

  Thread.sleep(3000)
  taskPool.shutdown()
  println("Finished")
}

object Ex10 extends App {
  val taskPool = new PriorityTaskPool(3, 100)

  println("Started")
  taskPool.asynchronous(1000) { println("Warm up task"); Thread.sleep(2000) }
  taskPool.asynchronous(1000) { println("Warm up task"); Thread.sleep(2000) }
  taskPool.asynchronous(1000) { println("Warm up task"); Thread.sleep(2000) }

  taskPool.asynchronous(1) { println("Hello 1") }
  taskPool.asynchronous(150) { println("Hello 150") }
  taskPool.asynchronous(2) { println("Hello 2") }
  taskPool.asynchronous(150) { println("Hello 150") }
  taskPool.asynchronous(3) { println("Hello 3") }
  taskPool.asynchronous(150) { println("Hello 150") }
  taskPool.asynchronous(4) { println("Hello 4") }

  taskPool.shutdown()
  println("Finished")
}
