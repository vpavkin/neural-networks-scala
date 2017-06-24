package ru.pavkin.ml.app.frontend.routing

sealed trait Route

object Route {

  case object MainPage extends Route
  case class NotFound(path: String) extends Route

}
