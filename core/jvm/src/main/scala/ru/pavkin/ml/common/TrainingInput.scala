package ru.pavkin.ml.common

import breeze.linalg.DenseVector

case class TrainingInput(input: DenseVector[Double], correctResult: DenseVector[Double])
