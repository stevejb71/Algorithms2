lazy val commonSettings = Seq(
  organization := "Coursera Project",
  version := "0.1.0",
  scalaVersion := "2.11.8"
)

enablePlugins(JmhPlugin)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    // set the name of the project
    name := "Graphs",
    
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-core" % "3.7.2" % "test",
      "com.novocode" % "junit-interface" % "0.11" % "test"
    )
  );

    

