package GUI

import scala.swing.TextArea
import java.awt.Dimension
import Logic.Gate
import Logic.Airport
import java.awt.Font
import scala.swing._
import java.awt.Color
import scala.swing.event.MouseClicked

/**Displays information about a gate.
 * Only updates when there is a change in the gate's state.*/
class GateTextArea(val gate: Gate, val airport: Airport) extends TextArea {
  val textAreaWidth = 630
  val textAreaHeight = 40

  editable = false
  maximumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  minimumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  border = Swing.LineBorder(Color.BLACK)
  var oldtext: String = ""

  font = new Font("arial", Font.BOLD, 12)
  
  def updateText: Unit = {
    if (gate.toString() != oldtext) {
      text = gate.toString()
      oldtext = gate.toString()
      repaint()
    }
  }

  listenTo(mouse.clicks)
  reactions += {
    case MouseClicked(_, p, _, _, _) => {
      if (gate.currentPlane.isDefined) {
        val gatePopup = new GatePopup(gate.currentPlane.get, airport)
        gatePopup.show(this, p.x, p.y)
      }
      else {
        val newPop = new PopupMenu{
          contents += new MenuItem("No airplane present")
        }
        newPop.show(this, p.x, p.y)
      }
    }

  }
}