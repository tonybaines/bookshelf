package controllers

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import models._
import models.Protocol._
import play.api.test.FakeApplication
import play.api.libs.json._
import play.api.http.HeaderNames
import org.specs2.matcher.ThrownExpectations

class BookControllerSpec extends Specification with ThrownExpectations {
  isolated // fails from sbt otherwise
  val application = FakeApplication(additionalConfiguration = inMemoryDatabase("default"))


  "The Books controller" should {
    "respond to the index Action" in {
      running(application) {
        val result = controllers.BookController.index()(FakeRequest())
        status(result) must equalTo(OK)

        // Parse the JSON string back into objects
        val books = Json.parse(contentAsString(result)).as[List[Book]]

        books(0).title must equalTo("The Tao of Pooh")
      }
    }

    "respond to the save Action" in {
      running(application) {
        val requestBody: JsValue = JsObject(List(
          "id" -> JsNumber(0),
          "title" -> JsString("The Dunwich Horror")
        ))

        val request = FakeRequest()
          .copy(body = requestBody)
          .withHeaders(HeaderNames.CONTENT_TYPE -> "application/json")

        val result = controllers.BookController.save()(request)

        status(result) must equalTo(CREATED)
        val newBook = Json.parse(contentAsString(result)).as[Book]

        newBook.title must equalTo("The Dunwich Horror")
        newBook.id mustNotEqual (0)
      }
    }
  }
}
