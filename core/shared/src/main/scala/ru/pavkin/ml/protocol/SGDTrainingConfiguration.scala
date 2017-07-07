package ru.pavkin.ml.protocol

case class SGDTrainingConfiguration(
  hiddenLayersSizes: List[Int],
  weightInitializer: WeightInitializer,
  activationFunction: ActivationFunction,
  costFunction: CostFunction,
  maxNumberOfEpochs: Int,
  batchSize: Int,
  learningRate: Double,
  regularization: Regularization,
  monitorEvaluationCost: Boolean,
  monitorEvaluationAccuracy: Boolean,
  monitorTrainingCost: Boolean,
  monitorTrainingAccuracy: Boolean,
  stopIfNoImprovementInLastNEpochs: Option[Int]) {

  require(hiddenLayersSizes.nonEmpty)
  require(hiddenLayersSizes.forall(_ > 0))
  require(maxNumberOfEpochs > 0)
  require(batchSize > 0)
  require(learningRate > 0)
  require(stopIfNoImprovementInLastNEpochs.forall(_ > 0))
}
