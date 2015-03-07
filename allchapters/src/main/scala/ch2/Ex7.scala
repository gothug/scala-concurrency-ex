package ch2

import util.Util._

/**
 * @author Got Hug
 */
object Ex7 {
  case class Account(name: String, var money: Int)

  def sendAll(accounts: Set[Account], target: Account) = {
    def deplete(a: Account) = {
      target.money += a.money
      a.money = 0
    }

    for (a <- accounts) {
      a.synchronized {
        target.synchronized {
          deplete(a)
        }
      }
    }
  }
}

object Ex7Test extends App {
  import Ex7._

  for (i <- 1 to 100) {
    println("Test")
    transfer()
    println("\n")
  }

  def transfer() = {
    val accounts =
      for {
        i <- 1 to 50
      } yield Account(s"Acc$i", 100)

    val tasks =
      for {
        i <- 1 to 20

        targetAccount = Account("Startup", 0)

        t = thread {
          sendAll(accounts.toSet, targetAccount)
        }
      } yield (t, targetAccount)

    tasks map { case (t, acc) => t.join() }

    tasks map { case (t, acc) => println(acc) }
  }
}
