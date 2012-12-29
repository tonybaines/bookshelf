package models

import play.api.libs.json.{Json, JsObject, JsValue, Format}

/**
 * Container for the play.api.libs.json.Format implicit converters
 * which own (de)serialising objects to/from JSON
 */
object Protocol {
  implicit object BookFormat extends Format[Book] {
    def writes(b: Book): JsValue = JsObject(
      List(
        "id" -> Json.toJson(b.id),
        "title" -> Json.toJson(b.title)
      )
    )
    def reads(json: JsValue): Book = Book(
      (json \ "id").as[Long],
      (json \ "title").as[String]
    )
  }
}
