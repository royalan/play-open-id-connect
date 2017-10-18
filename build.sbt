name := """play-open-id-connect"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += cacheApi
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test
//libraryDependencies += "com.h2database" % "h2" % "1.4.194"
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.2"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.2"
libraryDependencies += "com.github.tminglei" %% "slick-pg" % "0.15.3"
libraryDependencies += "com.github.tminglei" %% "slick-pg_play-json" % "0.15.3"
libraryDependencies += "org.postgresql" % "postgresql" % "42.1.4"

