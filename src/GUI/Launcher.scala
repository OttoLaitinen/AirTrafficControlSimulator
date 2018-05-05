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

/**This is the launcher object that starts the game. 
 * It only runs one screen at a time.**/
object Launcher {

  def main(args: Array[String]) {
    //Creating the start screen that collects the difficulty setting
    val start = new StartScreen
    start.visible = true
    var level: Option[Int] = None 
    
    /**This loop keeps the start screen running until 
     * the difficulty setting has been chosen. **/
    while (level.isEmpty) {
      if (start.levelChosen.isDefined) level = start.levelChosen
      Thread.sleep(0)
    }
    start.close()
    
    /*Here the required creator and airport objects are created.*/
    val creator: Creator = {
      level.get match {
        //TODO Different files for the difficulty settings
        case 1 => new Creator("Config/Difficulty1.json")
        case 2 => new Creator("Config/Difficulty2.json")
        case 3 => new Creator("Config/Difficulty3.json")
      }
    }
    val airport: Airport = creator.createAirport
    

    /**Game holds the main game screen where all of the action happens.
     * The while loop keeps the game running until the game closes itself.**/
    val game = new MainGame(airport)
    game.visible = true
    while (game.isRunning) Thread.sleep(0)
    
    /**After the main game has closed an end screen is opened to show the reason for losing, points aquired and maybe some other stats.**/
    val end = new EndScreen(airport)
    end.visible = true

    //TODO Start info
    //TODO Update basic info in the info tab 
    

  }



}








