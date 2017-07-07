package ru.pavkin.ml.app.frontend.redux.handlers

import diode.{ActionHandler, ActionResult, ModelRW}
import ru.pavkin.ml.app.frontend.redux.actions.UpdateTrainingParametersForm
import ru.pavkin.ml.app.frontend.redux.model.TrainingParameters

class TrainingParametersFormHandler[M](
  modelRW: ModelRW[M, TrainingParameters])
  extends ActionHandler(modelRW) {

  override protected def handle: PartialFunction[Any, ActionResult[M]] = {
    case e: UpdateTrainingParametersForm =>
      updated(e.newState)
  }

}
