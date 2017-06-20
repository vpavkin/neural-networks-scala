package ru.pavkin.ml.exercises

import breeze.linalg.{Vector => _, _}
import ru.pavkin.ml.common._
import ru.pavkin.ml.mnist.ImageDataLoader

/**
  * Main image recognition exercise, updated to use CrossEntropy cost function (chapter 3)
  */
object ImageRecognitionCrossEntropyCost extends App {

  val mnistData = ImageDataLoader.loadTestDataFromResources.getOrElse(throw new Exception("Failed to load test data"))
  val (trainingInputs, validationInputs) = mnistData.splitAt(50000)

  val activationFunction: DifferentiableActivationFunction = SigmoidActivationFunction

  val net = TrainableLayeredNetwork.generate(List(28 * 28, 100, 10), activationFunction)

  val training = new StochasticGradientDescentTraining(
    net,
    CrossEntropyCostFunction,
    trainingInputs.map(_.toTrainingInput).toVector,
    numberOfEpochs = 10,
    batchSize = 10,
    learningRate = 0.5)

  training.trainAndValidate(
    validationInputs.map(_.toTrainingInput).toVector,
    input => argmax(net.feedForward(input.input)) == argmax(input.correctResult))
}
