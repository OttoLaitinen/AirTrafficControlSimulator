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
  val planes: Buffer[Airplane] = Buffer[Airplane]()
  val random = new Random()
  val descendTime = 30

  /*Functions*/
  private def updatePlanes: Unit = {
    if (tick % 50 == 0) {
      time += 1
      createPlane
      planes.foreach(_.currentFlight.get.update)
    }
    
    planes.foreach(_.checkAirplane)

  }

  private def checkRunways: Unit = ???

  def ascendPlane(runway: Runway, plane: Airplane): Unit = ???

  def descendPlane(runway: Runway, plane: Airplane): Unit = {
    plane.descendRunway = Some(runway)
    
  }

  private def createPlane: Unit = {
    if (random.nextFloat() < rushFactor) {
      planes.+=(creator.createAirplane(this))
    }
  }

  def onTick(): Unit = {
    tick += 1
    updatePlanes
    //checkRunways

  }

  def ascendPlanesInQueues(runway: Runway): Unit = ???

  def sendToGate(gate: Gate, plane: Airplane): Unit = ???

  def sendToQueue(queue: Queue, plane: Airplane): Unit = ???

  def getRunwayNo(runwayNo: Int): Runway = {
    runways.map(runway => runway.number -> runway).toMap.get(runwayNo).get //TODO Lisää THROW ERROR!
  }

  def getQueueNo(number: Int): Queue = ???

  def getGateNo(number: Int): Gate = ???

  def getMaxRWLength: Int = {
    runways.map(_.length).max
  }

  /*Miten teen tuon onTick metodin timerin?? Pitää varmaankin myös kehittää testiluokka/vast
   *Jolla ohjelmaa pystyy ajamaan ilman käyttöliittymää */

}