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

class AirplanePopup(val airplane: Airplane, val airport: Airport) extends PopupMenu {
  val dialogFrame = new JFrame()
  if (airplane.isInAir) {
    //TODO Missä vaiheessa asioita ei voi enää muuttaa ja miten rajoitetaan
    contents += new MenuItem(new Action("Descend") {
      def apply() = {
        val possibilities: Array[Object] = airport.runways.map(_.number.toString()).toArray
        val numberAsString = JOptionPane.showInputDialog(dialogFrame, "Pick a runway.", "", JOptionPane.PLAIN_MESSAGE, icon, possibilities, possibilities(0)).toString()
        val number = Integer.parseInt(numberAsString)
        airplane.descend(number)
      }

    })
    contents += new MenuItem(new Action("Change altitude") {
      def apply() = {
        val possibilities: Array[Object] = (1000 to 15000 by 500).toArray.map(_.toString())
        val numberAsString = JOptionPane.showInputDialog(dialogFrame, "Write the altitude between 1000 and 15000:", "", JOptionPane.PLAIN_MESSAGE, icon, possibilities, possibilities(0)).toString()

        val number = Integer.parseInt(numberAsString)
        airplane.changeAltitude(number)
      }
    })
  } else {
    contents += new MenuItem(new Action("Kokeilu") { def apply() = println(airplane) })
  }
  contents += new MenuItem(new Action("Kokeilu") { def apply() = println(airplane) })
  contents += new MenuItem(new Action("Testi") { def apply() = println(airplane) })
}
