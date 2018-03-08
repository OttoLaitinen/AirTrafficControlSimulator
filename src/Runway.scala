

class Runway(
  val length: Int,
  val number: Int,
  var usable: Boolean = true,
  var isInUse: Boolean = true,
  var condition: Double = 100.0,
  var currentPlane: Option[Airplane] = None) {
  
  def reserve(airplane: Airplane): Unit = currentPlane = Some(airplane)
  
  def unreserve(): Unit = currentPlane = None
  
  def clean: Unit = ???
  
  
}