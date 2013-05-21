scalaVersion := "2.10.0"

scalaSource in Compile <<= baseDirectory (_ / "src")

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xlint", "-Xfatal-warnings",
                      "-encoding", "us-ascii", "-language:_")

libraryDependencies ++= Seq(
  "org.nlogo" % "NetLogoEngine" % "5.1.x-20885db" from
    "http://ccl.northwestern.edu/devel/NetLogoEngine-20885db.jar",
  "org.nlogo" % "NetLogoGUI" % "5.1.x-20885db" from
      "http://ccl.northwestern.edu/devel/NetLogoGUI-20885db.jar"
)

artifactName := { (_, _, _) => "json.jar" }

packageOptions := Seq(
  Package.ManifestAttributes(
    ("Extension-Name", "json"),
    ("NetLogo-Extension-API-Version", "5.0"),
    ("Class-Manager", "org.nlogo.extensions.json.JSONExtension")))
