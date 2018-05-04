package Logic

import java.util.concurrent.Executor


/**Models a runway. Knows its length and if a plane is using it or not.
 * Every runway is assigned a number.*/
class Runway(
  val runwayLength: Int,
  val number: Int,
  var usable: Boolean = true,
  var condition: Double = 100.0,
  var currentPlane: Option[Airplane] = None) {

  def reserve(airplane: Airplane): Unit = {
    if (currentPlane.isEmpty) {
      println("Reserving runway number " + number)
      currentPlane = Some(airplane)
    } else {
      throw new Error("Two planes on the same runway")
    }
  }

  def unreserve(): Unit = {
    println("Unreserving runway number " + number)
    currentPlane = None
    println(currentPlane.isEmpty)
  }


  def clean: Unit = ???

  def isInUse: Boolean = currentPlane.isDefined

}