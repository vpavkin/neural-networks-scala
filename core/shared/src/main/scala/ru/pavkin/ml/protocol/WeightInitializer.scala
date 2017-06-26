package ru.pavkin.ml.protocol

import enumeratum.EnumEntry.CapitalWords
import enumeratum._
import scala.collection.immutable.IndexedSeq

sealed trait WeightInitializer extends EnumEntry with CapitalWords

object WeightInitializer extends Enum[WeightInitializer] {
  case object Default extends WeightInitializer
  case object Large extends WeightInitializer
  def values: IndexedSeq[WeightInitializer] = findValues
}
