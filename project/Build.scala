import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "bookshelf"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.seleniumhq.selenium" % "selenium-java" % "2.28.0" % "test",
      "org.squeryl" % "squeryl_2.9.0-1" % "0.9.4",
      "mysql" % "mysql-connector-java" % "5.1.21"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
