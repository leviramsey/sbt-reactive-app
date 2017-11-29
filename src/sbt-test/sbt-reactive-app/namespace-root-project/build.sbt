version := "0.1.2-SNAPSHOT"
scalaVersion := "2.12.4"

enablePlugins(SbtReactiveAppPlugin)

lazy val `hello-building` = (project in file("."))
  .aggregate(boxes)

lazy val boxes = (project in file("boxes"))
  .enablePlugins(SbtReactiveAppPlugin)

TaskKey[Unit]("check") := {
  val outputDir = (stage in Docker in boxes).value
  val contents = IO.readLines(outputDir / "Dockerfile")
  val lines = Seq(
    """LABEL com.lightbend.rp.namespace="hello-building""""
  )

  lines.foreach { line =>
    if (!contents.contains(line)) {
      sys.error(
        s"""Dockerfile is missing line "$line" - Dockerfile contents:
           |${contents.mkString("\n")}
         """.stripMargin)
    }
  }

  val dockerRepositoryValue = (dockerRepository in Docker in boxes).value
  val dockerRepositoryValueExpected = Some("hello-building")
  assert(dockerRepositoryValue == dockerRepositoryValueExpected,
    s"Docker repository value is $dockerRepositoryValue - expected $dockerRepositoryValueExpected}")

  val namespaceValue = namespace.value
  val namespaceValueExpected = Some("hello-building")
  assert(namespaceValue  == namespaceValueExpected,
    s"Namespace value is $namespaceValue - expected $namespaceValueExpected}")

}