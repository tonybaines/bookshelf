package controllers

import play.api.mvc._
import play.api.libs.json.{JsArray, JsValue, Writes, Json}

// be careful which Json object gets imported
import models.Book

object BookController extends Controller{
  def index = Action {
    val books = BooksWrites.writes(Book.findAll)
    Ok(books)
  }


  // Converters from model to JSON
  implicit object BookWrites extends Writes[Book] {
    def writes(b: Book) = Json.toJson(
      Map(
        "id" -> Json.toJson(b.id),
        "title" -> Json.toJson(b.title)
      )
    )
  }
  implicit object BooksWrites extends Writes[Iterable[Book]] {
    def writes(books: Iterable[Book]) = JsArray(
      books.map(b => BookWrites.writes(b)).toSeq
    )
  }
}
