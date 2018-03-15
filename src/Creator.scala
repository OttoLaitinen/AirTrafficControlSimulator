import scala.util.parsing.json._
import scala.collection.mutable.Buffer

class Creator(fileName: String) {

  /////////////////////////////////*PARSER STARTS HERE*//////////////////////////////////////////

  val configFile = scala.io.Source.fromFile(fileName)
  val configString = try configFile.getLines mkString "\n" finally configFile.close()

  val parsed = JSON.parseFull(configString)

  /*parsedMap is mapped so that all the keys in this map are lower case.
  * This is done to make it possible to ignore the case when getting values from the map*/
  val parsedMap = parsed.get.asInstanceOf[Map[String, Any]]
    .map(original => (original._1.toLowerCase(), original._2))

  /*Names and basic values*/
  val gameTitle = parsedMap.get("title").get.asInstanceOf[String]
  val airportName = parsedMap.get("airportname").get.asInstanceOf[String]
  val country = parsedMap.get("country").get.asInstanceOf[String]
  val city = parsedMap.get("city").get.asInstanceOf[String]
  val description = parsedMap.get("description").get.asInstanceOf[String]
  val rushFactor = parsedMap.get("rushfactor").get.asInstanceOf[Double]

  /*Every list containing objects has to be mapped separately because
   *the first mapping done for ignoring the case doesn't affect these map keys. */

  val runwaysData = parsedMap.get("runways")
    .get.asInstanceOf[List[Map[String, Any]]]
    .map(_.map(original => (original._1.toLowerCase(), original._2)))

  val gatesAmount = parsedMap.get("gates")
    .get.asInstanceOf[Double].toInt

  val landQueuesData = parsedMap.get("landqueues")
    .get.asInstanceOf[List[Map[String, Double]]]
    .map(_.map(original => (original._1.toLowerCase(), original._2.toInt)))

  val inAirQueuesData = parsedMap.get("inairqueues")
    .get.asInstanceOf[List[Map[String, Double]]]
    .map(_.map(original => (original._1.toLowerCase(), original._2)))

  ////////////*More advanced values to be given to createAirport*//////////////

  val runways: Vector[Runway] = runwaysData.map {
    parsedMap =>
      new Runway(
        parsedMap.get("length").get.asInstanceOf[Double].toInt,
        parsedMap.get("number").get.asInstanceOf[Double].toInt)
  }.toVector
  
  def getRunway(number: Int): Runway = {
    val runwayMap = runways.map(runway => runway.number -> runway).toMap //Helps with finding the Runway with its number only
    runwayMap.get(number).get //TODO Needs to send an error if no corresponding runway is found
  }
  

  val crossingRunways: Map[Runway, Vector[Runway]] = runwaysData.map { //(Runway, Vector[Runway]).toMap
    parsedMap =>
      (
        getRunway(parsedMap.get("number").get.asInstanceOf[Double].toInt),
        parsedMap.get("crosses").get.asInstanceOf[List[Double]].map(_.toInt).map(getRunway(_)).toVector)
  }.toMap
  
  val gates: Vector[Gate] = {
    var gateBuffer = Buffer[Gate]()
    for (i <- 1 to gatesAmount) {
      gateBuffer += new Gate(i)
    }
    gateBuffer.toVector
  }
  
  val queuesOnGround: Vector[LandQueue] = { 
    landQueuesData.map{
      data => 
        new LandQueue(getRunway(data.get("number").get), data.get("capacity").get, data.get("number").get)   
      }.toVector }
      
  val queuesInAir: Vector[InAirQueue] = { 
    inAirQueuesData.map{
      data => 
        new InAirQueue(data.get("altitude").get, 2, data.get("number").get.toInt)
      }.toVector
  }
 

  /////////////////////////////////*PARSER ENDS HERE*//////////////////////////////////////////

  def createAirport: Airport = new Airport(gameTitle, airportName, country, city, description, runways, 
                                           crossingRunways, gates, queuesOnGround, queuesInAir, rushFactor)

  def createAirplane: Airplane = ???

}