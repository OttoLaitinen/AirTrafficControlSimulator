package GUI

import scala.swing._
import Logic.Airplane
import Logic.Airport
import java.awt.Dimension
import java.awt.Color
import java.awt.Font
import scala.swing.event.MouseClicked

class AirplaneTextArea(val airplane: Airplane, val airport: Airport) extends EditorPane {
  val textAreaWidth = 500
  val textAreaHeight = 80

  editable = false
  maximumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  minimumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  preferredSize = (new Dimension(textAreaWidth, textAreaHeight))
  border = Swing.LineBorder(Color.BLACK)
  
  if (airplane.altitude == 0 && airplane.timeToDestination == 0)background = Color.WHITE
  else if (airplane.isChangingAlt || (airplane.descendRunway.isDefined && airplane.timeToDestination < 30))  background = Color.YELLOW 
  else if (airplane.timeToDestination < airport.descendTime) background = Color.RED
  else background = Color.GREEN
  
  text = airplane.toString() + "\n"
  font = new Font("Courier New", Font.BOLD , 12)
  
  val planePopup = new AirplanePopup(airplane, airport)

  listenTo(mouse.clicks)
  reactions += {
    case MouseClicked(_, p, _, _, _) => {
      planePopup.show(this, p.x, p.y)

    }
  }

}