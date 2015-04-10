name := "allchapters"

version := "1.0"

scalaVersion := "2.11.5"

fork := true

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2+" % "test",
  "com.netflix.rxjava" % "rxjava-scala" % "0.19.1",
  "org.scala-lang.modules" %% "scala-swing" % "1.0.1"
)
