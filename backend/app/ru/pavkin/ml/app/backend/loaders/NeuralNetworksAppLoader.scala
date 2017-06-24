package ru.pavkin.ml.app.backend.loaders

import play.api.ApplicationLoader.Context
import play.api._
import ru.pavkin.ml.app.backend.modules.NeuralNetworksAppModule

class NeuralNetworksAppLoader extends ApplicationLoader {

  def module(context: Context): NeuralNetworksAppModule = new NeuralNetworksAppModule(context)

  def startApplication(module: NeuralNetworksAppModule): Application = {
    module.application
  }

  def load(context: Context): Application = {
    val theModule = module(context)
    theModule.overrideLogbackConfiguration(context)
    startApplication(theModule)
  }

}
