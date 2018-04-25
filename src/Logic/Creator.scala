package Logic

import scala.util.parsing.json._
import scala.collection.mutable.Buffer
import scala.util.Random
import java.io.FileReader
import java.io.FileNotFoundException
import java.io.BufferedReader
import java.io.IOException

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
    landQueuesData.map {
      data =>
        new LandQueue(getRunway(data.get("runwayno").get), data.get("capacity").get, data.get("runwayno").get)
    }.toVector
  }

  val queuesInAir: Vector[InAirQueue] = {
    inAirQueuesData.map {
      data =>
        new InAirQueue(data.get("altitude").get, 2, data.get("altitude").get.toInt)
    }.toVector
  }

  /////////////////////////////////*PARSER ENDS HERE*//////////////////////////////////////////

  val nextFlightPercentage: Int = 30 //How many planes have a "nextFlight"
  val airlines = reader("Config/Airlines.txt")
  val cities = reader("Config/Cities.txt")

  def createAirport: Airport = new Airport(this, gameTitle, airportName, country, city, description, runways,
    crossingRunways, gates, queuesOnGround, queuesInAir, rushFactor)

  def createAirplane(airport: Airport): Airplane = {
    val rndm = new Random()

    /*Necessary values for creating the airplane*/
    val airline: String = airlines(rndm.nextInt(airlines.length - 1))
    val fuelCapacity: Int = 20000 + rndm.nextInt(5) * 1000 //Litres
    val fuelConsumption: Int = 70 + rndm.nextInt(40) //Liters / minute
    val altitude: Int = rndm.nextInt(7) * 1000 + 8000
    val passengers: Int = 25 + rndm.nextInt(300)
    val totalCargoWeigth: Int = passengers * 80 //Kilograms
    val minRunwayLength: Int = airport.getMaxRWLength

    /*Plane is created without any flights assigned*/
    val newPlane = new Airplane(airport, airline, fuelCapacity, fuelConsumption, altitude,
      totalCargoWeigth, minRunwayLength, passengers, None, None)

    /*Flights are generated*/
    val currentFlight: Option[Flight] = Some(createFlight(newPlane, true,airport))

    val nextFlight: Option[Flight] = {
      if (rndm.nextInt(100) <= nextFlightPercentage) Some(createFlight(newPlane, false, airport))
      else None
    }

    /*The new airplane is updated with the new flights*/
    newPlane.currentFlight = currentFlight
    newPlane.nextFlight = nextFlight

    /*Return*/
    newPlane
  }

  private def createFlight(airplane: Airplane, arriving: Boolean, airport: Airport): Flight = {
    val rndm = new Random()

    /*Necessary values*/
    val destination: String = {
      if (arriving) city
      else {
        var city2 = cities(rndm.nextInt(cities.length -1))
        while (city2 == city) {
          city2 = cities(rndm.nextInt(cities.length - 1))
        }
        city2
      }
    }
    val departure: String = {
      if (!arriving) city
      else {
        var city2 = cities(rndm.nextInt(cities.length - 1))
        while (city2 == city) {
          city2 = cities(rndm.nextInt(cities.length - 1))
        }
        city2
      }
    }
    val shortForm: String = {
      var start = destination.take(3).toUpperCase()
      var end = rndm.nextInt(999)
      
      while (airport.planes.exists(_.currentFlight.get.shortForm == (start + end))) {
       start = destination.take(3).toUpperCase()
      end = rndm.nextInt(999)
      }
      start + end
      
    }
    val flightTime: Int = 60 + rndm.nextInt(12) * 10 //minutes

    new Flight(destination, departure, shortForm, flightTime, airplane)

  }

  private def reader(filePath: String): Array[String] = {
    val fileReader = try {
      new FileReader(filePath);
    } catch {
      case e: FileNotFoundException =>
        println("File not found")
        return Array[String]() //TODO tulee ottaa huomioon myöhemmin
    }

    val lineReader = new BufferedReader(fileReader)

    try {
      var readLine = lineReader.readLine()
      var returned: Buffer[String] = Buffer[String]()

      while (readLine != null) {
        returned += readLine
        readLine = lineReader.readLine()
      }
      returned.toArray
    } catch {
      case e: IOException =>
        println("There was an error when reading a file.")
        return Array[String]() //TODO tulee ottaa huomioon myöhemmin
    }
  }

}