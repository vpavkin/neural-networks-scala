package ru.pavkin.ml.app.backend.learning

import breeze.stats.distributions.Rand
import ru.pavkin.ml.core._
import ru.pavkin.ml.exercises.MNISTTrainingScenario
import ru.pavkin.ml.protocol
import ru.pavkin.ml.protocol.ActivationFunction.Sigmoid
import ru.pavkin.ml.protocol.CostFunction.{CrossEntropy, Quadratic}
import ru.pavkin.ml.protocol.{SGDTrainingConfiguration, WeightInitializer}

class ConfigurableTrainingScenario(parameters: SGDTrainingConfiguration) extends MNISTTrainingScenario {

  override val activationFunction: DifferentiableActivationFunction = parameters.activationFunction match {
    case Sigmoid => SigmoidActivationFunction
  }

  override val costFunction: CostFunction = parameters.costFunction match {
    case Quadratic => new QuadraticCostFunction(activationFunction)
    case CrossEntropy => CrossEntropyCostFunction
  }

  val initializer: (List[Int], DifferentiableActivationFunction, Rand[Double]) => TrainableLayeredNetwork =
    parameters.weightInitializer match {
      case WeightInitializer.Default => TrainableLayeredNetwork.initializeWithNormalizedWeights
      case WeightInitializer.Large => TrainableLayeredNetwork.initializeWithLargeWeights
    }

  val net: TrainableLayeredNetwork =
    initializer((28 * 28 :: parameters.hiddenLayersSizes) :+ 10, activationFunction, Rand.gaussian)

  override def regularization: Regularization = parameters.regularization match {
    case protocol.Regularization.NoRegularization => NoRegularization
    case protocol.Regularization.L2Regularization => L2Regularization(net, trainingData.length, 5.0)
  }

  override def monitoring: Monitoring =
    super.monitoring.copy(configuration = Monitoring.Configuration(
      monitorEvaluationCost = parameters.monitorEvaluationCost,
      monitorEvaluationAccuracy = parameters.monitorEvaluationAccuracy,
      monitorTrainingCost = parameters.monitorTrainingCost,
      monitorTrainingAccuracy = parameters.monitorTrainingAccuracy,
      stopIfNoImprovementInLastNEpochs = parameters.stopIfNoImprovementInLastNEpochs
    ))

  override def training: StochasticGradientDescentTraining =
    super.training.copy(
      maxNumberOfEpochs = parameters.maxNumberOfEpochs,
      batchSize = parameters.batchSize,
      learningRate = parameters.learningRate
    )
}
