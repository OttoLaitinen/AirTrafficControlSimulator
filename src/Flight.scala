

class Flight(
  val destination: String,
  val departure: String,
  val shortForm: String,
  val flightTime: Int,
  val plane: Airplane,
  var completion: Double) {

  override def toString = shortForm
  
}