package ru.pavkin.ml.app.frontend.pages

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.all._
import ru.pavkin.ml.app.frontend.routing.Route

object MainPage {

  case class Props(router: RouterCtl[Route])

  val component = ScalaComponent.builder[Props]("Main Page")
    .render_P(p => div("Main page"))
    .build

}
