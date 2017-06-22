package ru.pavkin.ml.exercises

import breeze.linalg.{Vector => _, _}
import ru.pavkin.ml.common._

/**
  * Image recognition exercise, updated to use CrossEntropy cost function and L2 Regularization (chapter 3)
  */
object ImageRecognitionL2Regularization extends App with RecognitionScenario {

  val net = TrainableLayeredNetwork.initializeWithNormalizedWeights(List(28 * 28, 30, 10), activationFunction)

  val costFunction: CostFunction = CrossEntropyCostFunction

  override val regularization = L2Regularization(net, trainingData.length, 5.0)

  runScenario()
}
