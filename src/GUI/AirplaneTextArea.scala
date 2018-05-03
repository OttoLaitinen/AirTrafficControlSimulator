package GUI

import scala.swing._
import Logic.Airplane
import Logic.Airport
import java.awt.Dimension
import java.awt.Color
import java.awt.Font
import scala.swing.event.MouseClicked

class AirplaneTextArea(val airplane: Airplane, val airport: Airport) extends EditorPane {
  val textAreaWidth = 600
  val textAreaHeight = 80

  editable = false
  maximumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  minimumSize_=(new Dimension(textAreaWidth, textAreaHeight))
  preferredSize = (new Dimension(textAreaWidth, textAreaHeight))
  border = Swing.LineBorder(Color.BLACK)

  if (airplane.altitude == 0 && airplane.timeToDestination == 0) background = Color.WHITE
  else if (airplane.isChangingAlt || (airplane.descendRunway.isDefined && airplane.timeToDestination < 30)) background = Color.YELLOW
  else if (airplane.timeToDestination < airport.descendTime) background = Color.RED
  else background = Color.GREEN

  text = {
    var basic = ""
    
    if (airplane.currentFlight.isDefined) {
      basic += "Flight: " + airplane.currentFlight.get.shortForm + " || Airline: " + airplane.airline + " || From: " + airplane.currentFlight.get.departure + " || To: " + airplane.currentFlight.get.destination + "\n" + "\n" +
        "Altitude: " + airplane.altitude + "m || Passengers: " + airplane.currentFlight.get.passengers + " || Minimum Runway Length: " + airplane.minRunwaylength + "m" + "\n" +
        "Fuel: " + airplane.fuel + " litres || Consumption: " + airplane.fuelConsumption + "l/min" + " || ETA: " + airplane.timeToDestination + "min" + "\n"
    }
    
    if (airplane.descendRunway.isDefined) basic += "LANDING RUNWAY: #" + airplane.descendRunway.get.number + " || Landing starts in " + math.max(airplane.timeToDestination - 10, 0) + "minutes"
    else if (airplane.ascendingRunway.isDefined) basic += "TAKE OFF RUNWAY: #" + airplane.ascendingRunway.get.number + " || Takeoff complete in " + airplane.timeToAscend + "minutes"
    else if (airplane.goToInAirQueue.isDefined) basic += "GOING TO QUEUE IN " + airplane.goToInAirQueue.get.idN + " meters"
    
    basic
  }
  font = new Font("arial", Font.BOLD, 12)

  val planePopup = new AirplanePopup(airplane, airport)

  listenTo(mouse.clicks)
  reactions += {
    case MouseClicked(_, p, _, _, _) => {
      planePopup.show(this, p.x, p.y)

    }
  }

}