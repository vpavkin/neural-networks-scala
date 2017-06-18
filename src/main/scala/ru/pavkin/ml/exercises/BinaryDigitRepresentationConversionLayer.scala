package ru.pavkin.ml.exercises

import breeze.linalg.{DenseMatrix, DenseVector}
import ru.pavkin.ml.common._

/**
  * Solution for exercise of transforming digit recognition network output to just for neurons,
  * emitting binary representation of the digit.
  *
  * Here we just construct this last transformation layer and use possible network outputs as inputs.
  *
  * Input weights are designed to pass 1 further for digits that have 1 in that position of binary repr.
  */
object BinaryDigitRepresentationConversionLayer extends App {

  // parameters to experiment with

  // zero weight
  val o = 0.0
  // significant weight
  val w = 3000.0
  // filtering bias
  val b = -1000.0

  // one layer, four neurons
  val network = new TrainableLayeredNetwork(
    Vector(DenseVector(b, b, b, b)),
    Vector(
      DenseMatrix(
        (o, o, o, o, o, o, o, o, w, w),
        (o, o, o, o, w, w, w, w, o, o),
        (o, o, w, w, o, o, w, w, o, o),
        (o, w, o, w, o, w, o, w, o, w)
      )
    ),
    activationFunction = SigmoidActivation
  )

  // test for all possible outbuts
  (0 until 10).foreach { n =>
    val inputs = DenseVector.fill[Double](10)(0.01)
    inputs(n) = 0.99

    val result = network.feedForward(inputs)

    // calculate decimal representation
    result.dot(DenseVector(8.0, 4.0, 2.0, 1.0))

    println(result.dot(DenseVector(8.0, 4.0, 2.0, 1.0)))

  }

}
