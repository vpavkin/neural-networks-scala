package ru.pavkin.ml.mnist

import java.nio.ByteBuffer

import better.files.File
import breeze.linalg.DenseMatrix

import scala.util.Try


object ImageDataLoader {
  def loadImages(path: String): Try[Array[Image]] = Try {

    val byteArray = File(path).loadBytes
    val bytes = ByteBuffer.wrap(byteArray)

    val controlValue = bytes.getInt(0)
    val imageCount = bytes.getInt(4)
    val width = bytes.getInt(8)
    val height = bytes.getInt(12)

    assert(controlValue == 2051)
    assert(imageCount > 0)
    assert(width == height)

    val pixelsStartFrom = 16
    val bytesPerImage = width * height

    val results = new Array[Image](imageCount)
    (0 until imageCount).foreach { imageIndex =>
      val imageStartsAt = pixelsStartFrom + bytesPerImage * imageIndex
      results(imageIndex) = Image(
        DenseMatrix.create(
          height,
          width,
          byteArray
            .slice(imageStartsAt, imageStartsAt + bytesPerImage)
            .map(b => (b & 0xFF).toDouble / 255.0)
        ).t
      )
    }
    results
  }

  def loadLabels(path: String): Try[Array[Digit]] = Try {
    val byteArray = File(path).loadBytes
    val bytes = ByteBuffer.wrap(byteArray)

    val controlValue = bytes.getInt(0)
    val labelsCount = bytes.getInt(4)

    assert(controlValue == 2049)
    assert(labelsCount > 0)

    val labelsStartFrom = 8

    val results = new Array[Digit](labelsCount)
    (0 until labelsCount).foreach { labelIndex =>
      results(labelIndex) = Digit((byteArray(labelsStartFrom + labelIndex) & 0xFF).toDouble)
    }
    results
  }

  def loadTestDataFromResources: Try[Array[TrainingImage]] = for {
    images <- loadImages("src/main/resources/train-images-idx3-ubyte")
    labels <- loadLabels("src/main/resources/train-labels-idx1-ubyte")
  } yield images.zip(labels).map(TrainingImage.tupled)

}
