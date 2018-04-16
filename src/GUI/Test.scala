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
import java.awt.Font

object Test extends SimpleSwingApplication {
  val width = 1700
  val fullHeight = 900
  val textAreaHeight = 80
  val textAreaWidth = 500

  /*Setting up the program */
  val creator: Creator = new Creator("Config/test.json")
  val airport = creator.createAirport
  var counter = 0

  ////////////////////*GUI elements*/////////////////////////
  val closestAirplanes = new BoxPanel(Orientation.Vertical) {
    border = Swing.LineBorder(Color.BLACK)
    background = Color.lightGray
    def update = {
      this.contents.clear()
      val planesSorted = airport.planes.sortWith(_.timeToDestination > _.timeToDestination)
      planesSorted.foreach(plane => this.contents.+=:(new AirplaneTextArea(plane, airport)))
      //airplanePanel.preferredSize_=(new Dimension(500, contents.length * 33))
      this.revalidate()
    }
  }
  val newAirplanes = new BoxPanel(Orientation.Vertical) {
    border = Swing.LineBorder(Color.BLACK)
    background = Color.lightGray
    def update = {
      this.contents.clear()
      val planesSorted = airport.planes
      planesSorted.foreach(plane => this.contents.+=:(new AirplaneTextArea(plane, airport)))
      //airplanePanel.preferredSize_=(new Dimension(500, contents.length * 33))
      this.revalidate()
    }
  }
  val planeScroller = new ScrollPane {
    contents_=(closestAirplanes)
    horizontalScrollBarPolicy_=(ScrollPane.BarPolicy.Never)
    verticalScrollBarPolicy_=(ScrollPane.BarPolicy.AsNeeded)
    preferredSize = (new Dimension(textAreaWidth, 500))
    //viewportView_=(airplanePanel)

  }

  val runwayPanel = new BoxPanel(Orientation.Vertical) {
    airport.runways.foreach(contents += new RunwayTextArea(_, airport))
    background = Color.gray
  }
  val runwayScroller = new ScrollPane {
    contents_=(runwayPanel)

  }

  val refresh = new Button {
    text = "Refresh airplanelist"
    listenTo(mouse.clicks)
    reactions += {
      case MouseClicked(_, p, _, _, _) => {
        planeScroller.revalidate()
        println("LOL")
      }
    }
  }

  val airplaneInfo = new TabbedPane {
    preferredSize = (new Dimension(textAreaWidth, fullHeight - 10))
    pages.+=(new scala.swing.TabbedPane.Page("Closest Planes", closestAirplanes))
    pages.+=(new scala.swing.TabbedPane.Page("Newest Planes", newAirplanes))
  }

  val groundObjects = new TabbedPane {
    preferredSize_=(new Dimension(textAreaWidth, airplaneInfo.preferredSize.height / 2))
    pages.+=(new scala.swing.TabbedPane.Page("Runways", runwayScroller))
    pages.+=(new scala.swing.TabbedPane.Page("Gates", new TextArea))
    pages.+=(new scala.swing.TabbedPane.Page("Queues", new TextArea))
  }
  val mainPanel = new GridBagPanel {
    preferredSize = (new Dimension(width, fullHeight))

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
    add(airplaneInfo, constraints(0, 0, gridheight = 2, fill = GridBagPanel.Fill.Vertical))
    add(groundObjects, constraints(1, 0))
    add(refresh, constraints(1, 1))
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

        /*These tasks are done only every 20 ticks meaning every 400ms*/
        if (airport.tick % 20 == 0) {
          closestAirplanes.update
          newAirplanes.update
        }

      }
    }
    /*Creates a timer that ticks every 20ms and then starts it.*/
    val timer = new javax.swing.Timer(20, listener)
    timer.start()
  }
}

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

class RunwayTextArea(val runway: Runway, val airport: Airport) extends TextArea {
  val textAreaWidth = 500
  val textAreaHeight = 80
  
  editable = false
  maximumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  minimumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  border = Swing.LineBorder(Color.BLACK)
 
  font = new Font("Courier New", Font.BOLD , 12)
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


