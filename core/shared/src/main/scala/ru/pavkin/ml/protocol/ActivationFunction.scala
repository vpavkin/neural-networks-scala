package ru.pavkin.ml.protocol

import enumeratum.EnumEntry.CapitalWords
import enumeratum._
import scala.collection.immutable.IndexedSeq

sealed trait ActivationFunction extends EnumEntry with CapitalWords

object ActivationFunction extends Enum[ActivationFunction] {

  case object Sigmoid extends ActivationFunction

  def values: IndexedSeq[ActivationFunction] = findValues
}
