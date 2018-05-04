package GUI

import scala.swing.TextArea
import java.awt.Dimension
import Logic.Runway
import Logic.Airport
import java.awt.Font
import scala.swing._
import java.awt.Color
/**Displas information about a runway*/
class RunwayTextArea(val runway: Runway, val airport: Airport) extends TextArea {
  val textAreaWidth = 600
  val textAreaHeight = 80
  editable = false
  maximumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  minimumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  border = Swing.LineBorder(Color.BLACK)
  def crossingIsOccupied = airport.crossingRunways.get(runway).get.exists(_.isInUse)
  def isOccupied = runway.isInUse

  font = new Font("arial", Font.BOLD, 12)
  
  def crossingText: String = {
    if (airport.crossingRunways.get(runway).isEmpty) "NaN"
    else {
      var basic = airport.crossingRunways.get(runway).get.map(_.number.toString() + ", ").foldLeft("")(_ + _).dropRight(2)
      if (crossingIsOccupied) basic += "\n" + "!!! There are planes on crossing runways !!!"
      basic
    }
    
  }
  def updateText: Unit = {
    text = {
      var basic = "NUMBER: " + runway.number + "\n" + "\n" +
        "Length: " + runway.runwayLength + "m" + " || Condition: " + runway.condition + "%" + " || Crossing runways: " + crossingText + "\n"

      if (isOccupied) basic = basic + "Currently Occupied by: " + runway.currentPlane.get.currentFlight.get.shortForm + " || Time to destination: " + runway.currentPlane.get.timeToDestination
      else basic = basic + "Not occupied at the moment"

      basic
    }
    /*Colors:
     * if a runway is occupied it is red
     * else if crossing runways are occupied textarea is yellow*/
    if (isOccupied) background = Color.RED
    else if (crossingIsOccupied) background = Color.yellow
    else background = Color.WHITE
    repaint()
  }
}