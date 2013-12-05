package co.movio.sbt

import sbt.Keys._
import sbt.Load.BuildStructure
import sbt._
import sbt.complete.DefaultParsers._
import complete.Parser
import scala.collection.mutable

object ExamplePlugin extends Plugin {

  // Define tasks
  val exampleTask = TaskKey[Unit]("exampleTask", "some help text")
  val exampleInput = InputKey[Unit]("exampleInput", "helloInput <number> <colour> will update deps")
  val exampleTaskExecution = InputKey[Unit]("exampleTaskExecution", "exampleTaskExecution ... will download src and javadoc")

  // Commands can get included in all settings but only run on the global scope
  def commandExample = Command.args("commandExample", "<name>") { (state, args) ⇒
    println("Hi " + args.mkString(" "))
    state
  }

  // Setup tasks

  // Manually add to projects
  //val exampleSettings: Seq[Setting[_]] = Seq[Setting[_]](

  // Add to all projects
  override def settings: Seq[Setting[_]] = Seq[Setting[_]](
    commands ++= Seq(commandExample),
    exampleTask <<= (thisProjectRef, defaultConfiguration, streams) map {
      (thisProjectRef, conf, streams) ⇒
        streams.log.info(s"Project : $thisProject")
        streams.log.info(s"Conf : $conf")
    },
    exampleInput <<= InputTask(exampleParser) { args ⇒
      (args, state, streams) map { (args, state, streams) ⇒
        streams.log.info(s"Args : $args")

        // Execute Task for the current project
        val updateReport = Project.evaluateTask(Keys.update, state)
        streams.log.info(s"UpdateReport : $updateReport")
      }
    },
    exampleTaskExecution <<= InputTask(basicParser) { args ⇒
      (args, state, buildStructure, baseDirectory, streams) map { (args, state, structure, base, streams) ⇒
        // Get all projects
        // Another way to get the structure
        //val structure = Project.extract(state).structure
        val projectRefs = structure.allProjectRefs

        // Dowload all dependencies, sources and java doc
        val updateClassifiers = Keys.updateClassifiers
        projectRefs foreach (ref ⇒ {
          val updateReport = EvaluateTask(structure, updateClassifiers, state, ref, EvaluateTask defaultConfig state)
          streams.log.info(s"Classifiers report: $updateReport")
        })
      }
    }
  )

  // Example IO taken from: http://github.com/kalmanb/sbt-ctags 
  def unzipSource(dest: File, sourceJar: File): Unit = {
    IO.delete(dest) // remove destination dir
    IO.createDirectory(dest)
    IO.unzip(sourceJar, dest)
  }


  // How to eval a TaskKey (eg. updateClassifiers: UpdateReport) see
  // http://www.scala-sbt.org/0.13.0/sxr/sbt/Keys.scala.html#sbt.Keys.updateClassifiers
  // val key = Keys.updateClassifiers - returns sbt.UpdateReport
  def evaluateTask[A](key: TaskKey[A], ref: ProjectRef, state: State): Option[(sbt.State, sbt.Result[A])] = {
    EvaluateTask(Project.extract(state).structure, key, state, ref, EvaluateTask defaultConfig state)
  }

  // auto complete example
  //http://www.scala-sbt.org/release/docs/Detailed-Topics/Parsing-Input.html
  val basicParser = (state: State) ⇒ (token(Space ~ "one" | Space ~ "two") ~ token(Space ~ "red" | Space ~ "blue"))

  import Project._
  val exampleParser: Initialize[State ⇒ Parser[(Seq[Char], String)]] =
    (resolvedScoped, baseDirectory) { (ctx, base) ⇒
      (state: State) ⇒
        token(Space ~ "someparam") | (Space ~ "another") // token("blue") | token("red") ~ Space ~ token(token("one") | "two")
    }

}

