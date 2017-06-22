package ru.pavkin.ml.common

import cats.Show
import cats.kernel.Monoid
import ru.pavkin.ml.common.Monitoring.Configuration

sealed trait LearningMonitoring {
  type Result

  implicit def resultMonoid: Monoid[Result]
  implicit def resultShow: Show[Result]

  def monitorEpoch(
    trainingData: Vector[TrainingInput],
    evaluationData: Vector[TrainingInput]): Result
}

object NoMonitoring extends LearningMonitoring {
  type Result = Unit
  implicit def resultMonoid: Monoid[Result] = cats.instances.unit.catsKernelStdAlgebraForUnit
  implicit def resultShow: Show[Result] = cats.instances.unit.catsStdShowForUnit
  def monitorEpoch(
    trainingData: Vector[TrainingInput],
    evaluationData: Vector[TrainingInput]): Unit = ()
}

case class Monitoring(
  network: TrainableLayeredNetwork,
  costFunction: CostFunction,
  regularization: Regularization,
  evaluationPredicate: TrainingInput => Boolean,
  configuration: Configuration = Configuration()) extends LearningMonitoring {

  type Result = Vector[Monitoring.Result]

  implicit def resultMonoid: Monoid[Result] = cats.instances.vector.catsKernelStdMonoidForVector
  implicit def resultShow: Show[Result] = cats.instances.vector.catsStdShowForVector[Monitoring.Result]

  def monitorEpoch(
    trainingData: Vector[TrainingInput],
    evaluationData: Vector[TrainingInput]): Vector[Monitoring.Result] = Vector(Monitoring.Result(
    if (configuration.monitorTrainingCost) Some(totalCost(trainingData)) else None,
    if (configuration.monitorTrainingAccuracy) Some((accuracy(trainingData), trainingData.size)) else None,
    if (configuration.monitorTrainingCost) Some(totalCost(evaluationData)) else None,
    if (configuration.monitorTrainingCost) Some((accuracy(evaluationData), evaluationData.size)) else None
  ))

  private def accuracy(data: Vector[TrainingInput]) =
    data.count(evaluationPredicate)

  private def totalCost(data: Vector[TrainingInput]) = data.foldLeft(0.0)(_ + inputCost(_))

  private def inputCost(input: TrainingInput) =
    regularization.modifyCost(costFunction.cost(network.feedForward(input.input), input.correctResult))

}

object Monitoring {

  case class Configuration(
    monitorEvaluationCost: Boolean = true,
    monitorEvaluationAccuracy: Boolean = true,
    monitorTrainingCost: Boolean = true,
    monitorTrainingAccuracy: Boolean = true)

  case class Result(
    costOnTrainingData: Option[Double],
    accuracyOnTrainingData: Option[(Int, Int)],
    costOnEvaluationData: Option[Double],
    accuracyOnEvaluationData: Option[(Int, Int)]) {
    def print: String = List(
      costOnTrainingData.map(c => s"Cost on training data: $c"),
      costOnEvaluationData.map(c => s"Cost on evaluation data: $c"),
      accuracyOnTrainingData.map { case (correct, outOf) => s"Accuracy on training data: $correct/$outOf" },
      accuracyOnEvaluationData.map { case (correct, outOf) => s"Accuracy on evaluation data: $correct/$outOf" }
    ).flatten.mkString("\n")
  }

  object Result {
    implicit def show: Show[Result] = Show.show(result =>
      List(
        result.costOnTrainingData.map(c => s"Cost on training data: $c"),
        result.costOnEvaluationData.map(c => s"Cost on evaluation data: $c"),
        result.accuracyOnTrainingData.map { case (correct, outOf) => s"Accuracy on training data: $correct/$outOf [${Math.round(10000.0 * correct / outOf).toDouble / 100}%]" },
        result.accuracyOnEvaluationData.map { case (correct, outOf) => s"Accuracy on evaluation data: $correct/$outOf [${Math.round(10000.0 * correct / outOf).toDouble / 100}%]" }
      ).flatten.mkString("\n")
    )
  }
}
