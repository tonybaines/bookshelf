package models

import play.api.libs.json._
import play.api.libs.json.JsObject

/**
 * Container for the play.api.libs.json.Format implicit converters
 * which own (de)serialising objects to/from JSON
 */
object Protocol {
  implicit object BookFormat extends Format[Book] {
    def writes(b: Book): JsValue = JsObject(
      List(
        "id" -> Json.toJson(b.id),
        "title" -> Json.toJson(b.title),
        "author" -> Json.toJson(b.author)
      )
    )
    def reads(json: JsValue): JsResult[Book] = JsSuccess(Book(
      (json \ "id").as[Long],
      (json \ "title").as[String],
      (json \ "author").as[String]
    ))
  }
}
