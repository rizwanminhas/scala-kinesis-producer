name := """scala-kinesis-producer"""

organization := "com.rminhas"

version := "1.0-SNAPSHOT"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "com.amazonaws" % "amazon-kinesis-producer" % "0.14.6",
  "org.slf4j" % "slf4j-api" % "1.7.29",
  "org.slf4j" % "slf4j-simple" % "1.7.29")
