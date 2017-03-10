/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;

import com.alee.laf.optionpane.WebOptionPane;
import javazoom.jlgui.basicplayer.BasicPlayerException;
public class Indicator {
    //displays what have done in command line arguments
    public static void printMessage(String message) {
        System.out.println(message + " is added.");
    }
    
    public static void printError(String message){
        System.err.println("Failed to "+ message);
    }
    
    public static void printDone(String message){
        System.out.println(message);
    }
    //displays an error message
    public static void display(String message) {
        WebOptionPane.showMessageDialog(null, message, "Error", WebOptionPane.ERROR_MESSAGE);
    }
}

