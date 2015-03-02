package ch1

import scala.util.{Success, Try}

/**
 * @author Got Hug
 */
object Solutions {
  def compose[A, B, C](g: B => C, f: A => B): A => C = {
    x => g(f(x))
  }

  def fuse[A, B](a: Option[A], b: Option[B]): Option[(A, B)] = {
    for {
      aa <- a
      bb <- b
    } yield (aa, bb)
  }

  def fuseViaMap[A, B](a: Option[A], b: Option[B]): Option[(A, B)] = {
    a.flatMap(aa => b.map(bb => (aa, bb)))
  }

  def check[T](xs: Seq[T])(pred: T => Boolean): Boolean = {
    def loop(xsxs: Seq[T]): Boolean = xsxs match {
      case h +: t => if (Try(pred(h)) == Success(true)) loop(t) else false
      case _      => true
    }

    loop(xs)
  }

  def permutations(x: String): Seq[String] = {
    def loop(xs: List[Char]): List[List[Char]] = {
      if (xs.isEmpty) List(List())
      else {
        for {
          e <- xs
          es <- loop(xs filter (_ != e))
        } yield e :: es
      }
    }

    (loop(x.toList) map { _.mkString }).toSeq
  }

  def permutations2(x: String): Seq[String] = {
    def loop(xs: List[Char]): List[List[Char]] = {
      if (xs.isEmpty) List(List())
      else {
        for {
          e <- xs
          es <- loop(xs filter (_ != e))
        } yield e :: es
      }
    }

    (loop(x.toList) map { _.mkString }).toSeq
  }
}
