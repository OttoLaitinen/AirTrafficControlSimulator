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
  val planes: Buffer[Airplane] = Buffer[Airplane]()
  val random = new Random()

  /*Functions*/
  private def checkPlanes: Unit = ???

  private def checkRunways: Unit = ???

  def ascendPlane(runway: Runway, plane: Airplane): Unit = ???

  def descendPlane(runway: Runway, plane: Airplane): Unit = ???

  private def createPlane: Unit = {
    if(random.nextFloat() < rushFactor) {
      planes.+=(creator.createAirplane(this))
    }
  }

  def onTick(): Unit = {
    createPlane
    //checkPlanes
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
  
  def getMaxRWLength: Int = 1000 //TODO

  /*Miten teen tuon onTick metodin timerin?? Pitää varmaankin myös kehittää testiluokka/vast
   *Jolla ohjelmaa pystyy ajamaan ilman käyttöliittymää */

}