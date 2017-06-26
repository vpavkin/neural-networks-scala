package ru.pavkin.ml.core

import breeze.generic.{MappingUFunc, UFunc}
import breeze.numerics.sigmoid

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
