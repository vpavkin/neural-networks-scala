package ru.pavkin.ml.app.frontend

import japgolly.scalajs.react.extra.router.{BaseUrl, Router, RouterConfigDsl}
import japgolly.scalajs.react.vdom.all._
import org.scalajs.dom
import ru.pavkin.ml.app.frontend.components.Layout
import ru.pavkin.ml.app.frontend.pages.MainPage
import ru.pavkin.ml.app.frontend.routing.Route

import scala.scalajs.js.JSApp

object NeuralNetworksApp extends JSApp {

  val routerConfig = RouterConfigDsl[Route].buildConfig { dsl =>
    import dsl._

    def renderMainPage = renderR(ctl => MainPage.component(MainPage.Props(ctl)))

    (trimSlashes |
      staticRoute(root, Route.MainPage) ~> renderMainPage |
      dynamicRouteCT[Route.NotFound]("#not-found" / remainingPathOrBlank.xmap(Route.NotFound)(_.path)) ~>
        dynRender(r => h1(s"Page not found: ${r.path}")))
      .notFound(path => Route.NotFound(path.value))
      .renderWith(Layout.render)
  }

  def main(): Unit = {

    // render the router
    val (router, ctl) = Router.componentAndCtl(BaseUrl.fromWindowOrigin_/, routerConfig)

    router() renderIntoDOM dom.document.getElementById("root")
  }
}
