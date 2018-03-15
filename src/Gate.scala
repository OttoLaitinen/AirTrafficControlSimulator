

class Gate(
  val number: Int,
  var isInUse: Boolean = false,
  var currentPlane: Option[Airplane] = None) {
  
  var isReserved = currentPlane.isDefined
  
  def reserve(airplane: Airplane): Unit = currentPlane = Some(airplane)

  def unreserve(): Unit = currentPlane = None
  
}