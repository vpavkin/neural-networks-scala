package ru.pavkin.ml.app.frontend.components

import japgolly.scalajs.react.extra.router.{Resolution, RouterCtl}
import japgolly.scalajs.react.vdom.all._
import ru.pavkin.ml.app.frontend.routing.Route

object Layout {

  def render(router: RouterCtl[Route], resolution: Resolution[Route]) = div(resolution.render())
}
