import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "bookshelf"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      jdbc, anorm,
      "org.seleniumhq.selenium" % "selenium-java" % "2.28.0" % "test",
      "org.squeryl" % "squeryl_2.10" % "0.9.5-6",
      "com.google.code.findbugs" % "jsr305" % "1.3.+",
      "mysql" % "mysql-connector-java" % "5.1.21"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
