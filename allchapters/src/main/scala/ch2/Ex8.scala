package ch2

import scala.collection.mutable

/**
 * @author Got Hug
 */
class PriorityTaskPool(n: Int) {
  case class PrioritizedTask(priority: Int, task: () => Unit)

  private val tasks = new mutable.PriorityQueue[PrioritizedTask]()(Ordering.by(_.priority))

  for (i <- 1 to n) {
    val name = s"Worker - $i"

    println(s"Starting $name")

    val worker = new Thread {
      def poll(): Option[() => Unit] = tasks.synchronized {
        if (tasks.nonEmpty) Some(tasks.dequeue().task) else None
      }

      override def run() = while (true) poll() match {
        case Some(task) => task()
        case None =>
      }
    }

    worker.setName(name)
    worker.setDaemon(true)
    worker.start()
  }

  def asynchronous(priority: Int)(task: =>Unit): Unit = tasks.synchronized {
    tasks enqueue PrioritizedTask(priority, () => task)
  }
}

object Ex8 extends App {
  val taskPool = new PriorityTaskPool(5)

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
  println("Finished")
}
