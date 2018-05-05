package Logic

import scala.collection.mutable.Buffer

/**Models a queue that is in the air or on land.
 * The queue has a number that it can be identified with but also knows its capacity and 
 * the planes that currently are in the queue.*/
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