package ch1

/**
 * @author Got Hug
 */
object Solutions extends App {
  def compose[A, B, C](g: B => C, f: A => B): A => C = {
    x => g(f(x))
  }

  def fuse[A, B](a: Option[A], b: Option[B]): Option[(A, B)] = {
    for {
      aa <- a
      bb <- b
    } yield (aa, bb)
  }
}

//object Solutions extends App {
//  println("abc")
//  assert(Solutions.fuse(None, None) == None)

//  fuse(Some(1), Some(2))
//}
