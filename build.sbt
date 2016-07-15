name := """satriapribadi"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

/* Oauth2 */
libraryDependencies ++= Seq(
  "com.nulab-inc" %% "play2-oauth2-provider" % "0.17.2"
)

/* Database */
libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.38",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0"
)

/* Joda */
libraryDependencies ++= Seq(
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.2.0",
  "joda-time" % "joda-time" % "2.7",
  "org.joda" % "joda-convert" % "1.7"
)

libraryDependencies +=  "com.iheart" %% "play-swagger" % "0.2.1-PLAY2.5"
libraryDependencies += "org.webjars" % "swagger-ui" % "2.1.4"