import play.sbt.PlayImport.PlayKeys._
import sbt.Keys._

lazy val buildSettings = Seq(
  scalaVersion := "2.12.2",
  crossScalaVersions := Seq("2.12.2"),
  organization := "ru.pavkin"
)

lazy val commonCompilerFlags = Seq(
  "-deprecation:false",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-unchecked",
  "-Xfatal-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ypartial-unification",
  "-Xfuture",
  "-deprecation:true"
)

lazy val compilerSettings = Seq(
  scalacOptions ++= commonCompilerFlags
)

lazy val testSettings = Seq(
  testOptions in Test := Seq(
    Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
  )
)

lazy val sourceSettings = buildSettings ++ compilerSettings ++ testSettings

lazy val betterFilesVersion = "3.0.0"
lazy val monixVersion = "2.2.4"
lazy val catsVersion = "0.9.0"
lazy val simulacrumVersion = "0.10.0"
lazy val shapelessVersion = "2.3.2"
lazy val circeVersion = "0.8.0"
lazy val refinedVersion = "0.8.2"
lazy val enumeratumVersion = "1.5.12"
lazy val breezeVersion = "0.13.1"

lazy val scalajsReactVersion = "1.0.1"
lazy val scalaCSSVersion = "0.5.3"
lazy val diodeVersion = "1.1.2"
lazy val jqueryFacadeVersion = "1.0"

lazy val reactJSVersion = "15.5.4"


lazy val coreDependencies = libraryDependencies ++= Seq(
  compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3"),
  "com.github.mpilquist" %%% "simulacrum" % simulacrumVersion,
  "org.typelevel" %%% "cats" % catsVersion,
  "io.monix" %%% "monix-execution" % monixVersion,
  "io.monix" %%% "monix-eval" % monixVersion,
  "io.monix" %%% "monix-cats" % monixVersion,
  "com.chuusai" %%% "shapeless" % shapelessVersion,
  "eu.timepit" %%% "refined" % refinedVersion,
  "com.beachape" %%% "enumeratum" % enumeratumVersion
)

lazy val jvmDependencies = libraryDependencies ++= Seq(
  "com.github.pathikrit" %% "better-files" % betterFilesVersion,
  "org.scalanlp" %% "breeze" % breezeVersion,
  "org.scalanlp" %% "breeze-natives" % breezeVersion,
  "org.scalanlp" %% "breeze-viz" % breezeVersion
)

lazy val scalaMacrosSettings = libraryDependencies ++= Seq(
  compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full)
)

lazy val frontendDependencies = libraryDependencies ++= Seq(
  "com.github.japgolly.scalajs-react" %%% "core" % scalajsReactVersion,
  "com.github.japgolly.scalajs-react" %%% "extra" % scalajsReactVersion,
  "com.github.japgolly.scalacss" %%% "core" % scalaCSSVersion,
  "com.github.japgolly.scalacss" %%% "ext-react" % scalaCSSVersion,
  "io.suzaku" %%% "diode" % diodeVersion,
  "io.suzaku" %%% "diode-react" % diodeVersion,
  "org.querki" %%% "jquery-facade" % jqueryFacadeVersion
)

lazy val jsonDependencies = libraryDependencies ++= Seq(
  "io.circe" %%% "circe-core" % circeVersion,
  "io.circe" %%% "circe-generic" % circeVersion,
  "io.circe" %%% "circe-parser" % circeVersion
)

lazy val core = (crossProject in file("core"))
  .settings(sourceSettings: _*)
  .settings(scalaMacrosSettings)
  .settings(coreDependencies)
  .jvmSettings(jvmDependencies)
  .jsConfigure(_ enablePlugins ScalaJSBundlerPlugin)

lazy val json = (crossProject in file("json"))
  .settings(sourceSettings: _*)
  .settings(
    name := "json",
    scalaMacrosSettings,
    coreDependencies,
    jsonDependencies
  )
  .dependsOn(core)
  .jsConfigure(_ enablePlugins ScalaJSBundlerPlugin)

lazy val backend = (project in file("backend"))
  .enablePlugins(PlayScala, WebScalaJSBundlerPlugin)
  .settings(
    name := "backend",
    scalaJSProjects := Seq(frontend),
    pipelineStages in Assets := Seq(scalaJSPipeline)
  )
  .settings(sourceSettings: _*)
  .settings(
    scalaMacrosSettings,
    coreDependencies,
    jvmDependencies,
    jsonDependencies
  )
  .settings(libraryDependencies ++= Seq(
    ws
  ))
  .dependsOn(coreJVM, jsonJVM)


lazy val frontend = project.in(file("frontend"))
  .enablePlugins(ScalaJSBundlerPlugin, ScalaJSWeb)
  .settings(sourceSettings: _*)
  .settings(
    name := "frontend",
    coreDependencies,
    frontendDependencies
  )
  .settings(
    npmDependencies in Compile ++= Seq(
      "react" -> reactJSVersion,
      "react-dom" -> reactJSVersion
    )
  )
  .dependsOn(coreJS, jsonJS)

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

lazy val jsonJVM = json.jvm
lazy val jsonJS = json.js

lazy val neuralNetworks = (project in file("."))
  .settings(buildSettings: _*)
  .settings(name := "neural-networks")
  .settings(publish := {})
  .aggregate(backend, frontend, coreJS, coreJVM, jsonJVM, jsonJS)
