package GUI

import scala.swing.TextArea
import java.awt.Dimension
import Logic.Gate
import Logic.Airport
import java.awt.Font
import scala.swing._
import java.awt.Color

class GateTextArea (val gate: Gate, val airport: Airport) extends TextArea {
  val textAreaWidth = 500
  val textAreaHeight = 40
  
  editable = false
  maximumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  minimumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  border = Swing.LineBorder(Color.BLACK) 
  var oldtext:String = ""
 
  font = new Font("Courier New", Font.BOLD , 12)
  def updateText: Unit = {  
    if (gate.toString() != oldtext) {
      text = gate.toString()
      oldtext = gate.toString()
      repaint()
    }
    
  }
}