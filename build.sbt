ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "scala-project-rule-engine"
  )

libraryDependencies += "org.postgresql" % "postgresql" % "42.7.3"