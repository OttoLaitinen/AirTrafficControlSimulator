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

  var fuel: Int = fuelCapacity //mahdollisesti minus joku luku mut meh... //liters

  var wantedAltitude = altitude
  var isInAir = true
  var descendRunway: Option[Runway] = None //TODO reset-nappula tälle
  var goToInAirQueue: Option[InAirQueue] = None
  private var hasReservedRunway: Boolean = false

  /*Functions and methods*/
  def changeAltitude(newAltitude: Int): Unit = wantedAltitude = newAltitude

  def moveAirplane: Unit = if (wantedAltitude - altitude < 0) altitude -= 10 else if (wantedAltitude - altitude > 0) altitude += 10

  def checkAirplane: Unit = {
    moveAirplane
    fuelOperations
    if (descendRunway.isDefined) descendingOperations
    if (goToInAirQueue.isDefined) queueOperations
  }

  def ascend(runwayNo: Int): Unit = airport.ascendPlane(airport.getRunwayNo(runwayNo), this)

  def descend(runwayNo: Int): Unit = {
    goToInAirQueue = None
    descendRunway = Some(airport.getRunwayNo(runwayNo))

  }
  
  def crash(): Unit = {
    println("Plane carrying flight " + currentFlight.get.shortForm + " has crashed")
    airport.gameIsOn = false
  }

  def sendToQueue(number: Int): Unit = {
    descendRunway = None
    val queue = airport.getQueueNo(number)
    if (queue.isInstanceOf[InAirQueue]) goToInAirQueue = Some(queue.asInstanceOf[InAirQueue])
    else ??? /*TODO Mitä tapahtuu kun kyseessä maajono*/
    
    //airport.sendToQueue(airport.getQueueNo(number), this)
  }

  def sendToGate(number: Int): Unit = {
    if (this.descendRunway.isDefined) {
      this.descendRunway.get.unreserve()
      this.descendRunway = None
    }
    airport.getGateNo(number).reserve(this)
  }

  def timeToDestination: Int = if (currentFlight.isDefined) math.max(currentFlight.get.timeToDestination, 0) else 0

  def descendingOperations: Unit = {
    if (this.timeToDestination < 30 && altitude > 1500) {
      changeAltitude(1500)
    } else if (this.timeToDestination < 10 && this.timeToDestination > 0 && !hasReservedRunway) {
      hasReservedRunway = true
      descendRunway.get.reserve(this)
      changeAltitude(0)
    } else if (this.timeToDestination == 0 && altitude == 0) {
      isInAir = false
    }
  }
  
  def queueOperations: Unit = {
    if (this.timeToDestination < 30) {
      /*TODO Vaihdetaan korkeus sopivaksi ja lisätään kone jonoon.*/ 
    }
  }

  def fuelOperations: Unit = {
    if (airport.tick % 50 == 0 && isInAir) {
      fuel = fuel - fuelConsumption.toInt
    }
  }

  def isChangingAlt: Boolean = wantedAltitude != altitude

  override def toString = {
    var basic = "Flight: " + currentFlight.get.shortForm + " || Airline: " + airline + "\n" + "\n" +
      "Altitude: " + altitude + "m || Passengers: " + passengers + " || Minimum Runway Length: " + minRunwaylength + "m" + "\n" +
      "Fuel: " + fuel + " litres || Consumption: " + fuelConsumption + "l/min" + " || ETA: " + timeToDestination + "min" + "\n"

    if (descendRunway.isDefined) basic += "LANDING RUNWAY: #" + descendRunway.get.number + " || Landing starts in " + math.max(this.timeToDestination - 10, 0) + "minutes"

    basic
  }

}


