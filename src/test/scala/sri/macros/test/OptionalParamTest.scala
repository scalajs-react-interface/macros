package sri.macros.test

import org.scalatest.FunSuite
import sri.macros.{OptDefault, OptSpecified, OptionalParam}

import scala.scalajs.js
import scala.scalajs.js.{UndefOr, |}

class OptionalParamTest extends FunSuite {
  test("should plain values") {
    def fun(value: OptionalParam[Int] = OptDefault) = value

    assert(fun(2) === OptSpecified(2))
    assert(fun() === OptDefault)
  }

  test("should handle Unions") {
    def fun(value: OptionalParam[Int | String] = OptDefault) = value

    assert(fun(2) === OptSpecified(2))
    assert(fun() === OptDefault)
  }

  test("should handle UndefOr conversions") {
    def fun(value: OptionalParam[Int] = OptDefault) = value

    assert(fun(js.undefined) === OptDefault)
    assert(fun(2) === OptSpecified(2))
  }

  test("should handle UndefOr Union conversions") {
    trait AA
    case class A(v1: String) extends AA
    case object B
    def fun(value: OptionalParam[AA | B.type] = OptDefault) = value

    val definedA: UndefOr[A] = A("hello")
    val definedB: UndefOr[A | B.type] = B
    val undefinedB: UndefOr[A | B.type] = js.undefined
    assert(fun(A("hello")) === OptionalParam.specified(A("hello")))
    assert(fun(js.undefined) === OptDefault)
    assert(fun(definedA) === OptSpecified(A("hello")))
    assert(fun(definedB) === OptSpecified(B))
    assert(fun(undefinedB) === OptDefault)
    assertDoesNotCompile("fun(\"hello\")")
  }
}
