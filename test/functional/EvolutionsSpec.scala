package functional

import org.specs2.mutable._

import play.api.db.DB
import play.api.Play.current

import play.api.test._
import play.api.test.Helpers._

/**
 * Taken from http://raibledesigns.com/rd/entry/upgrading_to_play_2_anorm
 *
 * Verify that database evolutions work
 */
class EvolutionsTest extends Specification {
  "Evolutions" should {
    "be applied without errors" in {
      evolutionFor("default")
      running(FakeApplication()) {
        DB.withConnection {
          implicit connection =>
            anorm.SQL("select count(1) from books").execute()
        }
      }
      success
    }
  }
}
