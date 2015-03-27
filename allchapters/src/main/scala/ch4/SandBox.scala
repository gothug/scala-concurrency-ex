package ch4

import scala.concurrent.Future

/**
 * @author Vasily Kozhukhovskiy <vn.kozhukhovskiy@jet.msk.su>
 */
object SandBox extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  val f1 = Future { Thread.sleep(1000); println("f1"); "f1"; }

  val f2 = Future { Thread.sleep(1000); println("f2"); "f2"; }

//  val answer = for {
//    ff1 <- f1
//    ff2 <- f2
//  } yield {
//    "First, read this: " + ff1 + ". Now, try this: " + ff2
//  }

  val answer = f1.flatMap(ff1 => f2.map(ff2 => "First, read this: " + ff1 + ". Now, try this: " + ff2))

//  val answer = for {
//    ff1 <- Future { Thread.sleep(2000); println("f1"); "f1"; }
//    ff2 <- Future { Thread.sleep(2000); println("f2"); "f2"; }
//  } yield {
//    "First, read this: " + ff1 + ". Now, try this: " + ff2
//  }

  Thread.sleep(5000)

  answer foreach { case x => println(x) }
}
