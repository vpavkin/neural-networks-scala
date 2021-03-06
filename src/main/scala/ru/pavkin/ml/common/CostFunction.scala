package ru.pavkin.ml.common

import breeze.linalg.DenseVector

trait CostFunction {
  def cost(
    networkActivation: DenseVector[Double],
    correctResult: DenseVector[Double]): Double

  def outputLayerDelta(
    networkActivation: DenseVector[Double],
    outputLayerWeightedInput: DenseVector[Double],
    correctResult: DenseVector[Double]): DenseVector[Double]
}
