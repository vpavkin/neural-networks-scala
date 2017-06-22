package ru.pavkin.ml.common

import breeze.linalg.{DenseMatrix, DenseVector}

class StochasticGradientDescentTraining(
  network: TrainableLayeredNetwork,
  costFunction: CostFunction,
  trainingData: Vector[TrainingInput],
  numberOfEpochs: Int,
  batchSize: Int,
  learningRate: Double,
  regularization: Regularization = NoRegularization) {

  import network._

  // just run the training
  def train(): Unit =
    (0 until numberOfEpochs).foreach { _ => runEpoch() }

  // run the training and use validation inputs after each epoch to evaluate intermediate results
  def trainAndEvaluate(
    evaluationData: Vector[TrainingInput],
    monitoring: LearningMonitoring = NoMonitoring): monitoring.Result =
    (0 until numberOfEpochs).foldLeft(monitoring.resultMonoid.empty) { case (result, epochIndex) =>
      runEpoch()
      println()
      println(s"Training epoch $epochIndex complete.")
      val epochMonitoringResult = monitoring.monitorEpoch(trainingData, evaluationData)
      println(monitoring.resultShow.show(epochMonitoringResult))
      monitoring.resultMonoid.combine(result, epochMonitoringResult)
    }

  // run single training epoch: split shuffed inputs into mini-batches and perform gradient descent for each mini-batch
  private def runEpoch(): Unit = {
    val shuffledInputs: Vector[TrainingInput] = scala.util.Random.shuffle(trainingData)

    (0 until (trainingData.size / batchSize)).foreach(batchIndex =>
      runBatch(shuffledInputs.slice(batchSize * batchIndex, batchSize * (batchIndex + 1)))
    )
  }

  private def runBatch(batch: Vector[TrainingInput]): Unit = {
    // initialize gradient accumulators with zeroes
    val biasesGradient = biases.map(v => DenseVector.zeros[Double](v.size))
    val weightsGradient = weights.map(m => DenseMatrix.zeros[Double](m.rows, m.cols))

    // for each element of the mini-batch calculate the gradient using backpropagation and
    // add the result to the total gradient of the mini-batch
    batch.foreach { input =>
      val originalGradient =
        new BackPropagation(network, costFunction).gradient(input)

      val gradient = originalGradient
        .copy(weightsDerivative = regularization.modifyWeightGradient(originalGradient.weightsDerivative))

      biasesGradient.zip(gradient.biasesDerivative).foreach { case (b, dB) => b :+= dB }
      weightsGradient.zip(gradient.weightsDerivative).foreach { case (w, dW) => w :+= dW }
    }

    // update network biases using calculated gradient, boosted by the learning rate.
    val scaledLearningRate = learningRate / batch.size
    biases.zip(biasesGradient).foreach { case (b, nb) => b :-= (nb * scaledLearningRate) }
    weights.zip(weightsGradient).foreach { case (w, nw) => w :-= (nw * scaledLearningRate) }
  }
}

