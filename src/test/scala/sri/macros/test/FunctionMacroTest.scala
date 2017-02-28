package sri.macros.test

import org.scalatest.FunSuite
import sri.macros.{
  OptionalParam,
  OptDefault,
  FunctionObjectMacro,
  rename,
  exclude
}

import scala.reflect.ClassTag
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSImport, ScalaJSDefined}
import scala.scalajs.js.{JSON, |}

class FunctionMacroTest extends BaseTest {

//  implicit def UnionEvidence[A: ClassTag, B: ClassTag](
//      ab: A | B)(implicit eva: A => js.Any, evb: B => js.Any): js.Any =
//    ab.asInstanceOf[js.Any]

  @ScalaJSDefined
  trait Address extends js.Object {
    var country: js.UndefOr[String] = js.undefined
  }

  object Address {
    def apply(country1: String) = new Address { country = country1 }
  }

  @ScalaJSDefined
  trait TestObj extends js.Object

  def Plain(name: String,
            category: String,
            peracre: OptionalParam[js.Any] = OptDefault): js.Object = {
    val p: js.Object = FunctionObjectMacro()
    println(JSON.stringify(p))
    p
  }

  def PlainKeys(@rename("custom_name") name: String,
                category: String,
                peracre: OptionalParam[Int] = OptDefault): js.Object = {
    val p = FunctionObjectMacro()
    p
  }

  def SeqTest(s: Seq[String] = Seq("dude"),
              as: Seq[Address] = Seq(Address("India"), null)): js.Object = {
    val p = FunctionObjectMacro()
    p
  }

  def SeqUndefTest(s: OptionalParam[Seq[String]] = Seq("dude"),
                   as: OptionalParam[Seq[Address]] =
                     Seq(Address("India"), null)): js.Object = {
    val p = FunctionObjectMacro()
    p
  }

  def SetTest(s: Set[String] = Set("dude"),
              as: OptionalParam[Set[Address]] = Set(Address("India"), null))
    : js.Object = {
    val p = FunctionObjectMacro()
    p
  }

  def ArrayTest(s: Array[String] = Array("dude"),
                as: OptionalParam[Array[Address]] =
                  Array(Address("India"), null)): js.Object = {
    val p = FunctionObjectMacro()
    p
  }

  def MapTest(
      m: Map[String, String] = Map("key" -> "0"),
      ma: OptionalParam[Map[String, Address]] =
        Map("address" -> Address("India"), "address2" -> null)): js.Object = {
    val p = FunctionObjectMacro()
    p
  }

  def JSDictTest(m: js.Dictionary[String] = js.Dictionary("key" -> "0"),
                 ma: OptionalParam[js.Dictionary[Address]] = js.Dictionary(
                   "address" -> Address("India"),
                   "address2" -> null)): js.Object = {
    val p = FunctionObjectMacro()
    p
  }

  def FunctionTest(fn0: () => Int = () => 5,
                   fn1: OptionalParam[Double => String] = (d: Double) =>
                     s"$d x"): js.Object = {
    val p = FunctionObjectMacro()
    p
  }

  def ExcludeTest(name: String = "Hello", @exclude age: Int = 1): js.Object = {
    val p = FunctionObjectMacro()
    p
  }

  def printResult(result: js.Any) = {
    println(s"Result is : ${JSON.stringify(result)}")
  }

  test("simple fields test") {
    val global = js.Dynamic.global
    println(global.document.body)
    val plain = (Plain("bpt", "rice")).asInstanceOf[js.Dynamic]
    assert(plain.name.toString == "bpt")
    assert(plain.category.toString == "rice")
    assert(!plain.asInstanceOf[js.Object].hasOwnProperty("peracre"))
  }

  test("simple fields test with custom field names") {
    val plain = PlainKeys("bpt", "rice").asInstanceOf[js.Dynamic]
    assert(plain.custom_name.toString == "bpt")
    assert(plain.category.toString == "rice")
    assert(!plain.asInstanceOf[js.Object].hasOwnProperty("peracre"))
  }

  test("should handle seq") {
    val result = SeqTest().asInstanceOf[js.Dynamic]
    assert(result.s.asInstanceOf[js.Array[String]].head == "dude")
    assert(
      result.as
        .asInstanceOf[js.Array[js.Dictionary[String]]]
        .head("country") == "India")

    val result2 = SeqUndefTest().asInstanceOf[js.Dynamic]
    assert(result2.s.asInstanceOf[js.Array[String]].head == "dude")
    assert(
      result2.as
        .asInstanceOf[js.Array[js.Dictionary[String]]]
        .head("country") == "India")
  }
  test("should handle sets") {
    val result = SetTest().asInstanceOf[js.Dynamic]
    assert(result.s.asInstanceOf[js.Array[String]].head == "dude")
    assert(
      result.as
        .asInstanceOf[js.Array[js.Dictionary[String]]]
        .head("country") == "India")
  }

  test("should handle arrays") {
    val result = ArrayTest().asInstanceOf[js.Dynamic]
    assert(result.s.asInstanceOf[js.Array[String]].head == "dude")
    assert(
      result.as
        .asInstanceOf[js.Array[js.Dictionary[String]]]
        .head("country") == "India")
  }

  test("should handle maps") {
    val result = MapTest().asInstanceOf[js.Dynamic]
    assert(result.m.asInstanceOf[js.Dictionary[String]].get("key").get == "0")
    assert(
      result.ma
        .asInstanceOf[js.Dictionary[js.Dynamic]]
        .get("address")
        .get
        .country
        .toString == "India")
  }

  test("should handle js.Dictionary") {
    val result = JSDictTest().asInstanceOf[js.Dynamic]
    assert(result.m.asInstanceOf[js.Dictionary[String]].get("key").get == "0")
    assert(
      result.ma
        .asInstanceOf[js.Dictionary[js.Dynamic]]
        .get("address")
        .get
        .country
        .toString == "India")
  }

  test("should handle functions") {
    val result = FunctionTest().asInstanceOf[js.Dynamic]
    assert(result.fn0.asInstanceOf[js.Function0[Int]]() == 5)
    assert(
      result.fn1.asInstanceOf[js.Function1[Double, String]](1.2) == "1.2 x")
  }

  test("should not include excluded fields") {
    val result = ExcludeTest().asInstanceOf[js.Dynamic]
    assert(result.name.toString == "Hello")
    assert(js.isUndefined(result.age))
  }

}
