organization := "co.movio.sbt"

name := "sbt-example-testing"

version := "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq() 


lazy val subA = project in file("sub-a") settings (
  libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.13"
)

lazy val subB= project in file("sub-b") settings (
  libraryDependencies ++= Seq(
    "com.icegreen" % "greenmail" % "1.3.1b",
    "org.scalatest" %% "scalatest" % "2.0" % "test")
    //"org.scalatest" % "scalatest_2.10" % "2.0" % "test")
  )


//net.virtualvoid.sbt.graph.Plugin.graphSettings
