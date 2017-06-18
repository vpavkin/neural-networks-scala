package ru.pavkin.ml.exercises

import breeze.linalg.DenseVector

case class Neuron(weights: DenseVector[Double], bias: Double)
case class Layer(neurons: List[Neuron])

trait LayeredNetwork {
  def layers: List[Layer]
  def activation(
    inputs: DenseVector[Double],
    weights: DenseVector[Double],
    bias: Double): Double

  def feedForward(inputs: DenseVector[Double]): DenseVector[Double] = layers.foldLeft(inputs) {
    case (input, layer) => DenseVector(layer.neurons.map(n => activation(input, n.weights, n.bias)).toArray)
  }
}

case class PerceptronNetwork(layers: List[Layer]) extends LayeredNetwork {
  def activation(
    inputs: DenseVector[Double],
    weights: DenseVector[Double],
    bias: Double): Double = if (inputs.dot(weights) + bias < 0) 0.0 else 1.0
}

case class SigmoidNetwork(layers: List[Layer]) extends LayeredNetwork {
  def activation(
    inputs: DenseVector[Double],
    weights: DenseVector[Double],
    bias: Double): Double = 1 / (1 + math.exp(-inputs.dot(weights) - bias))
}

object DigitsToBinary extends App {

  val o = 0.0
  val w = 3000.0
  val b = -1000.0

  val network = SigmoidNetwork(List(Layer(List(
    Neuron(DenseVector(o, o, o, o, o, o, o, o, w, w), b),
    Neuron(DenseVector(o, o, o, o, w, w, w, w, o, o), b),
    Neuron(DenseVector(o, o, w, w, o, o, w, w, o, o), b),
    Neuron(DenseVector(o, w, o, w, o, w, o, w, o, w), b)
  ))))

  (0 until 10).foreach { n =>
    val inputs = DenseVector.fill[Double](10)(0.01)
    inputs(n) = 0.99
    //    println(s"Feeding input: $inputs")
    val result = network.feedForward(inputs)
    result.dot(DenseVector(8.0, 4.0, 2.0, 1.0))
    //    println(s"Result: ${result}")
    println(result.dot(DenseVector(8.0, 4.0, 2.0, 1.0)))

  }

}
