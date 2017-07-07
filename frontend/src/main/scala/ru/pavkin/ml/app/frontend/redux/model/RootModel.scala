package ru.pavkin.ml.app.frontend.redux.model

import diode.data.Pot

case class RootModel(
  lastTrainingResult: Pot[TrainingResult] = Pot.empty,
  trainingParametersForm: TrainingParameters = TrainingParameters.Default
)
