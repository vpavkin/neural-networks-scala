package ru.pavkin.ml.core

import breeze.linalg.DenseVector
import breeze.numerics.sigmoid

object SigmoidActivationFunction extends DifferentiableActivationFunction {
  def apply(x: DenseVector[Double]): DenseVector[Double] = sigmoid(x)
  def derivative(x: DenseVector[Double]): DenseVector[Double] = sigmoidPrime(x)
}
