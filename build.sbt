name := """play-open-id-connect"""

version := "0.0.1"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, JavaAppPackaging)

TwirlKeys.templateImports += "an.royal.oidc.dtos._"

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.4"

libraryDependencies += guice
libraryDependencies += cacheApi
libraryDependencies += ehcache
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.2"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.2"
libraryDependencies += "com.github.tminglei" %% "slick-pg" % "0.15.3"
libraryDependencies += "com.github.tminglei" %% "slick-pg_play-json" % "0.15.3"
libraryDependencies += "org.postgresql" % "postgresql" % "42.1.4"


maintainer := "Royalan"
dockerBaseImage := "openjdk:8-jre-alpine"
dockerUpdateLatest := true
dockerExposedPorts += 9000
dockerExposedVolumes += "/tmp"
dockerUpdateLatest := true
dockerUsername := Some("royalan")