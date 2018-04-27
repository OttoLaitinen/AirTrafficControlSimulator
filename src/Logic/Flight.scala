package Logic

class Flight(
  val destination: String,
  val departure: String,
  val shortForm: String,
  val flightTime: Int,
  val passengers: Int,
  val plane: Airplane) {

  var timeToDestination: Int = flightTime
  
  def update: Unit = timeToDestination -= 1
  
  def completion: Double = BigDecimal(1 - (timeToDestination.toDouble / flightTime)).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  
  override def toString = shortForm
  
}