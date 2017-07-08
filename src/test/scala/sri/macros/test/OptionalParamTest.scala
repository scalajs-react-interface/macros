package sri.macros.test

import sri.macros.{OptDefault, OptSpecified, OptionalParam}

import scala.scalajs.js
import scala.scalajs.js.{UndefOr, |}

class OptionalParamTest extends BaseTest {
  test(
    "should plain values",
    () => {
      def fun(value: OptionalParam[Int] = OptDefault) = value

      expect(fun(2)).toEqual(OptSpecified(2).asInstanceOf[js.Any])
      expect(fun()).toBe(OptDefault)
    }
  )

  test(
    "should handle Unions",
    () => {
      def fun(value: OptionalParam[Int | String] = OptDefault) = value

      expect(fun(2)).toEqual(OptSpecified(2).asInstanceOf[js.Any])
      expect(fun()).toBe(OptDefault)
    }
  )

  test(
    "should handle UndefOr conversions",
    () => {
      def fun(value: OptionalParam[Int] = OptDefault) = value

      expect(fun(2)).toEqual(OptSpecified(2).asInstanceOf[js.Any])
      expect(fun(js.undefined)).toBe(OptDefault)
    }
  )

  test(
    "should handle UndefOr Union conversions",
    () => {
      trait AA
      case class A(v1: String) extends AA
      case object B
      def fun(value: OptionalParam[AA | B.type] = OptDefault) = value

      val definedA: UndefOr[A] = A("hello")
      val definedB: UndefOr[A | B.type] = B
      val undefinedB: UndefOr[A | B.type] = js.undefined
      expect(fun(A("hello")))
        .toEqual(OptionalParam.specified(A("hello")).asInstanceOf[js.Any])
      expect(fun(js.undefined)).toBe(OptDefault)
      expect(fun(definedA))
        .toEqual(OptSpecified(A("hello")).asInstanceOf[js.Any])
      expect(fun(definedB)).toEqual(OptSpecified(B).asInstanceOf[js.Any])
      expect(fun(undefinedB)).toBe(OptDefault)
    }
  )
}
