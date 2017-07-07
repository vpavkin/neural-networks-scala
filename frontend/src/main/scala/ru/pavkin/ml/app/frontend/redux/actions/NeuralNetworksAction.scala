package ru.pavkin.ml.app.frontend.redux.actions

import ru.pavkin.ml.app.frontend.redux.model.TrainingParameters

sealed trait NeuralNetworksAction extends diode.Action

case class UpdateTrainingParametersForm(newState: TrainingParameters) extends NeuralNetworksAction
