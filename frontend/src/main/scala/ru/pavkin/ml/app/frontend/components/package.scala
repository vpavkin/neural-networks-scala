package ru.pavkin.ml.app.frontend

import japgolly.scalajs.react.{Callback, ReactEventFromInput}

package object components {

  def onTextChange(cb: String => Callback)(e: ReactEventFromInput): Callback =
    e.extract(_.target.value)(text => cb(text))
}
