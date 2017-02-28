package sri.macros.test

import org.scalactic.source.Position
import org.scalatest._

import scala.scalajs.js
import scala.scalajs.js.JavaScriptException

class BaseTest extends FunSuite with BeforeAndAfter with BeforeAndAfterAll {

  var domRegister: js.Function0[_] = null

  override protected def beforeAll(): Unit = {
    domRegister = JSDOMGlobal()
  }

  override protected def afterAll(): Unit = {
    domRegister() // cleanup
  }
  override protected def test(testName: String, testTags: org.scalatest.Tag*)(
      testFun: => Any)(implicit pos: Position) = {
    super.test(testName, testTags: _*)(
      try testFun
      catch {
        case jse @ JavaScriptException(e) =>
          println(e)
          jse.printStackTrace()
          throw jse
      })
  }

}
