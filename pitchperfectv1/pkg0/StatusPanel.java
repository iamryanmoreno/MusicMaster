/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;

import com.alee.laf.panel.WebPanel;
import java.awt.BorderLayout;
import java.io.File;
import java.net.URL;
import java.util.Map;
import javax.swing.border.EtchedBorder;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import javax.swing.*;
import javax.swing.border.Border;

public class StatusPanel extends PanelPlugin implements BasicPlayerListener {
    
    SystemClock clock;
    WebPanel panelStatus;
    JLabel lblStatus;
    private JTextField sourceTF = null;
    private String audiotype = null;
    private LabelClock time;
    private JLabel title = null;
    private DefilPanel dp = null;
    private int speed = 100;
    static Border DefaultBorder;
    private int byteslength = -1;
    private int milliseconds = -1;
    
    public StatusPanel(){
        panelStatus = new WebPanel();
        panelStatus.setLayout(new BorderLayout());
        clock = new SystemClock();
        lblStatus = new JLabel("Default");
        panelStatus.add(lblStatus, BorderLayout.WEST);
        panelStatus.add(clock, BorderLayout.EAST);
        panelStatus.setBorder(new EtchedBorder());
        add(panelStatus, BorderLayout.PAGE_END);
    }
    
    

    @Override
    public void opened(Object stream, Map properties) {
        if (properties.containsKey("audio.length.bytes")) {
            byteslength = ((Integer) properties.get("audio.length.bytes")).intValue();
        }
        
        if (properties.containsKey("duration")) {
            milliseconds = (int) (((Long) properties.get("duration")).longValue()) / 1000;
        }
        
        if (stream != null) {
            System.out.println("stream != null");
            if (stream instanceof File) {
                lblStatus.setText(((File) stream).getName());
            } else if (stream instanceof URL) {
                lblStatus.setText(((URL) stream).toString());
            }
        }
    }

    @Override
    public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
    }

    @Override
    public void stateUpdated(BasicPlayerEvent event) {
    }

    @Override
    public void setController(BasicController controller) {
    }

    @Override
    public BasicPlayerListener getPlugin() {
        return this;
    }
    
}
