package ru.pavkin.ml.protocol

import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import ru.pavkin.ml.core.MonitoringResult

object codecs {

  implicit val sgdConfigEncoder = Encoder[SGDTrainingConfiguration]
  implicit val sgdConfigDecoder = Decoder[SGDTrainingConfiguration]

  implicit val monitoringResultEncoder = Encoder[MonitoringResult]
  implicit val monitoringResultDecoder = Decoder[MonitoringResult]
}
