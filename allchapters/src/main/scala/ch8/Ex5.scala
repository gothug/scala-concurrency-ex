package ch8

import akka.actor._
import akka.event.Logging
import akka.util.Timeout

import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * @author Got Hug
 */
object Ex5 extends App {
  lazy val ourSystem = ActorSystem("OurExampleSystem")

  val mainActor: ActorRef =
    ourSystem.actorOf(Props[MainActor], name = "main")

  println("Main actor created..")
  Thread.sleep(10000)

  println("Stopping main actor")
  ourSystem.stop(mainActor)
  Thread.sleep(1000)

  ourSystem.shutdown()
}

object MainActor {
  case object Stop
}

class MainActor extends Actor {
  val log = Logging(context.system, this)

  var failureDetector: ActorRef = _

  var child1: ActorRef = _
  var child2: ActorRef = _
  var child3: ActorRef = _

  override def preStart(): Unit = {
    log.info("about to start")

    child1 = context.actorOf(Props[ChildActor], "child1")
    child2 = context.actorOf(Props[ChildActor], "child2")
    child3 = context.actorOf(Props[ChildActor], "child3")

    child2 ! MainActor.Stop

    failureDetector = context.actorOf(Props(classOf[FailureDetector], "../child*"), "failureDetector")
  }

  def receive = {
    case msg =>
  }

  override def postStop(): Unit = {
    log.info("main actor stopping")
    context.stop(failureDetector)
    super.postStop()
  }
}

class ChildActor extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case MainActor.Stop => context.stop(self)
  }
}

class FailureDetector(selection: String) extends Actor {
  val log = Logging(context.system, this)

  implicit val timeout: Timeout = 5 second

  def askForIdentity() = {
    log.info(s"Asking for identity of actor selection $selection")

    val as: ActorSelection = context.actorSelection(selection)
    as ! Identify(scala.util.Random.nextInt(100))
  }

  val cancellable =
    context.system.scheduler.schedule(0 milliseconds, 2000 milliseconds)(askForIdentity())(context.system.dispatcher)

  def receive = {
    case ActorIdentity(ci, ref) => log.info(s"Got actor identity reply, corr id $ci, ref $ref")
  }

  override def postStop(): Unit = {
    log.info("failure detector actor stopping")
    cancellable.cancel()
    super.postStop()
  }
}
