package ru.pavkin.ml.common

import breeze.linalg.{DenseMatrix, DenseVector}
import breeze.stats.distributions.Rand

/**
  * Network that exposes it's neurons for training modifications
  *
  * @param biases             Biases vectors for each layer (except input layer)
  * @param weights            Weight matrices for each layer (except input layer).
  *                           Each row represents single neuron - it stores weights for all connections from previous layer to this neuron.
  *                           Each column represents all weights for connections from particular neuron of previous layer to current layer.
  * @param activationFunction activation function and it's derivative
  */
class TrainableLayeredNetwork(
  val biases: Vector[DenseVector[Double]],
  val weights: Vector[DenseMatrix[Double]],
  val activationFunction: DifferentiableActivationFunction) extends LayeredNetwork {

  require(biases.size == weights.size)

  def numberOfLayers: Int = biases.size

  def weightedInput(input: DenseVector[Double], layerIndex: Int): DenseVector[Double] =
    (weights(layerIndex) * input) + biases(layerIndex)
}

object TrainableLayeredNetwork {
  def initializeWithLargeWeights(
    layerSizes: List[Int],
    activationFunction: DifferentiableActivationFunction,
    distribution: Rand[Double] = Rand.gaussian): TrainableLayeredNetwork = {
    val (biases, weights) = generateNeurons(layerSizes, distribution)
    new TrainableLayeredNetwork(biases, weights, activationFunction)
  }

  def initializeWithNormalizedWeights(
    layerSizes: List[Int],
    activationFunction: DifferentiableActivationFunction,
    distribution: Rand[Double] = Rand.gaussian): TrainableLayeredNetwork = {
    val (biases, weights) = generateNeurons(layerSizes, distribution)
    new TrainableLayeredNetwork(biases, weights.map(w => w / Math.sqrt(w.cols.toDouble)), activationFunction)
  }

  private def generateNeurons(
    layerSizes: List[Int],
    distribution: Rand[Double]): (Vector[DenseVector[Double]], Vector[DenseMatrix[Double]]) = {
    val biases = layerSizes.tail.map(layerSize => DenseVector.rand[Double](layerSize, distribution)).toVector
    val weights = layerSizes.zip(layerSizes.tail).map {
      case (inputs, layer) => DenseMatrix.rand[Double](layer, inputs, distribution)
    }.toVector

    (biases, weights)
  }

}
