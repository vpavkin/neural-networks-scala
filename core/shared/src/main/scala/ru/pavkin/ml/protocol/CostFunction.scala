package ru.pavkin.ml.protocol

import enumeratum.EnumEntry.CapitalWords
import enumeratum._
import scala.collection.immutable.IndexedSeq

sealed trait CostFunction extends EnumEntry with CapitalWords

object CostFunction extends Enum[CostFunction] {

  case object Quadratic extends CostFunction
  case object CrossEntropy extends CostFunction

  def values: IndexedSeq[CostFunction] = findValues
}
