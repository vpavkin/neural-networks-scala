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
lazy val refinedVersion = "0.8.1"
lazy val breezeVersion = "0.13.1"

lazy val scalatestVersion = "3.0.1"
lazy val scalacheckVersion = "1.13.4"


lazy val dependencies = libraryDependencies ++= Seq(
  compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3"),
  "com.github.mpilquist" %% "simulacrum" % simulacrumVersion,
  "com.github.pathikrit" %% "better-files" % betterFilesVersion,
  "org.typelevel" %% "cats" % catsVersion,
  "io.monix" %% "monix-execution" % monixVersion,
  "io.monix" %% "monix-eval" % monixVersion,
  "io.monix" %% "monix-cats" % monixVersion,
  "com.chuusai" %% "shapeless" % shapelessVersion,
  "eu.timepit" %% "refined" % refinedVersion,
  "org.scalanlp" %% "breeze" % breezeVersion,
  "org.scalanlp" %% "breeze-natives" % breezeVersion,
  "org.scalanlp" %% "breeze-viz" % breezeVersion,
  "org.scalacheck" %% "scalacheck" % scalacheckVersion % "test",
  "org.scalatest" %% "scalatest" % scalatestVersion % "test"
)

lazy val scalaMacrosSettings = libraryDependencies ++= Seq(
  compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full)
)


lazy val neuralNetworks = (project in file("."))
  .settings(sourceSettings: _*)
  .settings(scalaMacrosSettings)
  .settings(dependencies)
