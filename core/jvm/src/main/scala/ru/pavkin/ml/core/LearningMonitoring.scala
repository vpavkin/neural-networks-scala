package ru.pavkin.ml.core

import cats.Show
import cats.kernel.Monoid
import ru.pavkin.ml.core.Monitoring.Configuration

sealed trait LearningMonitoring {
  type Result

  implicit def resultMonoid: Monoid[Result]
  implicit def resultShow: Show[Result]

  def monitorEpoch(
    trainingData: Vector[TrainingInput],
    evaluationData: Vector[TrainingInput]): Result

  /**
    * Checks for early stop conditions
    *
    * @param accumulatedResults monitoring results accumulated so far
    */
  def shouldStopEarlier(accumulatedResults: Result): Boolean
}

object NoMonitoring extends LearningMonitoring {
  type Result = Unit
  implicit def resultMonoid: Monoid[Result] = cats.instances.unit.catsKernelStdAlgebraForUnit
  implicit def resultShow: Show[Result] = cats.instances.unit.catsStdShowForUnit
  def monitorEpoch(
    trainingData: Vector[TrainingInput],
    evaluationData: Vector[TrainingInput]): Unit = ()
  def shouldStopEarlier(accumulatedResults: Unit): Boolean = false
}

case class Monitoring(
  network: TrainableLayeredNetwork,
  costFunction: CostFunction,
  regularization: Regularization,
  evaluationPredicate: TrainingInput => Boolean,
  configuration: Configuration = Configuration()) extends LearningMonitoring {

  type Result = Vector[MonitoringResult]

  implicit def resultMonoid: Monoid[Result] = cats.instances.vector.catsKernelStdMonoidForVector
  implicit def resultShow: Show[Result] = cats.instances.vector.catsStdShowForVector[MonitoringResult]

  def monitorEpoch(
    trainingData: Vector[TrainingInput],
    evaluationData: Vector[TrainingInput]): Vector[MonitoringResult] = Vector(MonitoringResult(
    if (configuration.monitorTrainingCost) Some(totalCost(trainingData)) else None,
    if (configuration.monitorTrainingAccuracy) Some((accuracy(trainingData), trainingData.size)) else None,
    if (configuration.monitorTrainingCost) Some(totalCost(evaluationData)) else None,
    if (configuration.monitorTrainingCost) Some((accuracy(evaluationData), evaluationData.size)) else None
  ))

  def shouldStopEarlier(accumulatedResults: Vector[MonitoringResult]): Boolean =
    configuration.monitorEvaluationAccuracy &&
      configuration.stopIfNoImprovementInLastNEpochs.fold(false) { n =>
        lazy val last10 = accumulatedResults.takeRight(n)
        accumulatedResults.size > n &&
          last10.head.evaluationDataRecognitionPercentage.exists(
            _ >= last10.tail.flatMap(_.evaluationDataRecognitionPercentage).max)
      }

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
    monitorTrainingAccuracy: Boolean = true,
    stopIfNoImprovementInLastNEpochs: Option[Int] = None)


}
