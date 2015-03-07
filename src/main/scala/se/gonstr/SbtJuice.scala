package se.gonstr

import com.typesafe.sbt.jse.JsEngineImport.JsEngineKeys
import sbt._
import sbt.Keys._
import com.typesafe.sbt.web._
import com.typesafe.sbt.jse.SbtJsTask
import spray.json._

object Import {

  object JuiceKeys {
    val juice = TaskKey[Seq[File]]("juice", "Invoke the juice css inliner.")
    val preserveMediaQueries = SettingKey[Boolean]("juice-preserve-media-queries", "Preserves all media queries (and contained styles) within <style></style> tags as a refinement when removeStyleTags is true. Other styles are removed. Defaults to false")
    val preserveImportant = SettingKey[Boolean]("juice-preserve-important", "Preserves !important in values. Defaults to false")
  }
}

object SbtJuice extends AutoPlugin {

  override def requires = SbtJsTask

  override def trigger = AllRequirements

  val autoImport = Import

  import SbtWeb.autoImport._
  import WebKeys._
  import SbtJsTask.autoImport.JsTaskKeys._
  import autoImport.JuiceKeys._

  val juiceUnscopedSettings = Seq(

    includeFilter := GlobFilter("*.html"),

    jsOptions := JsObject(
      "preserveMediaQueries" -> JsBoolean(preserveMediaQueries.value),
      "preserveImportant" -> JsBoolean(preserveImportant.value)
    ).toString()
  )

  override def projectSettings = Seq(
    preserveMediaQueries := false,
    preserveImportant := false,
    JsEngineKeys.engineType in juice := JsEngineKeys.EngineType.Node

  ) ++ inTask(juice)(
    SbtJsTask.jsTaskSpecificUnscopedSettings ++
      inConfig(Assets)(juiceUnscopedSettings) ++
      inConfig(TestAssets)(juiceUnscopedSettings) ++
      Seq(
        moduleName := "juice",
        shellFile := getClass.getClassLoader.getResource("juice-shell.js"),

        taskMessage in Assets := "Juice inlining css",
        taskMessage in TestAssets := "Juice test inlining css"
      )
  ) ++ SbtJsTask.addJsSourceFileTasks(juice) ++ Seq(
    juice in Assets := (juice in Assets).dependsOn(webModules in Assets).value,
    juice in TestAssets := (juice in TestAssets).dependsOn(webModules in TestAssets).value
  )
}