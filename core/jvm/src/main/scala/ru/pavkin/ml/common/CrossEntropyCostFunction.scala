package ru.pavkin.ml.common

import breeze.linalg.DenseVector
import breeze.linalg.sum
import breeze.numerics.log

/**
  * Cross-entropy cost function.
  *
  * C = (−1/n) * ∑[ y * ln(a) + (1−y) * ln(1−a) ]
  * where `a` is network activation and `y` is correct result for training input
  **/
object CrossEntropyCostFunction extends CostFunction {
  def cost(
    networkActivation: DenseVector[Double],
    correctResult: DenseVector[Double]): Double =
    sum(
      nanToZero(
        (-correctResult) * log(networkActivation) - (1.0 - correctResult) * log(1.0 - networkActivation)
      )
    )


  def outputLayerDelta(
    networkActivation: DenseVector[Double],
    outputLayerWeightedInput: DenseVector[Double],
    correctResult: DenseVector[Double]): DenseVector[Double] =
    networkActivation - correctResult
}
