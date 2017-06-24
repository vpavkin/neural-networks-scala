package ru.pavkin.ml.mnist

import breeze.linalg.{DenseMatrix, DenseVector}
import ru.pavkin.ml.common.TrainingInput

case class Image(pixels: DenseMatrix[Double]) extends AnyVal {
  def printToConsole(): Unit =
    (0 until pixels.rows).foreach { row =>
      (0 until pixels.cols).foreach { column =>
        if (pixels(row, column) > 0.1) print("*") else print(" ")
      }
      println()
    }
}

case class Digit(value: Double) extends AnyVal {
  def toVector: DenseVector[Double] = {
    val labelVector = DenseVector.zeros[Double](10)
    labelVector(value.toInt) = 1.0
    labelVector
  }
}

case class TrainingImage(image: Image, label: Digit) {
  def toTrainingInput: TrainingInput = TrainingInput(image.pixels.toDenseVector, label.toVector)
}
