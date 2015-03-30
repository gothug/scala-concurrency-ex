package ch4

import scala.concurrent.Promise
import scala.util.Success

/**
 * @author Vasily Kozhukhovskiy <vn.kozhukhovskiy@jet.msk.su>
 */

//Ex 2
class IVar[T] {
  var s: Option[T] = None

  val p = Promise[T]()

  def apply(): T = p.future.value match {
    case Some(Success(x)) => x
    case _                => throw new Exception("no value")
  }

  def :=(x: T): Unit = {
    try {
      p.success(x)
    } catch {
      case e: Throwable =>
        throw new Exception("already assigned")
    }
  }
}

object IVarTest extends App {
  val iVar = new IVar[Int]()
  iVar := 5
  println(iVar.apply())
}
