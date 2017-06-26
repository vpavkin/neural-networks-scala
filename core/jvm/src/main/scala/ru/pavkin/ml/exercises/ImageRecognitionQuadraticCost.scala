package ru.pavkin.ml.exercises

import breeze.linalg.{Vector => _, _}
import ru.pavkin.ml.core._

/**
  * Image recognition using layered neural network trained with stochastic gradient descent and quadratic cost function.
  * Covers chapter 1-2
  */
object ImageRecognitionQuadraticCost extends App with MNISTTrainingScenario {

  val net = TrainableLayeredNetwork.initializeWithLargeWeights(List(28 * 28, 100, 10), activationFunction)

  val costFunction = new QuadraticCostFunction(activationFunction)

  runScenario()
}
