package Logic

/**Class Airplane is used to model an airplane in the game.
 * Airplane is a physical vehicle and thus doesn't know its destination
 * or passenger count as these vary depending on the flight. (See class Flight)*/
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
  var wantedAltitude: Int = altitude //meters
  private var ascendingTimer: Int = 0 
  
  var isInAir = true
  var hasReservedRunway: Boolean = false
  private var hasNotified: Boolean = false
  
  /*Airplane uses these variables to automate its movements*/
  var descendRunway: Option[Runway] = None
  var goToInAirQueue: Option[InAirQueue] = None
  var ascendingRunway: Option[Runway] = None
  
  /*For easier searching, airplane knows where it is.*/
  var gate: Option[Gate] = None
  private var queuingIn: Option[InAirQueue] = None

  
  

  /*Functions and methods*/
  
  /**Changes the wantedAltitude variable which determines what is the current aimed altitude for the plane.
   * wantedAltitude is equal to the current altitude when the plane is not changeing altitude.*/
  def changeAltitude(newAltitude: Int): Unit = wantedAltitude = newAltitude

  /**Checks if the plane has to changeAltitude to meet its wantedAltitude. If changes are needed the plane moves towards the wantedAltitude
   * incrementally.*/
  def moveAirplane: Unit = if (wantedAltitude - altitude < 0) altitude -= 10 else if (wantedAltitude - altitude > 0) altitude += 10

  /**Method that makes the airplane act:
   * calls the necessary methods to make airplane move and act in wanted way.*/
  def checkAirplane: Unit = {
    moveAirplane
    fuelOperations
    
    if (descendRunway.isDefined) descendingOperations 
    else if (goToInAirQueue.isDefined) queueOperations
    else if (ascendingRunway.isDefined) ascendingOperations
    else if (!descendRunway.isDefined && this.timeToDestination < 30 && !hasNotified && isInAir) { //If the Airplane has no instructions and it's ready to start landing procedures immediatly, it sends a notification.
      airport.addNotification("Flight " + currentFlight.get.shortForm + " is ready for landing.")
      hasNotified = true
    }
    
    //If the plane has no instructions when arriving to the airport. It automatically seeks a free queuing altitude.
    else if (this.timeToDestination <= 0 && !airport.getFreeInAirQueues.isEmpty) this.sendToQueue(airport.getFreeInAirQueues(0).idN) 
    else if (this.timeToDestination <= 0) {
      airport.endingReason = Some("Plane had no instructions when arriving to the airspace of the airport and all the queuing altitudens were full. This resulted in a crash.")
      this.crash()
    }
  }
  
  /**Starts the take off procedures for the airplane:
   * unreserves the gate,
   * reserves the wanted runway,
   * sends a notification and
   * starts the "countdown"*/
  def ascend(runwayNo: Int): Unit = {
    airport.addPlane(this)
    gate.get.unreserve()
    gate = None
    ascendingRunway = Some(airport.getRunwayNo(runwayNo))
    airport.getRunwayNo(runwayNo).reserve(this)
    airport.addNotification("Flight " + this.currentFlight.get.shortForm + " has reserved runway number " + runwayNo + " for take off.")
    ascendingTimer = 10
  }

  /**Instructs the airplane to land on a specific runway when it's ready.
   * Also resets every instruction given before and sends a notification.*/
  def descend(runwayNo: Int): Unit = {
    goToInAirQueue = None
    queuingIn = None
    descendRunway = Some(airport.getRunwayNo(runwayNo))

    airport.addNotification("Flight " + this.currentFlight.get.shortForm + " will land on runway number " + runwayNo + ".")

  }

  /**Crashes the airplane thus ending the game. 
   * (For now crashing a plane ends the game)*/
  def crash(): Unit = {
    println("Plane carrying flight " + currentFlight.get.shortForm + " has crashed")
    airport.addNotification("Flight " + this.currentFlight.get.shortForm + " has crashed.")
    airport.gameIsOn = false
  }

    /**Instructs the airplane to go to a specific queue when it's ready.
   * Also resets every instruction given before and sends a notification.*/
  def sendToQueue(number: Int): Unit = {
    descendRunway = None
    val queue = airport.getQueueNo(number)
    if (queue.isInstanceOf[InAirQueue]) goToInAirQueue = Some(queue.asInstanceOf[InAirQueue])
    airport.addNotification("Flight " + this.currentFlight.get.shortForm + " is now instructed to queue in " + number + " meters.")
  }
  
  /**Plane immediatly leaves the runway and goes to the wanted gate.
   * If the plane has a nextFlight the passengers and bags are changed and the plane
   * is made ready to leave the airport again*/
  def sendToGate(number: Int): Unit = {
    
    if (this.descendRunway.isDefined) {
      airport.points += this.currentFlight.get.timeToDestination * 10 + this.fuel * 1

      hasReservedRunway = false
      this.descendRunway.get.unreserve()
      descendRunway = None

      airport.addNotification("Flight " + this.currentFlight.get.shortForm + " is going to gate number " + number + ".")
      airport.removePlane(this)
      gate = Some(airport.getGateNo(number))
      gate.get.reserve(this)
    }
    
    if (this.nextFlight.isDefined) {
      val oldFlight = currentFlight.get 
      currentFlight = nextFlight
      this.refuel()
      airport.addNotification("Plane that was carrying " + oldFlight.shortForm + " is now ready to take off as flight  " + currentFlight.get.shortForm + ".")
    } else {
      currentFlight = None
    }

  }

  /**Gives the time remaining before the plane is at the airport. 
   * Doesn't display the time that the plane is late from its schedule.*/
  def timeToDestination: Int = if (currentFlight.isDefined) math.max(currentFlight.get.timeToDestination, 0) else 0

  /**Takes care of the airplanes landing. 
   * First when there is under 30 minutes to the airport the plane starts descending to 1500 meters.
   * When there is under 10 minutes to the airport the plane reserves the runway and lands.
   * Lastly the plane notifies that it has landed succesfully.
   * If the reserved runway already has an airplane on it the game end because of a crash.*/
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

  /**If the plane is instructed to go queing around the airport,
   * this method changes the altitude and reserves a place for the plane in the queue.*/
  def queueOperations: Unit = {
    if (this.timeToDestination < 30 && !queuingIn.exists(_ == goToInAirQueue.get)) {
      this.changeAltitude(goToInAirQueue.get.altitude.toInt)
      goToInAirQueue.get.addPlane(this)
      queuingIn = Some(goToInAirQueue.get)
      airport.addNotification("Flight " + this.currentFlight.get.shortForm + " is now queuing in " + queuingIn.get.idN + " meters.")
    }
  }

  /**Takes care of the airplane's take off.
   * First it takes time off the countdown timer
   * And then it send the plane on its way
   * (Currently in the game taking off from the airport means that the 
   * plane is no longer controlled from the airport and the plane is deleted from the game*/
  def ascendingOperations: Unit = {

    if (airport.tick % 50 == 0) ascendingTimer -= 1

    if (ascendingTimer <= 0) {
      airport.addNotification("Flight " + this.currentFlight.get.shortForm + " took off succesfully.")
      ascendingRunway.get.unreserve()
      ascendingRunway = None
      airport.removePlane(this)
    }
  }
  
/**Checks the fuel of the airplane and also makes the airplane to consume fuel.
 * If the airplane runs out of fuel it crashes.
 * Also if there is only 30 minutes worth of fuel left the plane sends a notification.*/
  def fuelOperations: Unit = {
    if (airport.tick % 50 == 0 && isInAir) {
      fuel = fuel - fuelConsumption.toInt
    }
    
    if (fuel <= 0) {
      airport.endingReason = Some("Plane carrying flight " + this.currentFlight.get.shortForm + " ran out of fuel and crashed.")
      this.crash()         
    } 
    else if (fuel - 30 * fuelConsumption <= 0) airport.addNotification("Plane carrying flight " + this.currentFlight.get.shortForm + "is running out of fuel!")
  }

  def isChangingAlt: Boolean = wantedAltitude != altitude
  
  def refuel(): Unit = fuel = fuelCapacity

  def timeToAscend: Int = ascendingTimer
  
  def isInQueue: Boolean = queuingIn.isDefined
  
 


}


