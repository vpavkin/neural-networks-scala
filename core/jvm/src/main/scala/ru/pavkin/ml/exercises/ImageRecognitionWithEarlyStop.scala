package ru.pavkin.ml.exercises

import breeze.linalg.{Vector => _}
import ru.pavkin.ml.core._

/**
  * Image recognition exercise, updated to use CrossEntropy cost function and L2 Regularization (chapter 3)
  */
object ImageRecognitionWithEarlyStop extends App with MNISTTrainingScenario {

  val net = TrainableLayeredNetwork.initializeWithNormalizedWeights(List(28 * 28, 30, 10), activationFunction)

  val costFunction: CostFunction = CrossEntropyCostFunction

  override val regularization = L2Regularization(net, trainingData.length, 5.0)

  override def monitoring: Monitoring =
    super.monitoring.copy(configuration = Monitoring.Configuration(stopIfNoImprovementInLastNEpochs = Some(5)))

  runScenario()
}
