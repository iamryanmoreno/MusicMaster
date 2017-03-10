/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;


import com.alee.laf.optionpane.WebOptionPane;
public class ErrorHandler {
    //displays an error message
    public static void display(String message) {
        WebOptionPane.showMessageDialog(null, message, "Error", WebOptionPane.ERROR_MESSAGE);
    }
}