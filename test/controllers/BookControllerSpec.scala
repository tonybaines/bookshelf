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
import play.api.mvc.AnyContent

class BookControllerSpec extends Specification with ThrownExpectations {
  isolated // fails from sbt otherwise, fails in IDEA when enabled :(

  val application = FakeApplication()
  "The Books controller" should {
    "respond to the index Action for JSON requests" in {
      running(application) {
        val jsonRequest: FakeRequest[AnyContent] = FakeRequest().withHeaders(HeaderNames.ACCEPT -> "application/json")
        val result = controllers.BookController.index()(jsonRequest)
        status(result) must equalTo(OK)

        // Parse the JSON string back into objects
        val books = Json.parse(contentAsString(result)).as[List[Book]]

        books(0).title must equalTo("The Tao of Pooh")
      }
    }

    "respond to the index action for HTML requests" in {
      running(application) {
        val htmlRequest: FakeRequest[AnyContent] = FakeRequest().withHeaders(HeaderNames.ACCEPT -> "text/html")
        val result = controllers.BookController.index()(htmlRequest)

        status(result) must equalTo(OK)
        contentType(result) must beSome("text/html")
        contentAsString(result) must contain("The Tao of Pooh")
      }
    }

    "respond to the save Action for JSON requests" in {
      running(application) {
        val requestBody: JsValue = JsObject(List(
          "id" -> JsNumber(0),
          "title" -> JsString("The Dunwich Horror"),
          "author" -> JsString("H P Lovecraft")
        ))

        val request = FakeRequest()
          .copy(body = requestBody)
          .withHeaders(HeaderNames.ACCEPT -> "application/json")

        val result = controllers.BookController.save()(request)

        status(result) must equalTo(CREATED)
        val newBook = Json.parse(contentAsString(result)).as[Book]

        newBook.title must equalTo("The Dunwich Horror")
        newBook.id mustNotEqual (0)
      }
    }
  }
}
