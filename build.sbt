organization := "co.movio"
            
name := "sbt-examples"

version := "0.1.0-SNAPSHOT"

sbtPlugin := true

publishMavenStyle := false

publishArtifact in Test := false

//publishTo := Some(Resolver.url("repo", new URL())(Resolver.ivyStylePatterns))

libraryDependencies += "junit" % "junit" % "4.11" % "test"

libraryDependencies <+= scalaVersion { version ⇒
  if (version startsWith "2.10") "org.scalatest" %% "scalatest" % "2.0"
  else "org.scalatest" %% "scalatest" % "2.0.M6-SNAP3"
}

