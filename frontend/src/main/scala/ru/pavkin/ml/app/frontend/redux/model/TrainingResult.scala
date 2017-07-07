package ru.pavkin.ml.app.frontend.redux.model

import java.time.Duration

import ru.pavkin.ml.core.MonitoringResult

case class TrainingResult(
  duration: Duration,
  epochs: Vector[MonitoringResult]
)
