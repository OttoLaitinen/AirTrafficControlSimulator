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
  if (airplane.currentFlight.isDefined) {
    /*Menu juttuja joissa esim. kiitoradalle tai jonoon lähettäminen*/
  }
  else {
    /*Hangaarin lähettäminen aka koneen pelistä poistaminen*/
    contents += new MenuItem("Example")
  }
}