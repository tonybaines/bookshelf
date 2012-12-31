package controllers

import play.api.mvc._
import play.api.libs.json._
import models.{Protocol, Book}
import play.api.http.ContentTypes


object BookController extends Controller {
  import Protocol._
  def index = Action { request =>
    val books = Book.findAll.toSeq
    if (request.accepts("text/html")) Ok(views.html.shelf(books))
    else if (request.accepts("application/json")) Ok(Json.toJson(books))
    else BadRequest("Unsupported Accepts: " + request.contentType)
  }

  def save = Action(parse.json) { request =>
    val bookJson = request.body
    val newBook = Book.insert(bookJson.as[Book])
    Created(Json.toJson(newBook))
  }

}