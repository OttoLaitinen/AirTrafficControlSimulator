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

class EndScreen(airport: Airport) extends MainFrame {
  private val width = 400
  private val fullHeight = 600
  private val buttonHeight = 155
  private var running = true

  var levelChosen: Option[Int] = None
  def isRunning = running

  val bigTitle = new TextArea {
    editable = false
    background = Color.BLACK
    foreground = Color.WHITE
    font = new Font("arial", Font.BOLD, 24)
    text = "                  GAME OVER   "

  }

  val infoPanel = new TextArea {
    val ending = if (airport.endingReason.isDefined) airport.endingReason.get else "None"
    text = "Thank you for playing ATCS and please try again. \n\n" +
      "Your game ended because of this reason: \n" +
      ending + "\n \n" +
      "Here are your stats: \n\n" +
      "Points: " + airport.points + "\n\n" + 
      "There might be other stats later but this is it for now..."
      
    background = Color.BLACK
    foreground = Color.WHITE
    wordWrap = true
    lineWrap = true
    preferredSize = new Dimension(width, fullHeight - 100)
    minimumSize = new Dimension(width, fullHeight - 100)
    maximumSize = new Dimension(width, fullHeight - 100)

    val toBeBorder = Swing.TitledBorder(Swing.LineBorder(Color.BLACK), "INFO")
    toBeBorder.setTitleFont(new Font("arial", Font.BOLD, 12))
    toBeBorder.setTitleColor(Color.BLACK)
    border = toBeBorder

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
    background = Color.LIGHT_GRAY
    add(bigTitle, constraints(0, 0, gridwidth = 2, fill = GridBagPanel.Fill.Horizontal))
    add(infoPanel, constraints(0, 1))
  }

  title = "GAME OVER"
  minimumSize = new Dimension(width, fullHeight)
  preferredSize = new Dimension(width, fullHeight)
  maximumSize = new Dimension(width, fullHeight)
  contents = mainPanel

}





