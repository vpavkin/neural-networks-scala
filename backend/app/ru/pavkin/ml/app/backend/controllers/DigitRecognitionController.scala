package ru.pavkin.ml.app.backend.controllers

import play.api.mvc._
import ru.pavkin.ml.protocol.codecs._
import io.circe.parser._
import io.circe.syntax._
import monix.eval.Task
import play.api.libs.json.JsValue
import ru.pavkin.ml.app.backend.learning.ConfigurableTrainingScenario
import ru.pavkin.ml.core.{SigmoidActivationFunction, TrainableLayeredNetwork}
import ru.pavkin.ml.protocol._
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future

class DigitRecognitionController(
  components: ControllerComponents) extends AbstractController(components) {

  // todo: use circe when available
  def train: Action[JsValue] = Action.async(parse.json) { implicit req =>
    decode[SGDTrainingConfiguration](req.body.toString) match {
      case Left(err) => Future.successful(BadRequest(err.getMessage))
      case Right(configuration) =>
        Task(trainNewNetwork(configuration)).map(res => Ok(res.asJson.toString)).runAsync
    }
  }

  def test: Action[AnyContent] = Action {
    Ok(SGDTrainingConfiguration(List(30), WeightInitializer.Default, ActivationFunction.Sigmoid, CostFunction.CrossEntropy,
      30, 10, 0.5, Regularization.L2Regularization, true, true, true, true, Some(10)
    ).asJson.toString())
  }

  private def trainNewNetwork(parameters: SGDTrainingConfiguration) = {
    val scenario = new ConfigurableTrainingScenario(parameters)
    val monitoringData = scenario.runScenario()
    network = scenario.net
    monitoringData
  }

  private var network = TrainableLayeredNetwork.initializeWithNormalizedWeights(
    List(28 * 28, 30, 10),
    SigmoidActivationFunction
  )

}
