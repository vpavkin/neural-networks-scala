package ru.pavkin.ml.protocol

import enumeratum.EnumEntry.CapitalWords
import enumeratum._
import scala.collection.immutable.IndexedSeq

sealed trait Regularization extends EnumEntry with CapitalWords

object Regularization extends Enum[Regularization] {

  case object NoRegularization extends Regularization
  case object L2Regularization extends Regularization

  def values: IndexedSeq[Regularization] = findValues
}
