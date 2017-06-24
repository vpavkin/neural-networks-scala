package ru.pavkin.ml

import breeze.linalg.argmax
import ru.pavkin.ml.common.{LayeredNetwork, TrainingInput}
import ru.pavkin.ml.mnist.{ImageDataLoader, TrainingImage}

package object exercises {

  def loadAndSplitMNISTData: (Array[TrainingImage], Array[TrainingImage]) = {
    val mnistData = ImageDataLoader.loadTestDataFromResources.getOrElse(throw new Exception("Failed to load test data"))
    mnistData.splitAt(50000)
  }

  def imageRecognized(network: LayeredNetwork)(input: TrainingInput): Boolean =
    argmax(network.feedForward(input.input)) == argmax(input.correctResult)

}
