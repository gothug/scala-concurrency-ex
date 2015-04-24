package ch8

import akka.actor.{Props, ActorRef, ActorSystem, Actor}
import akka.event.Logging
import ch8.TimerActor.Register

/**
 * @author Got Hug
 */
object Ex1 extends App {
  lazy val ourSystem = ActorSystem("OurExampleSystem")

  val timerActor: ActorRef =
    ourSystem.actorOf(Props[TimerActor], name = "timer")

  timerActor ! Register(5000)
  timerActor ! Register(5000)
  timerActor ! Register(5000)

  println("waiting")
  Thread.sleep(16000)
  ourSystem.shutdown()
}

class TimerActor extends Actor {
  import TimerActor._

  val log = Logging(context.system, this)

  def receive = {
    case Register(timeout) =>
      log.info("Got Register msg")
      Thread.sleep(timeout)
      sender ! Timeout
  }
}

object TimerActor {
  case class Register(t: Int)
  case object Timeout
}
