package ru.pavkin.ml.core

import cats.Show

case class MonitoringResult(
  costOnTrainingData: Option[Double],
  accuracyOnTrainingData: Option[(Int, Int)],
  costOnEvaluationData: Option[Double],
  accuracyOnEvaluationData: Option[(Int, Int)]) {

  def evaluationDataRecognitionPercentage: Option[Double] =
    accuracyOnEvaluationData.map {
      case (correct, outOf) => correct.toDouble / outOf
    }

  def print: String = List(
    costOnTrainingData.map(c => s"Cost on training data: $c"),
    costOnEvaluationData.map(c => s"Cost on evaluation data: $c"),
    accuracyOnTrainingData.map { case (correct, outOf) => s"Accuracy on training data: $correct/$outOf" },
    accuracyOnEvaluationData.map { case (correct, outOf) => s"Accuracy on evaluation data: $correct/$outOf" }
  ).flatten.mkString("\n")
}

object MonitoringResult {
  implicit val show: Show[MonitoringResult] = Show.show(result =>
    List(
      result.costOnTrainingData.map(c => s"Cost on training data: $c"),
      result.costOnEvaluationData.map(c => s"Cost on evaluation data: $c"),
      result.accuracyOnTrainingData.map { case (correct, outOf) => s"Accuracy on training data: $correct/$outOf [${Math.round(10000.0 * correct / outOf).toDouble / 100}%]" },
      result.accuracyOnEvaluationData.map { case (correct, outOf) => s"Accuracy on evaluation data: $correct/$outOf [${Math.round(10000.0 * correct / outOf).toDouble / 100}%]" }
    ).flatten.mkString("\n")
  )
}
