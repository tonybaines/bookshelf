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
import play.api.mvc.{AnyContentAsEmpty, AnyContent, Request}

class BookControllerSpec extends Specification with ThrownExpectations {
  isolated // fails from sbt otherwise, fails in IDEA when enabled :(

  val application = FakeApplication()
  "The Books controller index Action" should {
    "respond to JSON requests" in {
      running(application) {
        val jsonRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withHeaders(HeaderNames.ACCEPT -> "application/json")
        val result = controllers.BookController.index()(jsonRequest)
        status(result) must equalTo(OK)

        // Parse the JSON string back into objects
        val books = Json.parse(contentAsString(result)).as[List[Book]]

        books(0).title must equalTo("The Tao of Pooh")
      }
    }

    "respond to HTML requests" in {
      running(application) {
        val htmlRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withHeaders(HeaderNames.ACCEPT -> "text/html")
        val result = controllers.BookController.index()(htmlRequest)

        status(result) must equalTo(OK)
        contentType(result) must beSome("text/html")
        contentAsString(result) must contain("The Tao of Pooh")
      }
    }

    "reject unknown 'Accepts'" in {
      running(application) {
        val stupidRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withHeaders(HeaderNames.ACCEPT -> "non/existant")
        val result = controllers.BookController.index()(stupidRequest)

        status(result) must equalTo(UNSUPPORTED_MEDIA_TYPE)
      }
    }
  }

  "The Books controller save Action" should {
    "respond to JSON requests" in {
      running(application) {
        val requestBody = JsObject(List(
          "id" -> JsNumber(0),
          "title" -> JsString("The Dunwich Horror"),
          "author" -> JsString("H P Lovecraft")
        ))

        val jsonRequest = FakeRequest()
          .withJsonBody(requestBody)
          .withHeaders(HeaderNames.ACCEPT -> "application/json")

        val result = controllers.BookController.save()(jsonRequest)

        status(result) must equalTo(CREATED)
        val newBook = Json.parse(contentAsString(result)).as[Book]

        newBook.title must equalTo("The Dunwich Horror")
        newBook.id mustNotEqual (0)
      }
    }
    "respond to form-submission requests" in {
      running(application) {
        val formRequest = FakeRequest()
          .withFormUrlEncodedBody("bookAuthor" -> "H P Lovecraft", "bookTitle" -> "At the Mountains of Madness")
          .withHeaders(HeaderNames.ACCEPT -> "text/html")

        val result = controllers.BookController.save()(formRequest)

        status(result) must equalTo(OK)
        contentType(result) must beSome("text/html")
        contentAsString(result) must contain("At the Mountains of Madness")
      }
    }

    "reject unknown 'Accepts'" in {
      running(application) {
        val stupidRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withHeaders(HeaderNames.ACCEPT -> "non/existant")
        val result = controllers.BookController.save()(stupidRequest)

        status(result) must equalTo(UNSUPPORTED_MEDIA_TYPE)
      }
    }
  }
}
