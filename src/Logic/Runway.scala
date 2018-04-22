package Logic

class Runway(
  val length: Int,
  val number: Int,
  var usable: Boolean = true,  
  var condition: Double = 100.0,
  var currentPlane: Option[Airplane] = None) {
  
  def reserve(airplane: Airplane): Unit = {
    if (currentPlane.isEmpty) currentPlane = Some(airplane)
    else {
      println("You assigned two planes on the same runway (Runway: "+ number + ") and it caused a fatal crash.")
      currentPlane.get.crash()
      airplane.crash()
    }
  }
  
  def unreserve(): Unit = currentPlane = None
  
  override def toString: String = {
   var basic =  "NUMBER: " + number + "\n" + "\n" + 
   "Length: " + length + "m" + " || " + "Condition: " + condition + "%" + "\n"
    
    if (currentPlane.isDefined) basic = basic + "Currently Occupied by: " + currentPlane.get.currentFlight.get.shortForm +" || Time to destination: "  + currentPlane.get.timeToDestination
    else basic = basic + "Not occupied at the moment"
    
    basic

  }
  
  def clean: Unit = ???
  
  def isInUse: Boolean = currentPlane.isDefined
  
  
}