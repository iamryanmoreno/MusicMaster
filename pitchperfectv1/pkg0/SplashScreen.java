
package pitchperfectv1.pkg0;

import com.alee.laf.label.WebLabel;
import com.alee.laf.rootpane.WebWindow;
import javax.swing.*;
import java.awt.*;

public class SplashScreen extends WebWindow implements Runnable{
	public void run(){
		WebLabel SplashLabel = new WebLabel(new ImageIcon(getClass().getResource("/Images/PitchPerfectSplash.png")));
		Dimension screen = 	Toolkit.getDefaultToolkit().getScreenSize();
		getContentPane().add(SplashLabel,BorderLayout.CENTER);
		setSize(490,300);
		setLocation((screen.width - 490)/2,((screen.height-300)/2));
		show();
	}
}

