package ru.pavkin.ml.common

import breeze.linalg.DenseVector

trait LayeredNetwork {

  /**
    * Number of layers, excluding input layer
    */
  def numberOfLayers: Int

  def activationFunction: DifferentiableActivationFunction

  def weightedInput(input: DenseVector[Double], layerIndex: Int): DenseVector[Double]

  def activation(
    input: DenseVector[Double],
    layerIndex: Int): DenseVector[Double] =
    activationFunction(weightedInput(input, layerIndex))

  def feedForward(inputLayerActivations: DenseVector[Double]): DenseVector[Double] =
    (0 until numberOfLayers).foldLeft(inputLayerActivations)(activation)
}
