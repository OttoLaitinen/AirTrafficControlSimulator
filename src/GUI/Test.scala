package GUI

import scala.swing._
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

object Test extends SimpleSwingApplication {
  val width = 1920
  val height = 300
  val fullHeight = 1080

  /*Setting up the program */
  val creator: Creator = new Creator("Config/test.json")
  val airport = creator.createAirport
  var counter = 0

  ////////////////////*GUI elements*/////////////////////////
  val airplanePanel = new BoxPanel(Orientation.Vertical)
  val planeScroller = new ScrollPane {
    contents_=(airplanePanel)

  }
  val dialogFrame = new JFrame()

  ///////////////////////////////////////////////////////////

  /*Setting up the mainframe*/
  def top = new MainFrame {
    title = "Test"

    /*Settings for the main window*/
    minimumSize = new Dimension(width, fullHeight)
    preferredSize = new Dimension(width, fullHeight)
    maximumSize = new Dimension(width, fullHeight)
    contents = planeScroller

    /*Listens to the timer and performs tasks always when the timer ticks*/
    val listener = new ActionListener() {
      def actionPerformed(e: java.awt.event.ActionEvent) = {

        /*Airport updates on every tick*/
        airport.onTick()

        /*This is the panel for planes*/
        val planesSorted = airport.planes.sortBy(_.currentFlight.get.completion)
        if (airport.tick % 50 == 0) {
          airplanePanel.contents.clear()

          planesSorted.foreach(plane => airplanePanel.contents.+=:(new airplaneTextArea(plane) {

            val planePopup = new PopupMenu {
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

            listenTo(mouse.clicks)
            reactions += {
              case MouseClicked(_, p, _, _, _) => planePopup.show(this, p.x, p.y)
            }
          }))

          planeScroller.contents_=(airplanePanel)
        }
      }
    }
    /*Creates a timer that ticks every 20ms and then starts it.*/
    val timer = new javax.swing.Timer(20, listener)
    timer.start()
  }
}

class airplaneTextArea(val airplane: Airplane) extends TextArea {
  editable = false
  text = airplane.toString() + "\n"
  maximumSize_=(new Dimension(500, 30))
  minimumSize_=(new Dimension(500, 30))
  border = Swing.LineBorder(Color.BLACK)
  background = Color.red
}


