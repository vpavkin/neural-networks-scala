package ru.pavkin.ml.exercises

import breeze.linalg.{Vector => _, _}
import breeze.numerics._
import breeze.stats.distributions.Rand
import ru.pavkin.ml.common._
import ru.pavkin.ml.mnist.ImageDataLoader

import scala.collection.mutable.ListBuffer

class OptimizedLayeredNetwork(
  val biases: Vector[DenseVector[Double]],
  val weights: Vector[DenseMatrix[Double]]) {

  // including input layer
  def numberOfLayers: Int = biases.size + 1

  def activation(input: DenseVector[Double], layerIndex: Int): DenseVector[Double] =
    sigmoid(weightedInput(input, layerIndex))

  def weightedInput(input: DenseVector[Double], layerIndex: Int): DenseVector[Double] =
    (weights(layerIndex) * input) + biases(layerIndex)

  def feedForward(inputLayerActivations: DenseVector[Double]): DenseVector[Double] =
    (0 until numberOfLayers - 1).foldLeft(inputLayerActivations)(activation)


  def MSE(inputs: List[TrainingInput]): Double =
    inputs.foldLeft(0.0)(_ + singleInputMSEBase(_)) / (2 * inputs.size)

  def sgd(
    trainingData: Vector[TrainingInput],
    numberOfEpochs: Int,
    batchSize: Int,
    learningRate: Double,
    testData: Vector[TrainingInput]): Unit =
    (0 until numberOfEpochs)
      .foreach { epochIndex =>
        sgdEpoch(trainingData, batchSize, learningRate)
        if (testData.nonEmpty)
          evaluateEpochResults(epochIndex, testData)
      }


  // private

  private def singleInputMSEBase(input: TrainingInput): Double =
    norm(feedForward(input.input) - input.correctResult)


  private def sgdEpoch(
    trainingData: Vector[TrainingInput],
    batchSize: Int,
    learningRate: Double): Unit = {
    val shuffledData: Vector[TrainingInput] = scala.util.Random.shuffle(trainingData)
    (0 until (trainingData.size / batchSize)).foreach(batchIndex =>
      sgdBatch(shuffledData.slice(batchSize * batchIndex, batchSize * (batchIndex + 1)), learningRate)
    )
  }

  private def sgdBatch(
    batch: Vector[TrainingInput],
    learningRate: Double): Unit = {
    val nablaB = biases.map(v => DenseVector.zeros[Double](v.size))
    val nablaW = weights.map(m => DenseMatrix.zeros[Double](m.rows, m.cols))
    batch.foreach { input =>
      val (deltaW, deltaB) = backprop(input)
      nablaB.zip(deltaB).foreach { case (b, dB) => b :+= dB }
      nablaW.zip(deltaW).foreach { case (w, dW) => w :+= dW }
    }
    biases.zip(nablaB).foreach { case (b, nb) => b :-= (nb * (learningRate / batch.size)) }
    weights.zip(nablaW).foreach { case (w, nw) => w :-= (nw * (learningRate / batch.size)) }
  }

  private def evaluateEpochResults(
    epochIndex: Int,
    testData: Vector[TrainingInput]): Unit = {
    val guessedCorrectly = testData.foldLeft(0) {
      case (n, input) => n + (if (recognizesCorrectly(input)) 1 else 0)
    }
    println(s"Epoch $epochIndex: $guessedCorrectly/${testData.size}")
  }

  private def costDerivative(
    outputActivations: DenseVector[Double],
    controlVector: DenseVector[Double]) =
    outputActivations - controlVector

  private def backprop(input: TrainingInput): (Vector[DenseMatrix[Double]], Vector[DenseVector[Double]]) = {
    val nablaB = biases.map(v => DenseVector.zeros[Double](v.size)).to[ListBuffer]
    val nablaW = weights.map(m => DenseMatrix.zeros[Double](m.rows, m.cols)).to[ListBuffer]

    // feedforward
    // reversed list of activation vectors (a)
    val initialActivations = List(input.input)
    // reversed list of weighted input vectors (z)
    val initialWeightedInputs = List.empty[DenseVector[Double]]

    val (weightedInputs, activations) = (0 until numberOfLayers - 1)
      .foldLeft((initialWeightedInputs, initialActivations)) {
        case ((accWI, accAct), layerIndex) =>
          val z = weightedInput(accAct.head, layerIndex)
          val nextActivation = sigmoid(z)
          (z :: accWI, nextActivation :: accAct)
      }
    // backward pass
    var delta: DenseVector[Double] =
      costDerivative(activations.head, input.correctResult) * sigmoidPrime(weightedInputs.head)
    nablaB(nablaB.size - 1) = delta
    nablaW(nablaW.size - 1) = delta * activations(1).t

    (1 until numberOfLayers - 1).foreach { invLayerIndex =>
      val z = weightedInputs(invLayerIndex)
      val sp = sigmoidPrime(z)
      delta = (weights(weights.size - invLayerIndex).t * delta) * sp
      nablaB(nablaB.size - 1 - invLayerIndex) = delta
      nablaW(nablaW.size - 1 - invLayerIndex) = delta * activations(invLayerIndex + 1).t
    }
    (nablaW.toVector, nablaB.toVector)
  }

  private def recognizesCorrectly(input: TrainingInput): Boolean =
    argmax(feedForward(input.input)) == argmax(input.correctResult)
}

object OptimizedLayeredNetwork {
  def generate(layerSizes: List[Int], distribution: Rand[Double] = Rand.gaussian) =
    new OptimizedLayeredNetwork(

      // tail because input layer doesn't have biases
      biases = layerSizes.tail
        .map(layerSize => DenseVector.rand[Double](layerSize, Rand.gaussian))
        .toVector,

      // row - activation weights for one neuron
      // column - neuron weights for one activation output
      weights = layerSizes.zip(layerSizes.tail).map {
        case (l1, l2) => DenseMatrix.rand[Double](l2, l1, Rand.gaussian)
      }.toVector
    )
}

object ImageRecognition extends App {

  val mnistData = ImageDataLoader.loadTestDataFromResources.getOrElse(throw new Exception("Failed to load test data"))
  val (trainingData, testData) = mnistData.splitAt(50000)
  val net = OptimizedLayeredNetwork.generate(List(28 * 28, 30, 10))
  net.sgd(trainingData.map(_.toTrainingInput).toVector, 25, 10, 2.0, testData.map(_.toTrainingInput).toVector)

}
