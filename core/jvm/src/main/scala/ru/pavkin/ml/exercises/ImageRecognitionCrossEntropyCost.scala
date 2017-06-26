package ru.pavkin.ml.exercises

import breeze.linalg.{Vector => _, _}
import ru.pavkin.ml.core._

/**
  * Image recognition, updated to use CrossEntropy cost function (chapter 3)
  */
object ImageRecognitionCrossEntropyCost extends App with MNISTTrainingScenario {

  val net = TrainableLayeredNetwork.initializeWithLargeWeights(List(28 * 28, 100, 10), activationFunction)

  val costFunction = CrossEntropyCostFunction

  runScenario()
}
