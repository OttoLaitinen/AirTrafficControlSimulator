import scala.util.parsing.json._

object Testing extends App {
  
  val configFile = scala.io.Source.fromFile("Config/test.json")
  val configString = try configFile.getLines mkString "\n" finally configFile.close()

  val parsed = JSON.parseFull(configString)
  
 /*parsedMap is mapped so that all the keys in this map are lower case. 
  * This is done to make it possible to ignore the case when getting values from the map*/
  val parsedMap = parsed.get.asInstanceOf[Map[String, Any]]
                         .map(original => (original._1.toLowerCase(), original._2))
      
  /*Names and basic values*/
  val title = parsedMap.get("title").get.asInstanceOf[String]
  val airportName = parsedMap.get("airportname").get.asInstanceOf[String]
  val country = parsedMap.get("country").get.asInstanceOf[String]
  val city = parsedMap.get("city").get.asInstanceOf[String]
  val description = parsedMap.get("description").get.asInstanceOf[String]
  val rushFactor = parsedMap.get("rushfactor").get.asInstanceOf[Double]
  
  /*Every list containing objects has to be mapped separately because 
   *the first mapping done for ignoring the case doesn't affect these map keys. */

  val runWays = parsedMap.get("runways")
                .get.asInstanceOf[List[Map[String, Any]]]
                .map(_.map(original => (original._1.toLowerCase(), original._2)))

  val gates = parsedMap.get("gates")
              .get.asInstanceOf[Double].toInt

  val landQueues = parsedMap.get("landqueues")
                   .get.asInstanceOf[List[Map[String, Double]]]
                   .map(_.map(original => (original._1.toLowerCase(), original._2.toInt)))

  val inAirQueues = parsedMap.get("inairqueues")
                    .get.asInstanceOf[List[Map[String, Double]]]
                    .map(_.map(original => (original._1.toLowerCase(), original._2)))
                    
                    
                    

  println("THIS IS A TEST PRINT FOR THE JSON-PARSER" + "\n")
  
  println("Title: " + title + "\n")
  println("Airport: " + airportName + " // City: " + city + " // Country: " + country + "\n")
  println("Description: " + description + " // " + "Rush level here is " + rushFactor + "\n")
  
  println("Runways:")
  runWays.foreach {
    runway =>
      println("#" + runway.get("number").get.asInstanceOf[Double].toInt +
        " // Length: " + runway.get("length").get.asInstanceOf[Double].toInt +
        " // Crossing runways " + runway.get("crosses").get.asInstanceOf[List[Double]].map(_.toInt))
  }
  
  println("\n" + "Number of gates: " + gates + "\n")
  
  println("Queues on land:")
  landQueues.foreach(queue =>println("A queue for runway number: " + queue.get("runway").get + ". With capacity of " + queue.get("capacity").get))
  println()
  println("Queues in air:")
  inAirQueues.foreach(queue =>println("A queue in altitude of " + queue.get("altitude").get))

}