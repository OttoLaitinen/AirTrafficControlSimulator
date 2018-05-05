package GUI
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

/**This is the start screen for the game. Here the player chooses the difficulty level for the game**/
class StartScreen extends MainFrame {
  private val width = 800
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
    text = "                          AIR TRAFFIC CONTROL SIMULATOR   "

  }

  val difficulty1 = new Button {
    text = "Kuopio -- EASY"
    minimumSize = new Dimension(width / 2, buttonHeight)
    maximumSize = new Dimension(width / 2, buttonHeight)
    background = Color.BLACK
    foreground = Color.WHITE
    listenTo(mouse.clicks)
    reactions += {
      case MouseClicked(_, p, _, _, _) => {

        levelChosen = Some(1)
        running = false

      }
    }
  }

  val difficulty2 = new Button {
    text = "Helsinki -- MEDIUM"
    minimumSize = new Dimension(width / 2, buttonHeight)
    maximumSize = new Dimension(width / 2, buttonHeight)
    background = Color.BLACK
    foreground = Color.WHITE
    listenTo(mouse.clicks)
    reactions += {
      case MouseClicked(_, p, _, _, _) => {
        levelChosen = Some(2)
        running = false
      }
    }
  }

  val difficulty3 = new Button {
    text = "Heathrow(London) -- HARD"
    minimumSize = new Dimension(width / 2, buttonHeight)
    maximumSize = new Dimension(width / 2, buttonHeight)
    background = Color.BLACK
    foreground = Color.WHITE
    listenTo(mouse.clicks)
    reactions += {
      case MouseClicked(_, p, _, _, _) => {
        levelChosen = Some(3)
        running = false
      }
    }
  }

  /**Contains information for the player.*/
  val infoPanel = new TextArea {
    text = "Hello and welcome to the Air Terminal Control Simulator.  \n\nThis is a game made by Otto Laitinen. \n\nIf you do not know how to play the game, please read the documentation provided with this project."
    background = Color.BLACK
    foreground = Color.WHITE
    wordWrap = true
    lineWrap = true
    preferredSize = new Dimension(width / 2, fullHeight - 100)

  }

  val infoScroller = new ScrollPane {
    contents = infoPanel
    background = Color.LIGHT_GRAY
    preferredSize = new Dimension(width / 2, fullHeight - 100)
    minimumSize = new Dimension(width / 2, fullHeight - 100)
    maximumSize = new Dimension(width / 2, fullHeight - 100)

    val toBeBorder = Swing.TitledBorder(Swing.LineBorder(Color.BLACK), "INFO")
    toBeBorder.setTitleFont(new Font("arial", Font.BOLD, 12))
    toBeBorder.setTitleColor(Color.BLACK)
    border = toBeBorder
  }

  val difficultyPanel = new BoxPanel(Orientation.Vertical) {
    contents += (difficulty1, Swing.VStrut(5), difficulty2, Swing.VStrut(5), difficulty3)
    background = Color.LIGHT_GRAY

    preferredSize = new Dimension(width / 2, fullHeight - 100)
    minimumSize = new Dimension(width / 2, fullHeight - 100)
    maximumSize = new Dimension(width / 2, fullHeight - 100)

    val toBeBorder = Swing.TitledBorder(Swing.LineBorder(Color.BLACK), "CHOOSE YOUR DIFFICULTY")
    toBeBorder.setTitleFont(new Font("arial", Font.BOLD, 12))
    toBeBorder.setTitleColor(Color.BLACK)
    border = toBeBorder
  }

  val mainPanel = new GridBagPanel {
    preferredSize = (new Dimension(width, fullHeight))
    /**Helper function to make constraints easier.*/
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
    add(difficultyPanel, constraints(0, 1))
    add(infoScroller, constraints(1, 1))
  }

  /*Settings for the window*/
  title = "Welcome to ATCS"
  minimumSize = new Dimension(width, fullHeight)
  preferredSize = new Dimension(width, fullHeight)
  maximumSize = new Dimension(width, fullHeight)
  contents = mainPanel
}