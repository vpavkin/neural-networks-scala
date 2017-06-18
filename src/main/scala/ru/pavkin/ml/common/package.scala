package ru.pavkin.ml

import breeze.generic.{MappingUFunc, UFunc}
import breeze.linalg.DenseVector
import breeze.numerics.sigmoid

package object common {

  object sigmoidPrime extends UFunc with MappingUFunc {

    implicit object sigmoidPrimeImplInt extends Impl[Int, Double] {
      def apply(x: Int): Double = sigmoid(x) * (1d - sigmoid(x))
    }

    implicit object sigmoidPrimeImplDouble extends Impl[Double, Double] {
      def apply(x: Double): Double = sigmoid(x) * (1f - sigmoid(x))
    }

    implicit object sigmoidPrimeImplFloat extends Impl[Float, Float] {
      def apply(x: Float): Float = sigmoid(x) * (1f - sigmoid(x))
    }
  }

  object SigmoidActivation extends DifferentiableActivationFunction {
    def apply(x: DenseVector[Double]): DenseVector[Double] = sigmoid(x)
    def derivative(x: DenseVector[Double]): DenseVector[Double] = sigmoidPrime(x)
  }
}
