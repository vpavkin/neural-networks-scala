package ru.pavkin.ml.exercises

import ru.pavkin.ml.core._

trait MNISTTrainingScenario {

  val (trainingData, validationData) = loadAndSplitMNISTData

  val activationFunction: DifferentiableActivationFunction = SigmoidActivationFunction

  def net: TrainableLayeredNetwork
  def costFunction: CostFunction
  def regularization: Regularization = NoRegularization

  def monitoring = Monitoring(
    net, costFunction, regularization,
    evaluationPredicate = imageRecognized(net))

  def training = StochasticGradientDescentTraining(
    net,
    costFunction,
    trainingData.map(_.toTrainingInput).toVector,
    maxNumberOfEpochs = 30,
    batchSize = 10,
    learningRate = 0.5,
    regularization = regularization
  )

  def runScenario(): Vector[MonitoringResult] = {

    val trainingInstance = training

    val monitoringData: Vector[MonitoringResult] = trainingInstance.trainAndEvaluate(
      validationData.map(_.toTrainingInput).toVector,
      monitoring)

    println()
    println("Scenario complete!")
    println(monitoringData)

    monitoringData
  }
}
