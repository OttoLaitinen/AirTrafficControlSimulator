

class Gate(
  val number: Int,
  var isInUse: Boolean = true,
  var currentPlane: Option[Airplane] = None) {
  
  
  def reserve(airplane: Airplane): Unit = currentPlane = Some(airplane)

  def unreserve(): Unit = currentPlane = None
  
}