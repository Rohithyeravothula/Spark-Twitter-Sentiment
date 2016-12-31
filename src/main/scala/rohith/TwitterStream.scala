package rohith

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.twitter.TwitterUtils

/**
  * Created by rohith on 31/12/16.
  */
object TwitterStream {
  def main(args: Array[String]): Unit = {
    val config = new SparkConf().setMaster("local[*]").setAppName("Twitter sentiment analysis")
    val spark = new SparkContext(config)
    val sparkStreaming = new StreamingContext(spark, Seconds(5))

    System.setProperty("twitter4j.oauth.consumerKey", "")
    System.setProperty("twitter4j.oauth.consumerSecret", "")
    System.setProperty("twitter4j.oauth.accessToken", "")
    System.setProperty("twitter4j.oauth.accessTokenSecret", "")

    spark.setLogLevel("ERROR")
    val twitterStream = TwitterUtils.createStream(sparkStreaming, None)
    val tweetBody = twitterStream.map {
      case tags if tags.getLang == "en" => tags.getText
      case _ => ""
    }
    tweetBody.foreachRDD{ tweetRDD =>
      tweetRDD.foreach{ tweet =>
        val sentiment = SentimentEngine.sentiment(tweet)
        println(s"sentiment for $tweet is $sentiment")
      }
    }
    sparkStreaming.start()
    sparkStreaming.awaitTermination()
  }
}
