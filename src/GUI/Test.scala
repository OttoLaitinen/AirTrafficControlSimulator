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
  val airplanePanel = new BoxPanel(Orientation.Vertical) {

  }
  val planeScroller = new ScrollPane {
    contents_=(airplanePanel)
    preferredSize = (new Dimension(500, 500))

  }

  val runwayPanel = new BoxPanel(Orientation.Vertical) {
    airport.runways.foreach(contents += new RunwayTextArea(_, airport))

  }
  val runwayScroller = new ScrollPane {
    contents_=(runwayPanel)
    preferredSize = (new Dimension(500, 500))

  }
  val mainPanel = new GridBagPanel {
    preferredSize = (new Dimension(1700, 1000))

    def constraints(x: Int, y: Int,
      gridwidth: Int = 1, gridheight: Int = 1,
      weightx: Double = 0.0, weighty: Double = 0.0,
      fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.None): Constraints = {
      val c = new Constraints
      c.gridx = x
      c.gridy = y
      c.gridwidth = gridwidth
      c.gridheight = gridheight
      c.weightx = weightx
      c.weighty = weighty
      c.fill = fill
      c
    }
    add(planeScroller, constraints(0, 0))
    add(runwayScroller, constraints(1, 0))
  }

  ///////////////////////////////////////////////////////////

  /*Setting up the mainframe*/
  def top = new MainFrame {
    title = "Test"

    /*Settings for the main window*/
    minimumSize = new Dimension(width, fullHeight)
    preferredSize = new Dimension(width, fullHeight)
    maximumSize = new Dimension(width, fullHeight)
    contents = mainPanel

    /*Listens to the timer and performs tasks always when the timer ticks*/
    val listener = new ActionListener() {
      def actionPerformed(e: java.awt.event.ActionEvent) = {

        

        /*Airport updates on every tick*/
        airport.onTick()
        runwayPanel.contents.foreach(_.asInstanceOf[RunwayTextArea].updateText)
        airplanePanel.contents.foreach(_.asInstanceOf[AirplaneTextArea].update)

        /*These tasks are done only every 20 ticks meaning every 400ms*/
        if (airport.tick % 20 == 0) {

          /*Planes are sorted by their distance from the airport
           * All textareas are remade on every update.*/

          airplanePanel.contents.clear()

          val planesSorted = airport.planes.sortWith(_.timeToDestination > _.timeToDestination)
          planesSorted.foreach(plane => airplanePanel.contents.+=:(new AirplaneTextArea(plane, airport)))
          planeScroller.contents_=(airplanePanel)

        }

      }
    }
    /*Creates a timer that ticks every 20ms and then starts it.*/
    val timer = new javax.swing.Timer(20, listener)
    timer.start()
  }
}

class AirplaneTextArea(val airplane: Airplane, val airport: Airport) extends TextArea {

  editable = false

  maximumSize_=(new Dimension(500, 33))
  minimumSize_=(new Dimension(500, 33))
  border = Swing.LineBorder(Color.BLACK)

  def update: Unit = {
    if (airplane.isChangingAlt) {
      background = Color.YELLOW
    } else if (airplane.timeToDestination < 30) background = Color.RED
    else background = Color.GREEN
    text = airplane.toString() + "\n"
    repaint()
  }

  val planePopup = new AirplanePopup(airplane, airport)

  listenTo(mouse.clicks)
  reactions += {
    case MouseClicked(_, p, _, _, _) => {
      planePopup.show(this, p.x, p.y)

    }
  }

}

class RunwayTextArea(val runway: Runway, val airport: Airport) extends TextArea {
  editable = false
  maximumSize_=(new Dimension(500, 33))
  minimumSize_=(new Dimension(500, 33))
  border = Swing.LineBorder(Color.BLACK)

  def updateText: Unit = {
    text = runway.toString()
    repaint()
  }
}

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


