package GUI

import scala.swing.TextArea
import java.awt.Dimension
import Logic.Runway
import Logic.Airport
import java.awt.Font
import scala.swing._
import java.awt.Color

class RunwayTextArea(val runway: Runway, val airport: Airport) extends TextArea {
  val textAreaWidth = 600
  val textAreaHeight = 80
  
  editable = false
  maximumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  minimumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  border = Swing.LineBorder(Color.BLACK) 
 
  font = new Font("Courier New", Font.BOLD , 12)
  def updateText: Unit = {
    text = runway.toString()
    repaint()
  }
}