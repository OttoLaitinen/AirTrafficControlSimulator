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

  var wantedAltitude = altitude
  var isInAir = true
  var descendRunway: Option[Runway] = None //TODO reset-nappula tälle

  /*Functions*/
  def changeAltitude(newAltitude: Int): Unit = wantedAltitude = newAltitude

  def moveAirplane: Unit = if (wantedAltitude - altitude < 0) altitude -= 10 else if (wantedAltitude - altitude > 0) altitude += 10

  def checkAirplane: Unit = {
    moveAirplane
    descendingOperations
  }

  def ascend(runwayNo: Int): Unit = airport.ascendPlane(airport.getRunwayNo(runwayNo), this)

  def descend(runwayNo: Int): Unit = airport.descendPlane(airport.getRunwayNo(runwayNo), this)

  def sendToQueue(number: Int): Unit = airport.sendToQueue(airport.getQueueNo(number), this)

  def sendToGate(number: Int): Unit = airport.sendToGate(airport.getGateNo(number), this)

  def timeToDestination: Int = if (currentFlight.isDefined) math.max(currentFlight.get.timeToDestination, 0) else 0

  def descendingOperations: Unit = {
    if (this.timeToDestination < 30 && altitude > 1500 && descendRunway.isDefined) {
      changeAltitude(1500)
    } else if (this.timeToDestination < 10 && descendRunway.isDefined) {
      descendRunway.get.reserve(this)
      changeAltitude(0)
    } else if (this.timeToDestination == 0 && altitude == 0) {
      isInAir = false
    }
  }

  def isChangingAlt: Boolean = wantedAltitude != altitude

  override def toString = {
    "Flight: " + currentFlight.get.shortForm + " || Airline: " + airline +"\n" +
     "Altitude: " + altitude + " || Passengers: " + passengers + " || Time to destination: " + timeToDestination + "\n" + 
     "Fuel Capacity: " + fuelCapacity + " || fuelConsumption: " + fuelConsumption
  }

}


