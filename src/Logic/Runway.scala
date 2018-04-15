package Logic

class Runway(
  val length: Int,
  val number: Int,
  var usable: Boolean = true,  
  var condition: Double = 100.0,
  var currentPlane: Option[Airplane] = None) {
  
  def reserve(airplane: Airplane): Unit = currentPlane = Some(airplane)
  
  def unreserve(): Unit = currentPlane = None
  
  override def toString: String = {
    if(currentPlane.isDefined) "Number: " + number + " || " + "Length: " + length + "m" + " || " + "\n" + 
    "Currently Occupied by: " + currentPlane.get.currentFlight.get.shortForm +" || Time to destination: " + currentPlane.get.timeToDestination
    
    else "Number: " + number + " || " + "Length: " + length + "m" + " || " + "\n" + "Currently Occupied by: " + currentPlane
    
  }
  
  def clean: Unit = ???
  
  def isInUse: Boolean = currentPlane.isDefined
  
  
}