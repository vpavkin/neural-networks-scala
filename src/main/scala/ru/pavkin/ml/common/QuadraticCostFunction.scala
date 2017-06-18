package ru.pavkin.ml.common

import breeze.linalg.DenseVector

/**
  * Quadratic cost function or MSE (Mean Squared Error).
  *
  * C = 0.5 * (y - a)^2
  * where `a` is network activation and `y` is correct result for training input
  **/
object QuadraticCostFunction extends CostFunction {
  def derivative(
    networkResult: DenseVector[Double],
    correctResult: DenseVector[Double]): DenseVector[Double] =
    networkResult - correctResult
}
