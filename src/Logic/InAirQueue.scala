package Logic

/**InAirQueue extends Queue and is used to model queues in air or in other words altitudes 
 * where planes can wait before they can land.*/
class InAirQueue(val altitude: Double, val c: Int, val idN: Int) extends Queue(c, idN) {

  def descendPlane(airplane: Airplane, runwayNo: Int): Unit = {
    this.getPlane(airplane).descend(runwayNo)
    this.removePlane(airplane)
  }
}