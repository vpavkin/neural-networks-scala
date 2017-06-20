package ru.pavkin.ml.exercises

import breeze.linalg.{Vector => _, _}
import ru.pavkin.ml.common._
import ru.pavkin.ml.mnist.ImageDataLoader

/**
  * Main exercise from chapters 1-2.
  * Image recognition using layered neural network trained with stochastic gradient descent.
  */
object ImageRecognitionQuadraticCost extends App {

  val mnistData = ImageDataLoader.loadTestDataFromResources.getOrElse(throw new Exception("Failed to load test data"))
  val (trainingInputs, validationInputs) = mnistData.splitAt(50000)

  val activationFunction: DifferentiableActivationFunction = SigmoidActivationFunction

  val net = TrainableLayeredNetwork.generate(List(28 * 28, 100, 10), activationFunction)

  val training = new StochasticGradientDescentTraining(
    net,
    new QuadraticCostFunction(activationFunction),
    trainingInputs.map(_.toTrainingInput).toVector,
    numberOfEpochs = 10,
    batchSize = 10,
    learningRate = 3.0)

  training.trainAndValidate(
    validationInputs.map(_.toTrainingInput).toVector,
    input => argmax(net.feedForward(input.input)) == argmax(input.correctResult))
}
