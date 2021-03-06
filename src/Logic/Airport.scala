package Logic

import scala.collection.mutable.Buffer
import scala.util.Random
/**Class Airport models the airport in the simulator and creates and keeps track of all the planes 
 * and other objects in the game. 
 * Class also contains the method onTick() that makes the game's objects act.*/
class Airport(
  creator: Creator,
  val title: String,
  val airportName: String,
  val country: String,
  val city: String,
  val description: String,
  val runways: Vector[Runway],
  val crossingRunways: Map[Runway, Vector[Runway]],
  val gates: Vector[Gate],
  val queuesInAir: Vector[InAirQueue],
  val rushFactor: Double) {

  /*Values and variables*/
  var time: Int = 0
  var tick: Int = 0
  var gameIsOn = true
  private var planeBuffer: Buffer[Airplane] = Buffer[Airplane]()
  private var notifications: Buffer[String] = Buffer[String]()
  val random = new Random()
  val descendTime = 30
  var points = 0
  var endingReason: Option[String] = None

  /*Functions*/
  def planes: Buffer[Airplane] = planeBuffer

  private def updatePlanes: Unit = {
    planeBuffer = planeBuffer.filter(_.currentFlight.isDefined).filter(_.gate.isEmpty)
    if (tick % 50 == 0) {
      time += 1
      createPlane
      planeBuffer.foreach(plane => if (plane.isInAir) plane.currentFlight.get.update)
    }

    planeBuffer.foreach(_.checkAirplane)
    /*planeBuffer holds only planes that have currentFlight defined. Other planes are not relevant for the airport.*/
    planeBuffer = planeBuffer.filter(_.currentFlight.isDefined)

  }

  /**Checks if a runway has a plane that needs a longer runway or if there are planes on crossing runways.*/
  private def checkRunways: Unit = {
    for (way <- runways) {
      if (crossingRunways.get(way).isDefined) {
        val crossers = crossingRunways.get(way).get
        if (way.isInUse && crossers.exists(_.isInUse)) {
          endingReason = Some("Planes assigned on crossing runways. Fatal crash happened...")
          way.currentPlane.get.crash()
          return crossers.filter(_.currentPlane.isDefined).foreach(_.currentPlane.get.crash())
        }
      }
      if (way.currentPlane.isDefined) {
        if (way.currentPlane.get.minRunwaylength > way.runwayLength) {
          endingReason = Some("Runway was too short for the plane. Crash occured...")
          return way.currentPlane.get.crash()
        }
      }
    }
  }

  private def createPlane: Unit = {
    if (random.nextFloat() < rushFactor) {
      addPlane(creator.createAirplane(this))
    }
  }

  def onTick(): Unit = {
    tick += 1
    updatePlanes
    checkRunways

  }
  
  
  def addPlane(airplane: Airplane): Unit = {
    planeBuffer.+=(airplane)
    planeBuffer = planeBuffer.filter(_.currentFlight.isDefined)
  }

  def removePlane(airplane: Airplane): Unit = {
    planeBuffer.-(airplane)
    planeBuffer = planeBuffer.filter(_.currentFlight.isDefined)
  }

  def getQueueNo(number: Int): Queue = {
    if (queuesInAir.exists(_.idN == number)) queuesInAir.map(queue => queue.idN -> queue).toMap.get(number).get
    else throw new Exception("Queue with number " + number + "doesn't exist; there is something wrong with the config files.")
  }

  def getRunwayNo(runwayNo: Int): Runway = {
    val option = runways.map(runway => runway.number -> runway).toMap.get(runwayNo)
    if (option.isDefined) option.get
    else throw new Exception("Runway with number " + runwayNo + "doesn't exist; there is something wrong with the config files.")
  }
  def getPlanesAtGates: Vector[Airplane] = gates.filter(_.currentPlane.isDefined).map(_.currentPlane.get)

  def getPlanesOnRunways: Vector[Airplane] = runways.filter(_.currentPlane.isDefined).map(_.currentPlane.get)

  def getPlanesInQueues: Vector[Airplane] = queuesInAir.map(queue => queue.planes).flatten

  def getFreeGates: Vector[Gate] = gates.filterNot(_.currentPlane.isDefined)

  def getFreeInAirQueues: Vector[InAirQueue] = queuesInAir.filterNot(queue => queue.c <= queue.planes.length)

  def getGateNo(number: Int): Gate = {
    val option = gates.map(gate => gate.number -> gate).toMap.get(number)
    if (option.isDefined) option.get
    else throw new Exception("Gate with number " + number + "doesn't exist; there is something wrong with the config files.")
  }

  def getMaxRWLength: Int = runways.map(_.runwayLength).max

  /**Gives time as a string in hours/minutes format*/
  def getTime: String = {
    val hours: Int = 0 + (time / 60)
    val minutes: Int = 0 + time - (hours * 60)

    hours + "h " + minutes + "min"
  }

  def addNotification(text: String): Unit = {
    notifications.+=:(text)
    notifications = notifications.take(15)
  }

  def getNotifications: Buffer[String] = notifications

}