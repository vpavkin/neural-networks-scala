package ru.pavkin.ml.app.frontend.redux

import diode.react.ReactConnectProxy
import ru.pavkin.ml.app.frontend.redux.circuits.RootCircuit
import ru.pavkin.ml.app.frontend.redux.model.TrainingParameters

object connectors {
  val trainingParametersForm: ReactConnectProxy[TrainingParameters] =
    RootCircuit.connect(_.trainingParametersForm)
}
