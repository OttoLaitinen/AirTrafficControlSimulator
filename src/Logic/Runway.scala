package Logic

import java.util.concurrent.Executor



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
//      println("You assigned two planes on the same runway (Runway: " + number + ") and it caused a fatal crash.")
//      currentPlane.get.crash()
//      airplane.crash()
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