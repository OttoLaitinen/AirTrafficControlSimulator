import scala.collection.mutable.Buffer

class Airport(
  title: String,
  airportName: String,
  country: String,
  city: String,
  description: String,
  runways: Vector[Runway],
  crossingRunways: Map[Runway, Vector[Runway]],
  gates: Vector[Gate],
  queuesOnGround: Vector[LandQueue],
  queuesInAir: Vector[InAirQueue],
  rushFactor: Double) {
  
  /*Values and variables*/
  var time: Int = 0
  val planes: Buffer[Airplane] = Buffer[Airplane]()

  /*Functions*/
  private def checkPlanes: Unit = ???

  private def checkRunways: Unit = ???

  def ascendPlane(runway: Runway, plane: Airplane): Unit = ???

  def descendPlane(runway: Runway, plane: Airplane): Unit = ???

  private def createPlane: Unit = ???

  private def onTick(): Unit = ???

  def ascendPlanesInQueues(runway: Runway): Unit = ???

  def sendToGate(gate: Gate, plane: Airplane): Unit = ???
  
  def sendToQueue(queue: Queue, plane: Airplane): Unit = ???
  
  def getRunwayNo(runwayNo: Int): Runway = ???
  
  def getQueueNo(number: Int): Queue = ???
  
  def getGateNo(number: Int): Gate = ???

  /*Miten teen tuon onTick metodin timerin?? Pitää varmaankin myös kehittää testiluokka/vast
   *Jolla ohjelmaa pystyy ajamaan ilman käyttöliittymää */

}