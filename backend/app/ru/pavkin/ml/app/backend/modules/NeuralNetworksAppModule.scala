package ru.pavkin.ml.app.backend.modules

import play.api.ApplicationLoader.Context
import play.api._
import _root_.controllers.AssetsComponents
import play.filters.HttpFiltersComponents
import router.Routes
import ru.pavkin.ml.app.backend.controllers.ApplicationController

class NeuralNetworksAppModule(context: Context)
  extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents
    with AssetsComponents {

  def overrideLogbackConfiguration(context: Context): Unit =
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(Map.empty, context.environment.resource("logback.xml"))
    }

  val applicationController = new ApplicationController(controllerComponents, assetsFinder)(environment)

  val router = new Routes(httpErrorHandler, applicationController, assets)
}
