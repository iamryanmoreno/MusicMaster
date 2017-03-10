
package pitchperfectv1.pkg0;


import java.util.Map;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import fr.unice.plugin.Plugin;

public interface PlayerPlugin extends Plugin{
	public void opened(Object stream, Map properties);
	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties);
	public void stateUpdated(BasicPlayerEvent event);
	public void setController(BasicController controller);
	public BasicPlayerListener getPlugin();

}

