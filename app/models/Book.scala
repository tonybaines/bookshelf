package models

import org.squeryl.{Query, KeyedEntity}
import org.squeryl.PrimitiveTypeMode._
import collection.Iterable

object Book {
  import Database.booksTable
  def deleteAll() = inTransaction {
    booksTable.deleteWhere{ b=> b.id === b.id }
  }

  def findAll: Iterable[Book] = inTransaction {
    from(booksTable) { book => select(book)}.toList
  }

  def insert(book: Book): Book = inTransaction {
    booksTable.insert(book.copy())
  }
}

case class Book(
  val id: Long = 0,
  val title: String
) extends KeyedEntity[Long]
