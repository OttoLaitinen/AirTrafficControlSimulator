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

  /*Methods*/
  def planesSortedByTime = airport.planes.sortWith(_.timeToDestination > _.timeToDestination)

  ////////////////////*GUI elements*/////////////////////////
  val closestAirplanes = new BoxPanel(Orientation.Vertical) {
    border = Swing.LineBorder(Color.BLACK)
    background = Color.lightGray
    def update = {
      this.contents.clear()
      planesSortedByTime.filterNot(airport.getPlanesAtGates.contains(_)).filterNot(airport.getPlanesOnRunways.contains(_)).foreach(plane => this.contents.+=:(new AirplaneTextArea(plane, airport)))
      this.revalidate()
    }
  }
  val newAirplanes = new BoxPanel(Orientation.Vertical) {
    border = Swing.LineBorder(Color.BLACK)
    background = Color.lightGray
    def update = {
      this.contents.clear()
      airport.planes.foreach(plane => this.contents.+=:(new AirplaneTextArea(plane, airport)))
      this.revalidate()
    }
  }
  
  val airplanesOnRunways = new BoxPanel(Orientation.Vertical) {
    border = Swing.LineBorder(Color.BLACK)
    background = Color.lightGray
    def update = {
      this.contents.clear()
      airport.getPlanesOnRunways.foreach(plane => this.contents.+=:(new AirplaneTextArea(plane, airport)))
      this.revalidate()
    }
  }

  val runwayPanel = new BoxPanel(Orientation.Vertical) {
    airport.runways.foreach(contents += new RunwayTextArea(_, airport))
    background = Color.gray
  }
  val runwayScroller = new ScrollPane {
    contents_=(runwayPanel)
  }

  val gatePanel = new BoxPanel(Orientation.Vertical) {
    airport.gates.foreach(contents += new GateTextArea(_, airport))
    background = Color.gray
  }
  val gateScroller = new ScrollPane { contents_=(gatePanel) }

  val refresh = new Button {
    text = "Refresh airplanelist"
    listenTo(mouse.clicks)
  }

  val airplaneInfo = new TabbedPane {
    preferredSize = (new Dimension(textAreaWidth, fullHeight - 10))
    pages.+=(new scala.swing.TabbedPane.Page("Closest Planes", closestAirplanes))
    pages.+=(new scala.swing.TabbedPane.Page("Newest Planes", newAirplanes))
    pages.+=(new scala.swing.TabbedPane.Page("Planes On  Runways", airplanesOnRunways))
  }

  val groundObjects = new TabbedPane {
    preferredSize_=(new Dimension(textAreaWidth, airplaneInfo.preferredSize.height / 2))
    pages.+=(new scala.swing.TabbedPane.Page("Runways", runwayScroller))
    pages.+=(new scala.swing.TabbedPane.Page("Gates", gateScroller))
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
        gatePanel.contents.foreach(_.asInstanceOf[GateTextArea].updateText)

        /*These tasks are done only every 20 ticks meaning every 400ms*/
        if (airport.tick % 20 == 0) {
          closestAirplanes.update
          newAirplanes.update
          airplanesOnRunways.update

        }

      }
    }
    /*Creates a timer that ticks every 20ms and then starts it.*/
    val timer = new javax.swing.Timer(20, listener)
    timer.start()
  }
}








