package Logic

import scala.collection.mutable.Buffer

abstract class Queue(
  val capacity: Int,
  val idNumber: Int,
  val planes: Buffer[Airplane] = Buffer[Airplane]()) {

  def addPlane(airplane: Airplane): Unit = {
    if (planes.length < capacity) {      
      planes.+=(airplane)
    }
    else {
      println("WTF DYYD " + planes.length) /*TODO Heittää virheen. Virheen ei pitäisi olla mahdollinen, koska täyteen jonoon ei tulisi voida edes yrittää lisätä ketään.*/
    }
  }

  def removePlane(airplane: Airplane): Unit = planes.-=(airplane)

  def getPlane(airplane: Airplane): Airplane = ???
}