package Logic

import scala.collection.mutable.Buffer

abstract class Queue(
  val capacity: Int,
  val idNumber: Int,
  val planes: Buffer[Airplane] = Buffer[Airplane]()) {

  def addPlane(airplane: Airplane): Unit = planes.+=(airplane)

  def removePlane(airplane: Airplane): Unit = planes.-=(airplane)

  def getPlane(airplane: Airplane): Airplane = ???
}