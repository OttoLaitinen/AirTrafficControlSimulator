package Logic

class Gate(
  val number: Int,
  var isInUse: Boolean = false,
  var currentPlane: Option[Airplane] = None) {
  
  var isReserved = currentPlane.isDefined
  
  def reserve(airplane: Airplane): Unit = currentPlane = Some(airplane)

  def unreserve(): Unit = currentPlane = None
  
  override def toString() = {
    var basic =  "NUMBER: " + number + "\n" 
    if(currentPlane.isDefined) {
      if(currentPlane.get.currentFlight.isDefined) basic += "Occupied by: " + currentPlane.get.currentFlight.get.shortForm + ". This plane needs a runway with a minimum length of " + currentPlane.get.minRunwaylength + " meters." 
      else basic += "Occupied by a plane with no next flight."
    }
    else basic += "Currently not occupied."
    
    basic
  }
  
}