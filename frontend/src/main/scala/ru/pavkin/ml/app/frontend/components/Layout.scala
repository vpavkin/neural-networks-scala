package ru.pavkin.ml.app.frontend.components

import japgolly.scalajs.react.extra.router.{Resolution, RouterCtl}
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.all._
import org.scalajs.dom.html.{Div, Element}
import ru.pavkin.ml.app.frontend.routing.Route

object Layout {

  def render(router: RouterCtl[Route], resolution: Resolution[Route]): TagOf[Div] = div(
    renderNavBar(router),
    div(cls := "container-fluid",
      resolution.render()
    )
  )

  def renderNavBar(router: RouterCtl[Route]): TagOf[Element] = nav(
    cls := "navbar navbar-toggleable-md navbar-light bg-faded",
    a(cls := "navbar-brand", href := "/", "Neural networks and Deep learning")
  )
}
