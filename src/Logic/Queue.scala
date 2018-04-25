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
      println("Too many planes were assigned on the same altitude. Crash occured.")
      planes(0).crash()
      airplane.crash()
       
    }
  }

  def removePlane(airplane: Airplane): Unit = planes.-=(airplane)

  def getPlane(airplane: Airplane): Airplane = ???
}