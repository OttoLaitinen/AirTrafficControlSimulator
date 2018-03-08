

class LandQueue(val runway: Runway,val c: Int, val idN: Int) extends Queue(c, idN){
  
  def ascendPlane(airplane: Airplane): Unit = {
    this.getPlane(airplane).ascend(this.runway.number)
    this.removePlane(airplane)    
  }
  
  def ascendFirst(): Unit = this.planes(0).ascend(this.runway.number)
}