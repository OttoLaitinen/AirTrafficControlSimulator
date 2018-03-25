package Logic

class Airplane(
  airport: Airport,
  val airline: String,
  val fuelCapacity: Int,
  val fuelConsumption: Double,
  var altitude: Int,
  var totalCargoWeight: Int,
  val minRunwaylength: Int,
  var passengers: Int,
  var currentFlight: Option[Flight],
  var nextFlight: Option[Flight]) {
  
  /*Additional values and variables*/
  var fuel = fuelCapacity //mahdollisesti minus joku luku mut meh... //liters
  
  var timeToDestination = 0
  
  
  /*Functions*/
  def changeAltitude(newAltitude: Int): Unit = altitude = newAltitude
  
  def ascend(runwayNo: Int): Unit = airport.ascendPlane(airport.getRunwayNo(runwayNo), this)
  
  def descend(runwayNo: Int): Unit = airport.descendPlane(airport.getRunwayNo(runwayNo), this)
  
  def sendToQueue(number: Int): Unit = airport.sendToQueue(airport.getQueueNo(number), this)
  
  def sendToGate(number: Int): Unit = airport.sendToGate(airport.getGateNo(number), this)
  
  def setFlightTime(time: Int) = {
    timeToDestination = time
    if(currentFlight.isDefined) {
      currentFlight.get.completion = BigDecimal(1 -(timeToDestination.toDouble / currentFlight.get.flightTime))
                                     .setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    }
  }
  
  override def toString = {
    "Airline: " + airline + " || Altitude: " + altitude + " || Passengers: " + passengers + " || Current Flight Completion: " + 
     currentFlight.get.completion + " || Fuel Capacity: " + fuelCapacity + " || fuelConsumption: " + fuelConsumption +  "."
  }
  
  
}


