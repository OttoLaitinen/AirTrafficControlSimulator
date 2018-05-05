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

/**
 * If gate is reserved by a plane this popup menu may be displayed.
 * Contains options to either send a plane to a runway or to a hangar.
 */
class GatePopup(val airplane: Airplane, val airport: Airport) extends PopupMenu {
  val dialogFrame = new JFrame()

  if (airplane.currentFlight.isDefined) {
    contents += new MenuItem(new Action("Take off") {
      def apply() = {
        val possibilities: Array[Object] = airport.runways.map(_.number.toString()).toArray
        try {
          val numberAsString = JOptionPane.showInputDialog(dialogFrame, "Pick a runway.", "", JOptionPane.PLAIN_MESSAGE, icon, possibilities, possibilities(0)).toString()
          val number = Integer.parseInt(numberAsString)
          airplane.ascend(number)
        } catch {
          case a: NullPointerException =>
        }
      }
    })
  } else {
    contents += new MenuItem(new Action("Send to hangar") {
      def apply() = {
        airplane.gate.get.unreserve()
      }
    })
  }
}