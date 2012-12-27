package controllers

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._

class BookControllerSpec extends Specification{
  "The Books controller" should {
    "respond to the index Action" in {
      val result = controllers.Book.index()(FakeRequest())
      status(result) must equalTo(OK)
    }
  }
}
