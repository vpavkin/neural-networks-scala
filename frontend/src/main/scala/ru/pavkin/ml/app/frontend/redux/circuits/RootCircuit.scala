package ru.pavkin.ml.app.frontend.redux.circuits

import diode.Circuit
import diode.react.ReactConnector
import ru.pavkin.ml.app.frontend.redux.handlers.TrainingParametersFormHandler
import ru.pavkin.ml.app.frontend.redux.model.RootModel

object RootCircuit
  extends Circuit[RootModel]
    with ReactConnector[RootModel] {
  protected def initialModel: RootModel = RootModel()

  protected def actionHandler: HandlerFunction = foldHandlers(
    new TrainingParametersFormHandler(zoomTo(_.trainingParametersForm))
  )
}
