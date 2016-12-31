name := "TwitterSentiment"

version := "1.0"

scalaVersion := "2.10.4"

// https://mvnrepository.com/artifact/org.apache.spark/spark-core_2.10
libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.6.0"

// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming_2.10
libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "1.6.0"

// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming-twitter_2.10
libraryDependencies += "org.apache.spark" % "spark-streaming-twitter_2.10" % "1.6.0"

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2" artifacts (Artifact("stanford-corenlp", "models"), Artifact("stanford-corenlp"))
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"