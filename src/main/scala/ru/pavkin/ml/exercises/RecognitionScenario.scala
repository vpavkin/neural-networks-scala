package ru.pavkin.ml.exercises

import ru.pavkin.ml.common._

trait RecognitionScenario {

  val (trainingData, validationData) = loadAndSplitMNISTData

  val activationFunction: DifferentiableActivationFunction = SigmoidActivationFunction

  def net: TrainableLayeredNetwork
  def costFunction: CostFunction
  def regularization: Regularization = NoRegularization

  def monitoring = Monitoring(
    net, costFunction, regularization,
    evaluationPredicate = imageRecognized(net))

  def training = new StochasticGradientDescentTraining(
    net,
    costFunction,
    trainingData.map(_.toTrainingInput).toVector,
    maxNumberOfEpochs = 30,
    batchSize = 10,
    learningRate = 0.5,
    regularization = regularization
  )

  def runScenario(): Unit = {

    val trainingInstance = training

    val monitoringData: Vector[Monitoring.Result] = trainingInstance.trainAndEvaluate(
      validationData.map(_.toTrainingInput).toVector,
      monitoring)

    println()
    println("Scenario complete!")
    println(monitoringData)

  }
}
