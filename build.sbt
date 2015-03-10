sbtPlugin := true

organization := "se.gonstr"

name := "sbt-inline-css"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.5"

resolvers ++= Seq(
  "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/",
  Resolver.url("sbt snapshot plugins", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots"))(Resolver.ivyStylePatterns),
  Resolver.sonatypeRepo("snapshots"),
  "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/",
  Resolver.mavenLocal
)

addSbtPlugin("com.typesafe.sbt" %% "sbt-js-engine" % "1.0.2")

publishMavenStyle := false

//publishTo := {
//  if (isSnapshot.value) Some(Classpaths.sbtPluginSnapshots)
//  else Some(Classpaths.sbtPluginReleases)
//}

scriptedSettings

scriptedLaunchOpts <+= version apply { v => s"-Dproject.version=$v" }
