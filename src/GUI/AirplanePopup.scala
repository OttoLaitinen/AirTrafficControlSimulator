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

/**AirplanePopup is used to command airplanes in the game*/
class AirplanePopup(val airplane: Airplane, val airport: Airport) extends PopupMenu {
  val dialogFrame = new JFrame()

  /*Popup has different options depending on the planes position in the sky or on land.*/

  if (airplane.isInAir && airplane.altitude >= 1000) {

    contents += new MenuItem(new Action("Descend") {
      def apply() = {
        val possibilities: Array[Object] = airport.runways.map(_.number.toString()).toArray
        try {
          val numberAsString = JOptionPane.showInputDialog(dialogFrame, "Pick a runway.", "", JOptionPane.PLAIN_MESSAGE, icon, possibilities, possibilities(0)).toString()
          val number = Integer.parseInt(numberAsString)
          airplane.descend(number)
        } catch {
          case a: NullPointerException =>
        }
      }

    })

    if (!airplane.isInQueue) {

      contents += new MenuItem(new Action("Change altitude") {
        def apply() = {
          val possibilities: Array[Object] = (1000 to 15000 by 500).map(_ + "m").toArray.map(_.toString())
          try {
            val numberAsString = JOptionPane.showInputDialog(dialogFrame, "Write the altitude between 1000 and 15000:", "", JOptionPane.PLAIN_MESSAGE, icon, possibilities, possibilities(0)).toString().dropRight(1)

            val number = Integer.parseInt(numberAsString)
            airplane.changeAltitude(number)
          } catch {
            case a: NullPointerException =>
          }
        }
      })

    }

    if (airplane.isInAir && airplane.goToInAirQueue.isEmpty && airplane.altitude >= 1000) {

      contents += new MenuItem(new Action("Send to queue around the airport") {
        def apply() = {
          val possibilities: Array[Object] = (airport.queuesInAir.map(_.idN + "m")).toArray.map(_.toString())

          if (!possibilities.isEmpty) {
            try {
              val numberAsString = JOptionPane.showInputDialog(dialogFrame, "Pick an altitude", "", JOptionPane.PLAIN_MESSAGE, icon, possibilities, possibilities(0)).toString().dropRight(1)
              val number = Integer.parseInt(numberAsString)
              airplane.sendToQueue(number)
            } catch {
              case a: NullPointerException =>
            }

          } else JOptionPane.showMessageDialog(dialogFrame, "There are no free spaces in queues", "Problem", JOptionPane.ERROR_MESSAGE, icon)
        }
      })

    }

  } else if (!airplane.isInAir && airplane.timeToDestination <= 0) {

    contents += new MenuItem(new Action("Send to gate") {
      def apply() = {
        val possibilities: Array[Object] = (airport.getFreeGates.map(_.number)).toArray.map(_.toString())

        if (!possibilities.isEmpty) {
          try {
            val numberAsString = JOptionPane.showInputDialog(dialogFrame, "Pick a free gate", "", JOptionPane.PLAIN_MESSAGE, icon, possibilities, possibilities(0)).toString()
            val number = Integer.parseInt(numberAsString)
            airplane.sendToGate(number)
          } catch {
            case a: NullPointerException =>
          }
        } else JOptionPane.showMessageDialog(dialogFrame, "There is no free gates available", "Problem", JOptionPane.ERROR_MESSAGE, icon)
      }
    })

  }
}
