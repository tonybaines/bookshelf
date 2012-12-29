package controllers

import play.api.mvc._
import play.api.libs.json._
import models.{Protocol, Book}

object BookController extends Controller {
  import Protocol._
  def index = Action { request =>
    val books = Json.toJson(Book.findAll.toSeq)
    Ok(books)
  }
  def save = Action(parse.json) { request =>
    val bookJson = request.body
    val newBook = Book.insert(bookJson.as[Book])
    Created(Json.toJson(newBook))
  }

}