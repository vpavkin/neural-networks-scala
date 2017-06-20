package ru.pavkin.ml.common

import breeze.linalg.{DenseVector, norm}
import breeze.numerics.pow

/**
  * Quadratic cost function or MSE (Mean Squared Error).
  *
  * C = 0.5 * ||y - a||^2
  * where `a` is network activation and `y` is correct result for training input
  **/
class QuadraticCostFunction(
  activationFunction: DifferentiableActivationFunction) extends CostFunction {

  def cost(
    networkActivation: DenseVector[Double],
    correctResult: DenseVector[Double]): Double = 0.5 * pow(norm(networkActivation - correctResult), 2.0)

  def outputLayerDelta(
    networkActivation: DenseVector[Double],
    outputLayerWeightedInput: DenseVector[Double],
    correctResult: DenseVector[Double]): DenseVector[Double] =
    (networkActivation - correctResult) * activationFunction.derivative(outputLayerWeightedInput)
}
