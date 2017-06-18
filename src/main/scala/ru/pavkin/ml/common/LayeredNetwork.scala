package ru.pavkin.ml.common

import breeze.linalg.{DenseMatrix, DenseVector}
import breeze.stats.distributions.Rand

import scala.collection.immutable.Vector

trait LayeredNetwork {

  /**
    * Number of layers, including input layer
    */
  def numberOfLayers: Int

  def activationFunction: DifferentiableActivationFunction

  def weightedInput(input: DenseVector[Double], layerIndex: Int): DenseVector[Double]

  def activation(
    input: DenseVector[Double],
    layerIndex: Int): DenseVector[Double] =
    activationFunction(weightedInput(input, layerIndex))

  def feedForward(inputLayerActivations: DenseVector[Double]): DenseVector[Double] =
    (0 until numberOfLayers - 1).foldLeft(inputLayerActivations)(activation)
}

class TrainableLayeredNetwork(
  val biases: Vector[DenseVector[Double]],
  val weights: Vector[DenseMatrix[Double]],
  val activationFunction: DifferentiableActivationFunction) extends LayeredNetwork {

  def numberOfLayers: Int = biases.size + 1

  def weightedInput(input: DenseVector[Double], layerIndex: Int): DenseVector[Double] =
    (weights(layerIndex) * input) + biases(layerIndex)
}

object LayeredNetwork {

  def generate(
    layerSizes: List[Int],
    activationFunction: DifferentiableActivationFunction,
    distribution: Rand[Double] = Rand.gaussian) = new TrainableLayeredNetwork(

    // tail because input layer doesn't have biases
    biases = layerSizes.tail
      .map(layerSize => DenseVector.rand[Double](layerSize, Rand.gaussian))
      .toVector,

    // row - activation weights for one neuron
    // column - neuron weights for one activation output
    weights = layerSizes.zip(layerSizes.tail).map {
      case (l1, l2) => DenseMatrix.rand[Double](l2, l1, Rand.gaussian)
    }.toVector,

    activationFunction = activationFunction
  )
}
