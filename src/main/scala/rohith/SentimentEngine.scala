package rohith


import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import rohith.SentimentEngine.SentimentType.Sentiment

import scala.collection.convert.wrapAll._

/**
  * Created by rohith on 31/12/16.
  */

object SentimentEngine {

  val props = new Properties()
  props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
  val pipeline: StanfordCoreNLP = new StanfordCoreNLP(props)

  // in case of multiple sentiments, choose sentiment of lengthiest sentence as main sentiment
  private def extractSentiment(text: String): Int = {
    extractSentiments(text) match {
      case list if list.nonEmpty =>
        val (_, senti) = list.maxBy{
          case (sentence, _) => sentence.length
        }
        senti
      case _ => 2
    }
  }

  private def extractSentiments(text: String): List[(String, Int)] = {
    val annotation: Annotation = pipeline.process(text)
    val sentences = annotation.get(classOf[CoreAnnotations.SentencesAnnotation])
    val annotationTree = sentences.map(sentence => (sentence, sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])))
    val sentimentVal = annotationTree.map{case (sentence, tree) => (sentence.toString, RNNCoreAnnotations.getPredictedClass(tree))}.toList
    sentimentVal
  }

  def sentiment(input:String): Sentiment = {
    input.isEmpty match {
      case true => SentimentType.toSentiment(extractSentiment(input))
      case false => SentimentType.toSentiment(2)
    }
  }

  object SentimentType extends Enumeration {
    type Sentiment = Value
    val POSITIVE, NEGATIVE, NEUTRAL = Value

    def toSentiment(sentiment: Int) : Sentiment = {
      sentiment match {
        case x if x == 0 || x == 1 => SentimentType.NEGATIVE
        case x if x == 2 => SentimentType.NEUTRAL
        case x if x == 3 || x == 4 => SentimentType.POSITIVE
      }
    }
  }
}
