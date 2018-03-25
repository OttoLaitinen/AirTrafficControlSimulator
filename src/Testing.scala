import scala.util.parsing.json._

object Testing extends App {

  
  val configFile = scala.io.Source.fromFile("Config/test.json")
  val configString = try configFile.getLines mkString "\n" finally configFile.close()

  val creator = new Creator("Config/test.json")
  
  val airport = creator.createAirport
  
                    
                    
                    

  println("/////// THIS IS A TEST PRINT FOR THE JSON-PARSER /////////" + "\n")
  
  println("Title: " + airport.title + "\n")
  println("Airport: " + airport.airportName + " // City: " + airport.city + " // Country: " + airport.country + "\n")
  println("Description: " + airport.description + " // " + "Rush level here is " + airport.rushFactor + "\n")
  
  println("Runways:")
  airport.runways.foreach {
    runway =>
      println("#" + runway.number +
        " // Length: " + runway.length +
        " // Crossing runways: " + airport.crossingRunways.get(runway).get.map(_.number))
  }
//  
  println("\n" + "Number of gates: " + airport.gates.length + "\n")
//  
  println("Queues on land:")
  airport.queuesOnGround.foreach(queue =>println("A queue for runway number: " + queue.runway.number + ". With capacity of " + queue.capacity))
  println()
  println("Queues in air:")
  airport.queuesInAir.foreach(queue =>println("A queue in altitude of " + queue.altitude))
  
  println()

  println("////////////// THE TEST ENDS HERE ////////////////")
  
  println()
  
  println("////////////// TEST FOR createAirplane STARTS /////////")
  
    airport.onTick()
    airport.onTick()
    airport.onTick()
    airport.onTick()
    airport.onTick()
    airport.onTick()
    airport.onTick()
    airport.onTick()
    airport.onTick()
    airport.onTick()
    airport.onTick()
        
    airport.planes.foreach(println(_))
  
   println("////////////// TEST FOR createAirplane ENDS /////////")

}