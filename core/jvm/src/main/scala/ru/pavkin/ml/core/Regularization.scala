package ru.pavkin.ml.core

import breeze.linalg.{DenseMatrix, sum}
import breeze.numerics.pow

sealed trait Regularization {
  def modifyCost(originalCost: Double): Double
  def modifyWeightGradient(
    originalGradients: Vector[DenseMatrix[Double]]): Vector[DenseMatrix[Double]]
}

case object NoRegularization extends Regularization {
  def modifyCost(originalCost: Double): Double = originalCost
  def modifyWeightGradient(
    originalGradients: Vector[DenseMatrix[Double]]): Vector[DenseMatrix[Double]] = originalGradients
}

case class L2Regularization(
  network: TrainableLayeredNetwork,
  trainingSetSize: Int,
  regularizationFactor: Double) extends Regularization {

  def modifyCost(originalCost: Double): Double =
    originalCost + (0.5 * regularizationFactor / trainingSetSize) * network.weights.map(m => sum(pow(m, 2))).sum

  def modifyWeightGradient(
    originalGradients: Vector[DenseMatrix[Double]]): Vector[DenseMatrix[Double]] =
    network.weights.zip(originalGradients)
      .map { case (w, dw) => dw + (regularizationFactor / trainingSetSize) * w }

}
