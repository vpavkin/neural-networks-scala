package ru.pavkin.ml.common

import breeze.generic.{MappingUFunc, UFunc}

object nanToZero extends UFunc with MappingUFunc {

  implicit object nanToZeroImplDouble extends Impl[Double, Double] {
    def apply(x: Double): Double = if (x.isNaN) 0.0 else x
  }
}
