package Logic
/**Flight models one flight an airplane can carry. 
 * This class knows where the flight is going to and 
 * how long it takes to get there.*/
class Flight(
  val destination: String,
  val departure: String,
  val shortForm: String,
  val flightTime: Int,
  val passengers: Int,
  val plane: Airplane) {

  /**Models the time that is still needed for the airplane to reach the airport.
   * This value can be negative and is used as one factor when giving points to the player*/
  var timeToDestination: Int = flightTime
  
  /**Makes the plane to come closer to the airport*/
  def update: Unit = timeToDestination -= 1
  
  /**Percentage*/
  def completion: Double = BigDecimal(1 - (timeToDestination.toDouble / flightTime)).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  
  override def toString = shortForm
  
}