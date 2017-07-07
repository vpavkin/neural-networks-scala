package ru.pavkin.ml.app.frontend.redux.model

import ru.pavkin.ml.protocol._

import scala.util.Try

case class TrainingParameters(
  hiddenLayersSizes: String,
  weightInitializer: WeightInitializer,
  activationFunction: ActivationFunction,
  costFunction: CostFunction,
  maxNumberOfEpochs: String,
  batchSize: String,
  learningRate: String,
  regularization: Regularization,
  monitorEvaluationCost: Boolean,
  monitorEvaluationAccuracy: Boolean,
  monitorTrainingCost: Boolean,
  monitorTrainingAccuracy: Boolean,
  stopIfNoImprovementInLastNEpochs: String) {

  def validate: Try[SGDTrainingConfiguration] = Try(SGDTrainingConfiguration(
    hiddenLayersSizes.split(",").map(_.trim.toInt).toList,
    weightInitializer,
    activationFunction,
    costFunction,
    maxNumberOfEpochs.trim.toInt,
    batchSize.trim.toInt,
    learningRate.trim.toDouble,
    regularization,
    monitorEvaluationCost,
    monitorEvaluationAccuracy,
    monitorTrainingCost,
    monitorTrainingAccuracy,
    if (stopIfNoImprovementInLastNEpochs.trim.isEmpty) None
    else Some(stopIfNoImprovementInLastNEpochs.trim.toInt)
  ))

}

object TrainingParameters {
  val Default = TrainingParameters(
    "30",
    WeightInitializer.Default,
    ActivationFunction.Sigmoid,
    CostFunction.CrossEntropy,
    "30",
    "10",
    "0.5",
    Regularization.L2Regularization,
    monitorEvaluationCost = true,
    monitorEvaluationAccuracy = true,
    monitorTrainingCost = true,
    monitorTrainingAccuracy = true, ""
  )
}
