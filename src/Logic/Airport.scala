package Logic

import scala.collection.mutable.Buffer
import scala.util.Random

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
  val queuesOnGround: Vector[LandQueue],
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
  def planes:Buffer[Airplane] = planeBuffer
  
  private def updatePlanes: Unit = {
    if (tick % 50 == 0) {
      time += 1
      createPlane
      planeBuffer.foreach(plane => if(plane.isInAir) plane.currentFlight.get.update)
    }

    planeBuffer.foreach(_.checkAirplane)
    planeBuffer = planeBuffer.filter(_.currentFlight.isDefined)

  }

  private def checkRunways: Unit = {
    for (way <- runways) {
      if (crossingRunways.get(way).isDefined) {
        val crossers = crossingRunways.get(way).get
        if (way.currentPlane.isDefined && crossers.exists(_.currentPlane.isDefined)) {
          endingReason = Some( "Planes assigned on crossing runways. Fatal crash happened...")
          way.currentPlane.get.crash()
          return crossers.filter(_.currentPlane.isDefined).foreach(_.currentPlane.get.crash())
        }
      }
      if(way.currentPlane.isDefined) {
        if (way.currentPlane.get.minRunwaylength > way.runwayLength) {
          endingReason = Some( "Runway was too short for the plane. Crash occured...")
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
    else if (queuesOnGround.exists(_.idN == number)) queuesOnGround.map(queue => queue.idN -> queue).toMap.get(number).get
    else ??? //TODO Lisää THROW ERROR!
  }

  def getRunwayNo(runwayNo: Int): Runway = {
    runways.map(runway => runway.number -> runway).toMap.get(runwayNo).get //TODO Lisää THROW ERROR!
  }
  def getPlanesAtGates: Vector[Airplane] = gates.filter(_.currentPlane.isDefined).map(_.currentPlane.get)

  def getPlanesOnRunways: Vector[Airplane] = runways.filter(_.currentPlane.isDefined).map(_.currentPlane.get)
  
  def getPlanesInQueues: Vector[Airplane] = queuesInAir.map(queue => queue.planes).flatten

  def getFreeGates: Vector[Gate] = gates.filterNot(_.currentPlane.isDefined)
  
  def getFreeInAirQueues: Vector[InAirQueue] = queuesInAir.filterNot(queue => queue.c <= queue.planes.length)

  def getGateNo(number: Int): Gate = gates.map(gate => gate.number -> gate).toMap.get(number).get //TODO Lisää THROW ERROR!

  def getMaxRWLength: Int = runways.map(_.runwayLength).max
  
  def getTime: String = {
    val hours: Int = 0 + (time / 60)
    val minutes: Int = 0 + time - (hours * 60)
    
    hours + "h " + minutes + "min"
  }
  
  def addNotification(text: String): Unit ={
    notifications.+=:(text)
    notifications = notifications.take(15)
  }
  
  def getNotifications: Buffer[String] = notifications
  

}