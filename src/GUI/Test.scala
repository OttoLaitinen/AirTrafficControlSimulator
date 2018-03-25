package GUI

import scala.swing._
import Logic._
import java.awt.event.ActionListener

object Test extends SimpleSwingApplication {
  val width = 160
  val height = 40
  val fullHeight = 50

  val creator: Creator = new Creator("Config/test.json")
  val airport = creator.createAirport
  var counter = 0

  val button = new Button {
    action = Action("Create plane") {
      println()
      airport.onTick()
      println("CLICK NUMBER: " + counter + " || Planes: " + airport.planes.length)
      airport.planes.foreach(println(_))
      println()
      counter += 1
    }
  }

  def top = new MainFrame {
    title = "Test"

    minimumSize = new Dimension(width, fullHeight)
    preferredSize = new Dimension(width, fullHeight)
    maximumSize = new Dimension(width, fullHeight)

    //contents = button

    val listener = new ActionListener() {
      def actionPerformed(e: java.awt.event.ActionEvent) = {
        airport.onTick()
      
        
        
        if(airport.tick % 50 == 0) {
        println()
        println("SECONDS: " + airport.time + " || Planes: " + airport.planes.length)
        airport.planes.foreach(println(_))
        println()
        }
        counter += 1
      }
    }

    val timer = new javax.swing.Timer(20, listener)
    timer.start()
  }
}