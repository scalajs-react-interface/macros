package sri.macros

import scala.reflect.ClassTag
import scala.scalajs.js
import scala.scalajs.js.{Any, UndefOr, UndefOrOps, |}
import scala.scalajs.js.|.Evidence

/**
  * https://github.com/scala-js/scala-js/issues/2714
  * @tparam A
  */
abstract sealed class OptionalParam[+A] {
  def foreach[U](f: A => U): Unit
}
case object OptDefault extends OptionalParam[Nothing] {
  def foreach[U](f: Nothing => U): Unit = ()
}
@inline final case class OptSpecified[+A](val get: A)
    extends OptionalParam[A] {
  def foreach[U](f: A => U): Unit = f(get)
}

sealed abstract class LowPriorityImplicits {
  implicit def any2undefOrUnion[A, B1, B2](a: A)(
      implicit ev: Evidence[A, B1 | B2]): OptionalParam[B1 | B2] = {
    OptSpecified(a).asInstanceOf[OptionalParam[B1 | B2]]
  }
}

object OptionalParam extends LowPriorityImplicits {
  implicit def specified[A](a: A): OptSpecified[A] = OptSpecified(a)

}
