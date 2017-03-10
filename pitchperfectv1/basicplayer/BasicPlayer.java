
package pitchperfectv1.basicplayer;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

// additional functionality not included into the jlGUI BasicPlayer
public abstract class BasicPlayer implements BasicController {
    //stopped, paused, playing states, need them to control Thread.
    public static final int UNKNOWN = -1;
    public static final int PLAYING = 0;
    public static final int PAUSED = 1;
    public static final int STOPPED = 2;
    public static final int OPENED = 3;
    public static final int SEEKING = 4;
    protected int m_status = UNKNOWN;
    // Listeners to be notified.
    protected Collection m_listeners = new ArrayList();
    protected String[] supportedFileTypeExtensions = {};
    public void addBasicPlayerListener(BasicPlayerListener bpl) {
        m_listeners.add(bpl);
    }
    
    //Returns BasicPlayer status.
    public int getStatus() {
        return m_status;
    }
    
    //Notify listeners about a BasicPlayerEvent.
    protected void notifyEvent(int code, int position, double value, Object description) {
        Iterator it = m_listeners.iterator();
        while (it.hasNext()) {
            BasicPlayerListener bpl = (BasicPlayerListener) it.next();
            bpl.stateUpdated(new BasicPlayerEvent(this, code, position, value, description));
        }
    }
    
    //Checks if the file is in a supported format
    public boolean isFileSupported(File file) {
        return isFileSupported(file.getName());
    }
    
    //Checks if the file is in a supported format
    public boolean isFileSupported(String filename) {
        System.out.println("Testing " + filename);
        for (int i = 0; i < supportedFileTypeExtensions.length; i++) {
            System.out.println("Compare " + supportedFileTypeExtensions[i]);
            if (filename.toLowerCase().endsWith(supportedFileTypeExtensions[i]))
                return true;
        }
        return false;
    }
    
    public String[] getSupportedFileTypeExtensions() {
        return supportedFileTypeExtensions;
    }
    
    public abstract void setSupportedFileTypeExtensions();
}

