package ru.pavkin.ml.exercises

import breeze.linalg.{Vector => _, _}
import ru.pavkin.ml.common._

/**
  * Main exercise from chapters 1-2.
  * Image recognition using layered neural network trained with stochastic gradient descent.
  */
object ImageRecognitionQuadraticCost extends App with RecognitionScenario {

  val net = TrainableLayeredNetwork.initializeWithLargeWeights(List(28 * 28, 100, 10), activationFunction)

  val costFunction = new QuadraticCostFunction(activationFunction)

  runScenario()
}
