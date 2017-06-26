package ru.pavkin.ml.core

import breeze.linalg.DenseVector

case class TrainingInput(input: DenseVector[Double], correctResult: DenseVector[Double])
