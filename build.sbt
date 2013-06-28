scalaVersion := "2.9.2"

scalaSource in Compile <<= baseDirectory(_ / "src")

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xfatal-warnings",
                      "-encoding", "us-ascii")

libraryDependencies +=
  "org.nlogo" % "NetLogo" % "5.0.4" from
    "http://ccl.northwestern.edu/netlogo/5.0.4/NetLogo.jar"

artifactName := { (_, _, _) => "json.jar" }

packageOptions := Seq(
  Package.ManifestAttributes(
    ("Extension-Name", "json"),
    ("Class-Manager", "org.nlogo.extensions.json.JSONExtension"),
    ("NetLogo-Extension-API-Version", "5.0")))

packageBin in Compile <<= (packageBin in Compile, baseDirectory, streams) map {
  (jar, base, s) =>
    IO.copyFile(jar, base / "json.jar")
    Process("pack200 --modification-time=latest --effort=9 --strip-debug " +
            "--no-keep-file-order --unknown-attribute=strip " +
            "json.jar.pack.gz json.jar").!!
    if(Process("git diff --quiet --exit-code HEAD").! == 0) {
      Process("git archive -o json.zip --prefix=json/ HEAD").!!
      IO.createDirectory(base / "json")
      IO.copyFile(base / "json.jar", base / "json" / "json.jar")
      IO.copyFile(base / "json.jar.pack.gz", base / "json" / "json.jar.pack.gz")
      Process("zip json.zip json/json.jar json/json.jar.pack.gz").!!
      IO.delete(base / "json")
    }
    else {
      s.log.warn("working tree not clean; no zip archive made")
      IO.delete(base / "json.zip")
    }
    jar
  }

cleanFiles <++= baseDirectory { base =>
  Seq(base / "json.jar",
      base / "json.jar.pack.gz",
      base / "json.zip") }
