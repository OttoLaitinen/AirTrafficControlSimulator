/**@author Otto Laitinen*/

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
import GUI.StartScreen

object Test {

  def main(args: Array[String]) {
    val start = new StartScreen
    start.visible = true
    var level: Option[Int] = None

    while (level.isEmpty) {
      if (start.levelChosen.isDefined) level = start.levelChosen
      Thread.sleep(1)
    }
    start.close()
    
    val creator: Creator = {
      level.get match {
        //TODO Different files for the difficulty settings
        case 1 => new Creator("Config/test.json")
        case 2 => new Creator("Config/test.json")
        case 3 => new Creator("Config/test.json")
        case _ => throw new NullPointerException("LOOL")
      }
    }
    val airport: Airport = creator.createAirport
    

    val game = new MainGame(airport)
    game.visible = true
    while (game.isRunning) Thread.sleep(1)
    
    val end = new EndScreen(airport)
    end.visible = true

    println("End of main function")

  }

  /*Methods*/

}








