/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.basicplayer;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;


//Composite player combines the BasicMP3Player and BasicMidiPlayer.
public class CompositePlayer extends BasicPlayer implements BasicController {
	private BasicPlayer mp3Player = new BasicMP3Player();
	private BasicPlayer midiKarPlayer = new BasicMidiPlayer();
	private BasicPlayer currentPlayer;
        
        public CompositePlayer() {
            setSupportedFileTypeExtensions();
	}
        
        public void open(InputStream in) throws BasicPlayerException {
            System.out.println("NOT IMPLEMENTED YET");
	}

	//Play a file
        public void open(File file) throws BasicPlayerException {
            // If this is a zip file, we need to unzip it and play the mp3 file inside.
            // We only support zipped mp3g files currently.
            if (file.getName().toLowerCase().endsWith(".zip"))
                file = ZipUtil.unzipMP3G(file);
            if (currentPlayer != null)
                currentPlayer.stop();
            
            String filename = file.getName().toLowerCase();
            if (filename.endsWith(".mid") || filename.endsWith(".kar")) {
                midiKarPlayer.open(file);
                currentPlayer = midiKarPlayer;
            } else {
                mp3Player.open(file);
                currentPlayer = mp3Player;
            }
	}
        
        public void open(URL url) throws BasicPlayerException {
            System.out.println("NOT IMPLEMENTED YET");
	}

	//Skip bytes.
        public long seek(long bytes) throws BasicPlayerException {
            if (currentPlayer != null)
                return currentPlayer.seek(bytes);
            return 0;
	}
        
        public void play() throws BasicPlayerException {
            if (currentPlayer != null)
                currentPlayer.play();
	}
        
        public void stop() throws BasicPlayerException {
            if (currentPlayer != null)
                currentPlayer.stop();
	}

	public void pause() throws BasicPlayerException {
            if (currentPlayer != null)
                currentPlayer.pause();
	}

	public void resume() throws BasicPlayerException {
            if (currentPlayer != null)
                currentPlayer.resume();
	}

	// Sets Pan (Balance) value. Linear scale : -1.0 <--> +1.0
        public void setPan(double pan) throws BasicPlayerException {
            if (currentPlayer != null)
                currentPlayer.setPan(pan);
	}

	//Sets Gain value. Linear scale 0.0 <--> 1.0
        public void setGain(double gain) throws BasicPlayerException {
            if (currentPlayer != null)
                System.out.println("in controller.setgain");
            currentPlayer.setGain(gain);
	}
        
        public void addBasicPlayerListener(BasicPlayerListener bpl) {
            mp3Player.addBasicPlayerListener(bpl);
            midiKarPlayer.addBasicPlayerListener(bpl);
	}

	//Composite player supports everything mp3Player and midiKarPlayer supports plus zip files(zipped mp3g).
        public void setSupportedFileTypeExtensions() {
            String[] tab1 = mp3Player.getSupportedFileTypeExtensions();
            String[] tab2 = midiKarPlayer.getSupportedFileTypeExtensions();
            supportedFileTypeExtensions = new String[tab1.length + tab2.length + 1];

            int j = 0;
            for (int i = 0; i < tab1.length; i++) {
                supportedFileTypeExtensions[j++] = tab1[i];
            }
            
            for (int i = 0; i < tab2.length; i++) {
                supportedFileTypeExtensions[j++] = tab2[i];
            }
            
            supportedFileTypeExtensions[j++] = ".zip";
        }
}

