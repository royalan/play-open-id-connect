name := """play-open-id-connect"""

version := "0.0.1"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, JavaAppPackaging, AshScriptPlugin)

TwirlKeys.templateImports += "an.royal.oidc.dtos._"

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  guice, cacheApi, ehcache,
  "com.typesafe.play" %% "play-slick" % "3.0.2",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.2",
  "com.github.tminglei" %% "slick-pg" % "0.15.3",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.15.3",
  "org.postgresql" % "postgresql" % "42.1.4",
  "net.logstash.logback" % "logstash-logback-encoder" % "4.11",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test)



//=======================================================================================
// Docker Settings
//=======================================================================================
maintainer := "Royalan"
dockerBaseImage := "openjdk:8-jre-alpine"
dockerUpdateLatest := true
dockerExposedPorts += 9000
dockerExposedVolumes += "/tmp"
dockerUpdateLatest := true
dockerUsername := Some("royalan")