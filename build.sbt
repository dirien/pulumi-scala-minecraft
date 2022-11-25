val scala3Version = "3.2.1"

lazy val root = project.in(file(".")).settings(
  name := "pulumi-scala-minecraft",
  version := "0.1.0-SNAPSHOT",

  scalaVersion := scala3Version,

  libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
  libraryDependencies += "com.pulumi" % "pulumi" % "0.6.0",
  libraryDependencies += "com.pulumi" % "digitalocean" % "4.16.0",
  libraryDependencies += "com.pulumi" % "command" % "4.5.0",
)
