package ch3

/**
 * @author Got Hug
 */
class LazyCell[T](initialization: =>T) {
  def apply(): T = initialization
}

object LazyCell extends App {
  def sum = {
    println("sum")
    1 + 1
  }

  val cell = new LazyCell[Int](sum)
  println(cell.apply())
}
