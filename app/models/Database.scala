package models

import org.squeryl.{Table, Schema}
import org.squeryl.PrimitiveTypeMode._


object Database extends Schema {
  val booksTable: Table[Book] = table[Book]("books") // the table-name

  on(booksTable) { b=> declare {
    b.id is(autoIncremented)
  }}
}
