package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json // be careful which Json object gets imported

object Book extends Controller{
  def index = Action {
    val books = Json.toJson(
      Seq(
        Map("title" -> "The Tao of Pooh"),
        Map("title" -> "The Te of Piglet")
      )
    )
    Ok(books)
  }
}
