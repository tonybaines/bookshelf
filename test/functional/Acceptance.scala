package functional

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._

class Acceptance extends Specification {
  "A list of books should be available" in {
    running(TestServer(3333), HTMLUNIT) { browser =>
      browser.goTo("http://localhost:3333/")
      browser.title() must equalTo("Your Books")
    }
  }

}
