package ru.pavkin.ml.core

import breeze.linalg.{DenseMatrix, DenseVector}

import scala.collection.mutable.ListBuffer

/**
  * Calculates gradient vector for specified `costFunction` over layered `network`
  */
class BackPropagation(
  network: TrainableLayeredNetwork,
  costFunction: CostFunction) {

  import network._

  def gradient(input: TrainingInput): BackPropagation.Gradient = {

    val (weightedInputsL, activationsL) = feedForward(input)

    // transform lists into vectors for easier indexed access
    backpropagate(input, weightedInputsL.toVector.reverse, activationsL.toVector.reverse)

  }

  /**
    * Feed forward phase.
    *
    * Calculate all intermediate and final weighted inputs and activations by feeding input forward along the net.
    *
    * @return Tuple of:
    *         - reversed list of weighted input vectors for each layer (except input layer)
    *         - reversed list of activation vectors for each layer (including input layer)
    */
  private def feedForward(input: TrainingInput): (List[DenseVector[Double]], List[DenseVector[Double]]) = {


    // at the beginning we know only input layer activation
    val initialActivations = List(input.input)
    // no weighted inputs known before feed forward phase
    val initialWeightedInputs = List.empty[DenseVector[Double]]

    /** Feed input forward along the layers and build up `z` (weightedInputs) and `a` (activations) lists.
      * For efficiency, lists are built up in reversed order, to take advantage of O(1) prepend */
    (0 until numberOfLayers)
      .foldLeft((initialWeightedInputs, initialActivations)) {
        case ((accZ, accA), layerIndex) =>
          val previousLayerActivation = accA.head
          val thisZ = weightedInput(previousLayerActivation, layerIndex)
          val thisA = activationFunction(thisZ)
          (thisZ :: accZ, thisA :: accA)
      }
  }

  /**
    * Error backpropagation phase.
    *
    * Starting from the very last layer calculate the "error" δ and use it to calculate respective gradient vector components.
    * After that δ is sent to the previous layer and so on.
    */
  private def backpropagate(
    input: TrainingInput,
    weightedInputs: Vector[DenseVector[Double]],
    activations: Vector[DenseVector[Double]]): BackPropagation.Gradient = {

    // Initialize gradient components with zeroes
    val biasesDerivative = biases.map(v => DenseVector.zeros[Double](v.size)).to[ListBuffer]
    val weightsDerivative = weights.map(m => DenseMatrix.zeros[Double](m.rows, m.cols)).to[ListBuffer]

    // calculate "error" for the last layer.
    // e.g. for MSE this will be: δ[L] = ∇C ⊙ σ′(z[L])
    val lastLayerDelta: DenseVector[Double] =
    costFunction.outputLayerDelta(activations.last, weightedInputs.last, input.correctResult)

    // update biases gradient for last layer: δ[L]
    biasesDerivative(numberOfLayers - 1) = lastLayerDelta
    // update weights gradient for last layer δ[L] * a[L].t
    weightsDerivative(numberOfLayers - 1) = lastLayerDelta * activations(activations.size - 2).t

    /** Backpropagate the "error" starting from the layer before the last (numberOfLayers - 1).
      * `nextLayerDelta` is the error that comes back from the next layer (calculated on previous step).
      * `reversedLayerIndex` is the reversed index if the layer that we calculate error for */
    (1 until numberOfLayers).foldLeft(lastLayerDelta) { (nextLayerDelta, reversedLayerIndex) =>

      // normal layer index to calculate error for
      val layerIndex = numberOfLayers - 1 - reversedLayerIndex
      // calculate "error" for this layer: δ[l] = (w[l+1].t * δ[l+1]) ⊙ σ′(z[l])
      val thisDelta = (weights(layerIndex + 1).t * nextLayerDelta) * activationFunction.derivative(weightedInputs(layerIndex))

      // assign respective components of the gradient vector
      biasesDerivative(layerIndex) = thisDelta
      weightsDerivative(layerIndex) = thisDelta * activations(layerIndex).t

      // send current layer "error" back to previous layer (via foldLeft)
      thisDelta
    }

    BackPropagation.Gradient(weightsDerivative.toVector, biasesDerivative.toVector)
  }
}

object BackPropagation {
  /**
    * Cost function gradient vector components:
    *
    * @param weightsDerivative ∂C/∂w components for each weight of each neuron in each layer
    * @param biasesDerivative  ∂C/∂b components for each bias of each neuron in each layer
    */
  case class Gradient(
    weightsDerivative: Vector[DenseMatrix[Double]],
    biasesDerivative: Vector[DenseVector[Double]])
}
