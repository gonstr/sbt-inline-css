package se.gonstr.sbt.inlinecss

import com.typesafe.sbt.jse.SbtJsTask
import sbt.Keys._
import sbt._
import spray.json._

object Import {

  object InlineCssKeys {
    val inlineCss = TaskKey[Seq[File]]("inline-css", "Invoke the css inliner.")
  }
}

object SbtInlineCss extends AutoPlugin {

  override def requires = SbtJsTask

  override def trigger = AllRequirements

  val autoImport = Import

  import com.typesafe.sbt.jse.SbtJsTask.autoImport.JsTaskKeys._
  import com.typesafe.sbt.web.Import.WebKeys._
  import com.typesafe.sbt.web.SbtWeb.autoImport._
  import se.gonstr.sbt.inlinecss.SbtInlineCss.autoImport.InlineCssKeys._

  val inlineCssUnscopedSettings = Seq(

    includeFilter := GlobFilter("*.html"),

    jsOptions := JsObject(
      // Read the options here (ex. "someOption" -> JsBoolean(someOption.value)
    ).toString()
  )

  override def projectSettings = Seq(
    // Add default option values here (ex. someOption := false)
  ) ++ inTask(inlineCss)(
    SbtJsTask.jsTaskSpecificUnscopedSettings ++
      inConfig(Assets)(inlineCssUnscopedSettings) ++
      inConfig(TestAssets)(inlineCssUnscopedSettings) ++
      Seq(
        moduleName := "inline-css",
        shellFile := getClass.getClassLoader.getResource("shell.js"),

        taskMessage in Assets := "Inlining css",
        taskMessage in TestAssets := "Test inlining css"
      )
  ) ++ SbtJsTask.addJsSourceFileTasks(inlineCss) ++ Seq(
    inlineCss in Assets := (inlineCss in Assets).dependsOn(webModules in Assets).value,
    inlineCss in TestAssets := (inlineCss in TestAssets).dependsOn(webModules in TestAssets).value
  )
}