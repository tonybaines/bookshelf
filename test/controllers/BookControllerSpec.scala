package controllers

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.{JsValue, Json}

class BookControllerSpec extends Specification{
  "The Books controller" should {
    "respond to the index Action" in {
      val result = controllers.BookController.index()(FakeRequest())
      status(result) must equalTo(OK)

      // Parse the JSON string back into objects
      val json = Json.parse(contentAsString(result)).as[List[Map[String, String]]]

      json(0)("title") must equalTo("The Tao of Pooh")
    }
  }
}
