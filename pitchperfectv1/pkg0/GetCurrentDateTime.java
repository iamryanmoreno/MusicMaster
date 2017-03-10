/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
 
 
public class GetCurrentDateTime {
  public static void main(String[] args) {
 
	   DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
           
	   DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
	   //get current date time with Date()
	   Date date = new Date();
	   System.out.println(dateFormat.format(date) + " " + dateFormat2.format(date));
 
	   //get current date time with Calendar()
	   Calendar cal = Calendar.getInstance();
	   System.out.println(dateFormat.format(cal.getTime()));
 
  }
}