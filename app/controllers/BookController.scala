package controllers

import play.api.mvc._
import play.api.libs.json._
import models.{Protocol, Book}
import play.api.data._
import play.api.data.Forms._


object BookController extends Controller {
  import Protocol._

  def index = Action { request =>
    val books = Book.findAll.toSeq
    if (request.accepts("text/html")) Ok(views.html.shelf(books))
    else if (request.accepts("application/json")) Ok(Json.toJson(books))
    else UnsupportedMediaType("No supported content-types from the Accepts list: " + request.accept.mkString(","))
  }


  val bookForm = Form(tuple (
      "bookAuthor" -> text,
      "bookTitle" -> text)
  )


  def save = Action { implicit request =>
    if (request.accepts("text/html")) {
      val (bookAuthor, bookTitle) = bookForm.bindFromRequest().get
      val newBook = Book(title=bookTitle, author=bookAuthor)
      Book.insert(newBook)
      Ok(views.html.shelf(Book.findAll.toSeq))
    }
    else if (request.accepts("application/json")) {
      val bookJson = request.body.asJson
      val newBook = Book.insert(Json.fromJson(bookJson.getOrElse(JsNull)))
      Created(Json.toJson(newBook))
    }
    else UnsupportedMediaType("No supported content-types from the Accepts list: " + request.accept.mkString(","))
  }

}