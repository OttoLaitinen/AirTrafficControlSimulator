package Logic

class Airplane(
  airport: Airport,
  val airline: String,
  val fuelCapacity: Int,
  val fuelConsumption: Double,
  var altitude: Int,
  var totalCargoWeight: Int,
  val minRunwaylength: Int,
  var currentFlight: Option[Flight],
  var nextFlight: Option[Flight]) {

  /*Additional values and variables*/

  var fuel: Int = fuelCapacity //liters

  var wantedAltitude = altitude
  var isInAir = true
  var descendRunway: Option[Runway] = None
  var goToInAirQueue: Option[InAirQueue] = None
  var ascendingRunway: Option[Runway] = None
  var gate: Option[Gate] = None

  var hasReservedRunway: Boolean = false
  private var queuingIn: Option[InAirQueue] = None

  private var ascendingTimer: Int = 0
  private var hasNotified: Boolean = false

  /*Functions and methods*/
  def changeAltitude(newAltitude: Int): Unit = wantedAltitude = newAltitude

  def moveAirplane: Unit = if (wantedAltitude - altitude < 0) altitude -= 10 else if (wantedAltitude - altitude > 0) altitude += 10

  def checkAirplane: Unit = {
    moveAirplane
    fuelOperations
    if (descendRunway.isDefined) descendingOperations
    else if (goToInAirQueue.isDefined) queueOperations
    else if (ascendingRunway.isDefined) ascendingOperations
    else if (!descendRunway.isDefined && this.timeToDestination < 30 && !hasNotified && isInAir) {
      airport.addNotification("Flight " + currentFlight.get.shortForm + " is ready for landing.")
      hasNotified = true
    }
  }

  def ascend(runwayNo: Int): Unit = {
    airport.addPlane(this)
    gate.get.unreserve()
    ascendingRunway = Some(airport.getRunwayNo(runwayNo))
    airport.getRunwayNo(runwayNo).reserve(this)
    airport.addNotification("Flight " + this.currentFlight.get.shortForm + " has reserved runway number " + runwayNo + " for take off.")
    ascendingTimer = 10
  }

  def descend(runwayNo: Int): Unit = {
    goToInAirQueue = None
    queuingIn = None
    descendRunway = Some(airport.getRunwayNo(runwayNo))

    airport.addNotification("Flight " + this.currentFlight.get.shortForm + " will land on runway number " + runwayNo + ".")

  }

  def crash(): Unit = {
    println("Plane carrying flight " + currentFlight.get.shortForm + " has crashed")
    airport.addNotification("Flight " + this.currentFlight.get.shortForm + " has crashed.")
    airport.gameIsOn = false
  }

  def sendToQueue(number: Int): Unit = {
    descendRunway = None
    val queue = airport.getQueueNo(number)
    if (queue.isInstanceOf[InAirQueue]) goToInAirQueue = Some(queue.asInstanceOf[InAirQueue])
    airport.addNotification("Flight " + this.currentFlight.get.shortForm + " is now instructed to queue in " + number + " meters.")
  }

  def sendToGate(number: Int): Unit = {
    if (this.descendRunway.isDefined) {
      airport.points += this.currentFlight.get.timeToDestination * 10 + this.fuel

      hasReservedRunway = false
      this.descendRunway.get.unreserve()
      descendRunway = None

      airport.addNotification("Flight " + this.currentFlight.get.shortForm + " is going to gate number " + number + ".")
      airport.removePlane(this)
      gate = Some(airport.getGateNo(number))
      gate.get.reserve(this)
    }
    if (this.nextFlight.isDefined) {
      val oldFlight = currentFlight.get //TODO tästä pitää tulla jonkunlainen ilmoitus
      currentFlight = nextFlight
      airport.addNotification("Plane that was carrying " + oldFlight.shortForm + " is now ready to take off as flight  " + currentFlight.get.shortForm + ".")
    } else {
      currentFlight = None
    }

  }

  def timeToDestination: Int = if (currentFlight.isDefined) math.max(currentFlight.get.timeToDestination, 0) else 0

  def descendingOperations: Unit = {
    if (this.timeToDestination < 30 && altitude > 1500) {
      changeAltitude(1500)
    } else if (this.timeToDestination < 10 && !hasReservedRunway && isInAir) {
      hasReservedRunway = true
      try {
        descendRunway.get.reserve(this)
      } catch {
        case c: Error => {
          airport.endingReason = Some("You assigned two planes on the same runway (Runway: " + descendRunway.get.number + ") and it caused a fatal crash.")
          this.crash()
          descendRunway.get.currentPlane.get.crash()  
        }
      }
      airport.addNotification("Flight " + this.currentFlight.get.shortForm + " has reserved runway number " + descendRunway.get.number + " for landing.")
      changeAltitude(0)
    } else if (this.timeToDestination <= 0 && altitude == 0 && isInAir) {
      airport.addNotification("Flight " + this.currentFlight.get.shortForm + " has landed succesfully.")
      isInAir = false
    }
  }

  def queueOperations: Unit = {
    if (this.timeToDestination < 30 && !queuingIn.exists(_ == goToInAirQueue.get)) {
      this.changeAltitude(goToInAirQueue.get.altitude.toInt)
      goToInAirQueue.get.addPlane(this)
      queuingIn = Some(goToInAirQueue.get)
      airport.addNotification("Flight " + this.currentFlight.get.shortForm + " is now queuing in " + queuingIn.get.idN + " meters.")
    }
  }

  def ascendingOperations: Unit = {

    if (airport.tick % 50 == 0) ascendingTimer -= 1

    if (ascendingTimer <= 0) {
      airport.addNotification("Flight " + this.currentFlight.get.shortForm + " took off succesfully.")
      ascendingRunway.get.unreserve()
      ascendingRunway = None
      airport.removePlane(this)
    }
  }

  def fuelOperations: Unit = {
    if (airport.tick % 50 == 0 && isInAir) {
      fuel = fuel - fuelConsumption.toInt
    }
  }

  def isChangingAlt: Boolean = wantedAltitude != altitude

  def timeToAscend: Int = ascendingTimer

  //TODO Korjaa myös nouseville koneille
  override def toString = {
    var basic = ""
    if (this.currentFlight.isDefined) {
      basic += "Flight: " + currentFlight.get.shortForm + " || Airline: " + airline + " || From: " + currentFlight.get.departure + " || To: " + currentFlight.get.destination + "\n" + "\n" +
        "Altitude: " + altitude + "m || Passengers: " + currentFlight.get.passengers + " || Minimum Runway Length: " + minRunwaylength + "m" + "\n" +
        "Fuel: " + fuel + " litres || Consumption: " + fuelConsumption + "l/min" + " || ETA: " + timeToDestination + "min" + "\n"
    }
    if (descendRunway.isDefined) basic += "LANDING RUNWAY: #" + descendRunway.get.number + " || Landing starts in " + math.max(this.timeToDestination - 10, 0) + "minutes"

    basic
  }

}


