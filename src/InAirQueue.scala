

class InAirQueue(val altitude: Double, val c: Int, val idN: Int) extends Queue(c, idN) {

  def descendPlane(airplane: Airplane, runwayNo: Int): Unit = {
    this.getPlane(airplane).descend(runwayNo)
    this.removePlane(airplane)
  }
}