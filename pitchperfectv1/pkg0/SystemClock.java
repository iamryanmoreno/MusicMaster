/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;


import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class SystemClock extends WebPanel{
    Timer clockTimer;
    Date clockDate;
    //SimpleDateFormat clockDateFormat = new SimpleDateFormat("h : mm : ss aa ---   EEEE - MMMMMMMMM dd, yyyy");
    SimpleDateFormat clockDateFormat = new SimpleDateFormat("MMMMMMMMM dd, yyyy  |  EEEE  |  h : mm : ss  ");
    WebLabel lblTime;
    
    
    public SystemClock(){
        this.setLayout(new BorderLayout());
        Calendar clockCalendar = Calendar.getInstance();
        clockDate = clockCalendar.getTime();
        this.add(lblTime = new WebLabel(clockDateFormat.format(clockDate) + ""));
        lblTime.setForeground(Color.BLACK);
        lblTime.setBackground(Color.red);
        //lblTime.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        clockTimer = new Timer(990, new clockHandler());
        clockTimer.start();  
    }
    
    public class clockHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            Calendar clockCalendar = Calendar.getInstance();
            clockDate = clockCalendar.getTime();
            lblTime.setText (clockDateFormat.format (clockDate) + "");	
            
        }
            
    }
        
}

