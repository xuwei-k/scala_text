import NpmCliBase._

val textLintAll = taskKey[Unit]("lint text, html")
val textTestAll = taskKey[Unit]("test scala, links")

ThisBuild / onChangedBuildSource := ReloadOnSourceChanges

name := "textbook"

scalaVersion := "3.2.2"

crossScalaVersions += "2.13.10"

enablePlugins(MdocPlugin)

mdocIn := srcDir

mdocOut := compiledSrcDir

cleanFiles += compiledSrcDir

// sbt 2のalpha版で不整合があるため。それがなくなったらこの設定削除
evictionErrorLevel := Level.Warn

libraryDependencies ++= Seq(
  "org.scala-sbt" % "main" % "2.0.0-alpha7" cross CrossVersion.for2_13Use3 exclude ("org.scala-lang.modules", "scala-xml_3"),
  "org.mockito" % "mockito-core" % "5.2.0",
  "org.scalacheck" %% "scalacheck" % "1.17.0",
  "org.scalatest" %% "scalatest-flatspec" % "3.2.15", // mdocで使うので、テストライブラリだが、わざとcompileスコープ
  "org.scalatest" %% "scalatest-diagrams" % "3.2.15"
)

Honkit.settings

TextLint.settings

LinkTest.settings

textLintAll := Def.sequential(LinkTest.textEslint, TextLint.textLint.toTask("")).value

textTestAll := Def.sequential(Test / compile, LinkTest.textLinkTest).value

aggregateProjects(
  RootProject(file("src/example_projects/trait-example")),
  RootProject(file("src/example_projects/trait-refactored-example"))
)
