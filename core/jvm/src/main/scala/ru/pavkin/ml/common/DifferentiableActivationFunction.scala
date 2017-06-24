package ru.pavkin.ml.common

import breeze.linalg.DenseVector

trait DifferentiableActivationFunction {
  def apply(x: DenseVector[Double]): DenseVector[Double]
  def derivative(x: DenseVector[Double]): DenseVector[Double]
}
