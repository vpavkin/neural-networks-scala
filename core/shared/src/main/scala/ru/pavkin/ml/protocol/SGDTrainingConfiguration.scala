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
  stopIfNoImprovementInLastNEpochs: Option[Int])
