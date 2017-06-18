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
  def generate(
    layerSizes: List[Int],
    activationFunction: DifferentiableActivationFunction,
    distribution: Rand[Double] = Rand.gaussian): TrainableLayeredNetwork = new TrainableLayeredNetwork(

    biases = layerSizes.tail
      .map(layerSize => DenseVector.rand[Double](layerSize, Rand.gaussian))
      .toVector,

    weights = layerSizes.zip(layerSizes.tail).map {
      case (l1, l2) => DenseMatrix.rand[Double](l2, l1, Rand.gaussian)
    }.toVector,

    activationFunction = activationFunction
  )
}
