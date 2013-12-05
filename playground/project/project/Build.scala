import sbt._
object PluginDef extends Build {
   override lazy val projects = Seq(root)
   lazy val root = Project("plugins", file(".")) dependsOn( myPlugin )
   lazy val myPlugin = uri("file:////home/kalmanb/work/sbt-examples/")
}
