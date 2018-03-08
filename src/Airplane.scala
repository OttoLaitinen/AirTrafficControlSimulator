

class Airplane(
  airport: Airport,
  airline: String,
  fuelCapacity: Int,
  fuelConsumption: Double,
  var altitude: Int,
  var timeToDestination: Int,
  totalCargoWeight: Int,
  minRunwaylength: Int,
  passengers: Int,
  var currentFlight: Option[Flight],
  var nextFlight: Option[Flight]) {
  
  /*Additional values and variables*/
  var fuel = fuelCapacity //mahdollisesti minus joku luku mut meh...
  
  
  /*Functions*/
  def changeAltitude(newAltitude: Int): Unit = altitude = newAltitude
  
  def ascend(runwayNo: Int): Unit = airport.ascendPlane(airport.getRunwayNo(runwayNo), this)
  
  def descend(runwayNo: Int): Unit = airport.descendPlane(airport.getRunwayNo(runwayNo), this)
  
  def sendToQueue(number: Int): Unit = airport.sendToQueue(airport.getQueueNo(number), this)
  
  def sendToGate(number: Int): Unit = airport.sendToGate(airport.getGateNo(number), this)
  
  
}


