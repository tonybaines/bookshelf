package functional

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import java.util.concurrent.TimeUnit

class Acceptance extends Specification {
  "A list of books should be available" in {
    running(TestServer(3333), FIREFOX) { browser =>
      browser.goTo("http://localhost:3333/")
      browser.title() must equalTo("Your Books")
      browser.await.atMost(20, TimeUnit.SECONDS).until(".bookEntry").hasSize(2)
      browser.$(".bookTitle").getTexts().get(0) must equalTo("The Tao of Pooh")
    }
  }

}
