package GUI

import Logic._
import java.awt.event.ActionListener
import scala.swing.SimpleSwingApplication
import scala.swing.ListView.AbstractRenderer
import scala.swing.ListView
import scala.swing.ListView.Renderer
import scala.swing.ListView.AbstractRenderer
import scala.swing.ListView.AbstractRenderer
import java.awt.Color
import scala.swing.event.MouseClicked
import java.awt.MouseInfo
import javax.swing.JOptionPane
import javax.swing.JFrame
import javax.swing.plaf.metal.MetalBorders.QuestionDialogBorder
import java.awt.Font
import scala.swing.PopupMenu
import scala.swing.MenuItem
import scala.swing.Action

class GatePopup (val airplane: Airplane, val airport: Airport) extends PopupMenu {
  val dialogFrame = new JFrame()
  
  if (airplane.currentFlight.isDefined) {
    /*Menu juttuja joissa esim. kiitoradalle tai jonoon lähettäminen*/
        contents += new MenuItem(new Action("Take off") {
          def apply() = {
            val possibilities: Array[Object] = airport.runways.map(_.number.toString()).toArray
            val numberAsString = JOptionPane.showInputDialog(dialogFrame, "Pick a runway.", "", JOptionPane.PLAIN_MESSAGE, icon, possibilities, possibilities(0)).toString()
            val number = Integer.parseInt(numberAsString)
            airplane.ascend(number)
      }
    })
  }
  
  else {
    /*Hangaarin lähettäminen aka koneen pelistä poistaminen*/
    contents += new MenuItem(new Action("Send to hangar") {
          def apply() = {
            airplane.gate.get.unreserve()
            airport.removePlane(airplane)
      }
    })
  }
}