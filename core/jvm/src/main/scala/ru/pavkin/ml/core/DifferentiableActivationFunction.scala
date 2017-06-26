package ru.pavkin.ml.core

import breeze.linalg.DenseVector

trait DifferentiableActivationFunction {
  def apply(x: DenseVector[Double]): DenseVector[Double]
  def derivative(x: DenseVector[Double]): DenseVector[Double]
}
