package ru.pavkin.ml.app.frontend.components

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._
import ru.pavkin.ml.app.frontend.redux.actions.UpdateTrainingParametersForm
import ru.pavkin.ml.app.frontend.redux.model.TrainingParameters

object TrainingParametersForm {

  case class Props(state: ModelProxy[TrainingParameters]) {
    val params: TrainingParameters = state()

    def dispatchStateChange(change: TrainingParameters => TrainingParameters): Callback =
      state.dispatchCB(UpdateTrainingParametersForm(change(params)))
  }

  val component = ScalaComponent.builder[Props]("TrainingParametersForm")
    .render_P(p => form(
      div(cls := "form-group row",
        label(cls := "col-sm-2 col-form-label", "Hidden Layer Sizes"),
        div(cls := "col-sm-10",
          input.email(cls := "form-control", placeholder := "Hidden Layer Sizes (e.g. 30,100",
            value := p.params.hiddenLayersSizes,
            onChange ==> onTextChange(newSizes => p.dispatchStateChange(_.copy(hiddenLayersSizes = newSizes)))
          )
        )
      )
    ))
    .build

}
