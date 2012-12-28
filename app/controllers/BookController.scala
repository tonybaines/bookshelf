package controllers

import play.api.mvc._
import play.api.libs.json.{JsArray, JsValue, Writes, Json}

// be careful which Json object gets imported

import models.Book

object BookController extends Controller {
  def index = Action {
    val books = Json.toJson(Book.findAll.map { b =>
      Map(
        "id" -> Json.toJson(b.id),
        "title" -> Json.toJson(b.title)
      )
    } toSeq)
    Ok(books)
  }
}