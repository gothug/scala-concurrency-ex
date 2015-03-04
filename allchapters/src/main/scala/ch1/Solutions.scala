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
    type Chars = Vector[Char]

    def loop(combs: Vector[(Chars, Chars)]): Vector[Chars] = {
      val acc: Vector[(Chars, Chars)] =
        for {
          (c1, c2) <- combs
          c <- c2
        } yield (c1 :+ c, c2 filter (_ != c))

      acc.head._2 match {
        case IndexedSeq() => acc.map(_._1)
        case _            => loop(acc)
      }
    }

    (loop(Vector((Vector(), x.toVector))) map { _.mkString }).toSeq
  }

  def permutations3(x: String): Seq[String] = {
    def loop(xs: Stream[Char]): Stream[Stream[Char]] = {
      if (xs.isEmpty) Stream(Stream())
      else {
        for {
          e <- xs
          es <- loop(xs filter (_ != e))
        } yield Stream.cons(e, es)
      }
    }

    (loop(x.toStream) map { _.mkString }).toSeq
  }
}
