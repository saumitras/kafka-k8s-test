name := "KafkaK8sTest"

version := "1.0"

scalaVersion := "2.12.7"

resolvers ++= Seq(
  "confluent" at "https://packages.confluent.io/maven/"
)

def dockerSettings(debugPort: Option[Int] = None) = Seq(

  dockerfile in docker := {
    val artifactSource: File = assembly.value
    val artifactTargetPath = s"/project/${artifactSource.name}"
    val scriptSourceDir = baseDirectory.value / "../scripts"
    val projectDir = "/project/"

    new Dockerfile {
      from("saumitras01/java:1.8.0_111")
      add(artifactSource, artifactTargetPath)
      copy(scriptSourceDir, projectDir)
      entryPoint(s"/project/start.sh")
      cmd(projectDir, s"${name.value}", s"${version.value}")
    }
  },
  imageNames in docker := Seq(
    ImageName(s"saumitras01/${name.value}:1.0.1")
  )
)

lazy val producer = (project in file("producer"))
  .enablePlugins(sbtdocker.DockerPlugin)
  .settings(
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x => MergeStrategy.first
    },
      libraryDependencies ++= Seq(
      "org.apache.kafka" %% "kafka" % "2.5.0",
      "org.apache.kafka" % "kafka-streams-scala_2.12" % "2.5.0",
      "org.apache.avro" % "avro" % "1.9.2"
    ),
    dockerSettings()
  )


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}