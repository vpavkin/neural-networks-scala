package ru.pavkin.ml.common

import breeze.linalg.DenseVector

trait CostFunction {
  def derivative(
    networkResult: DenseVector[Double],
    correctResult: DenseVector[Double]): DenseVector[Double]

}
