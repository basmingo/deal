ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"
assembly / assemblyJarName := s"deal-$version.jar"
scalacOptions += "-Ymacro-annotations"

enablePlugins(JooqCodegenPlugin)

lazy val root = (project in file("."))
  .settings(
    name := "deal"
  )

jooqCodegenConfig := file("src/main/resources/jooq-codegen.xml")
jooqCodegenVariables ++= Map(
  "JDBC_DRIVER" -> "org.postgresql.Driver",
  "JDBC_URL" -> "jdbc:postgresql://localhost:5431/conveyor_deal",
  "JDBC_PROPS" -> {
    val props = new java.util.Properties()
    props.setProperty("user", "admin")
    props.setProperty("password", "password")
    props
  },
)

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.0.15",
  "dev.zio" %% "zio-macros" % "2.0.15",
  "dev.zio" %% "zio-json" % "0.6.2",
  "org.jooq" %% "jooq-scala" % "3.18.0",
  "org.postgresql" % "postgresql" % "42.6.0" % JooqCodegen,
  "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1"
)
