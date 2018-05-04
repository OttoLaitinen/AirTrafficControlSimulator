package GUI

import scala.collection.mutable.Queue
import scala.swing.BoxPanel
import scala.swing.Orientation
import Logic.Airport
import Logic.InAirQueue
import Logic.LandQueue
import java.awt.Color
import scala.swing._

/**Contains the textareas of the airplanes that currently are in the queue.*/
class InAirQueuePanel(queue: InAirQueue, airport: Airport) extends BoxPanel(Orientation.Vertical) {
  border = Swing.LineBorder(Color.BLACK)
  background = Color.lightGray
  
  def update = {
    this.contents.clear()

    queue.planes.foreach(plane => this.contents.+=:(new AirplaneTextArea(plane, airport)))

    this.revalidate()
  }
}