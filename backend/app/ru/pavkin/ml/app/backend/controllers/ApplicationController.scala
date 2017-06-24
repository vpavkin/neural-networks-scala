package ru.pavkin.ml.app.backend.controllers

import controllers.AssetsFinder
import play.api.Environment
import play.api.mvc._

class ApplicationController(
  components: ControllerComponents,
  assetsFinder:AssetsFinder)(
  implicit environment: Environment) extends AbstractController(components) {

  def index = Action(Ok(views.html.index(assetsFinder)))
}
