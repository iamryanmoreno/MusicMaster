/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javazoom.jlgui.basicplayer.*;

public class StopPlaySeekPlugin extends PanelPlugin implements BasicPlayerListener, ActionListener, MouseListener {
    
    public static final String NAME = "Stop Play Seek Plugin";
    private JButton play = null;
    private JButton stop = null;
    private JButton pause = null;
    private JButton next = null;
    private JButton prev = null;
    private JButton add = null;
    private JSlider slider = null;
    private DefaultBoundedRangeModel model = null;
    private int modelscale = 1000;
    private JPanel panelButton = null;
    private boolean isFirst = true;
    // for Audio
    private int byteslength = -1;
    private BasicController controller = null;
    private static boolean paused = false;
    
    public StopPlaySeekPlugin() throws HeadlessException {
        initUI();
    }

	/**
	 * Initialize UI
	 */
	public void initUI()
	{
		model = new DefaultBoundedRangeModel(0, 1, 0, modelscale);
		// seekLB = new JLabel("Seek : ");
		slider = new JSlider(model);
		slider.setPreferredSize(new Dimension(50, 20));
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(50);
		slider.setMinorTickSpacing(10);
		slider.addMouseListener(this);

		//Box b = Box.createVerticalBox();
                //Box b = Box.createHorizontalBox();
		// this.setLayout(new GridLayout(2, 0));
		panelButton = new JPanel();

		prev = MyClasses.addButton(panelButton, "prev", "prev", "/Images/bigPrev.gif");
		prev.addActionListener(this);

		play = MyClasses.addButton(panelButton, "play", "play", "/Images/bigPlay.gif");
		play.addActionListener(this);

		pause = MyClasses.addButton(panelButton, "pause", "pause", "/Images/bigPause.gif");
		pause.addActionListener(this);

		stop = MyClasses.addButton(panelButton, "stop", "stop", "/Images/bigStop.gif");
		stop.addActionListener(this);

		next = MyClasses.addButton(panelButton, "next", "next", "/Images/bigNext.gif");
		next.addActionListener(this);

		add = MyClasses.addButton(panelButton, "add", "add file", "/Images/bigEject.gif");
		add.addActionListener(this);

                JPanel panelControls = new JPanel();
                panelControls.setLayout(new BorderLayout());
                panelControls.add(slider, BorderLayout.CENTER);
                panelControls.add(panelButton, BorderLayout.EAST);
		slider.setEnabled(false);
		//b.add(slider);
		//b.add(Box.createRigidArea(new Dimension(1, 3)));
		//b.add(panelButton);
                //b.add(panelControls);
		this.add(panelControls);
	}
        
        public String getName() {
            return NAME;
	}
        
        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource() == play) {
                    if (Main.isActivate() && isFirst) {
                        File f = PlaylistPanel.getFileSelected();
                        controller.open(f);
                        isFirst = false;
                        PlaylistPanel.setPlaying(true);
                    }
                    controller.play();
                    slider.setEnabled(true);
                    
                } else if (e.getSource() == stop) {
                    controller.stop();
                    model.setValue(0);
                    slider.setEnabled(false);
                    PlaylistPanel.setPlaying(false);
                } else if (e.getSource() == next) {
                    controller.stop();
                    model.setValue(0);
                    PlaylistPanel.getNext();
                } else if (e.getSource() == prev) {
                    controller.stop();
                    model.setValue(0);
                    PlaylistPanel.getPrev();
                } else if (e.getSource() == pause) {
                    if (paused == true) {
                        controller.resume();
                        paused = false;
                    } else {
                        controller.pause();
                        paused = true;
                    }
                } else if (e.getSource() == add) {
                    JFileChooser fc = new JFileChooser();
                    fc.setMultiSelectionEnabled(false);
                    fc.setDialogTitle("Add a file");
                    fc.showOpenDialog(this);
                    File file = fc.getSelectedFile();
                    if (file != null) {
                        controller.open(file);
                        // if (Player.isActivate()) {
                        PlaylistPanel.addElementToPlayList(file.getAbsolutePath());
                        // }
                    }
                } 
            } catch (BasicPlayerException e1) {
                //e1.printStackTrace();
                ErrorHandler.display(e1.getMessage());
            }
	}
        
        public void mouseEntered(MouseEvent e)
	{}

	public void mouseExited(MouseEvent e)
	{}

	public void mousePressed(MouseEvent e)
	{}

	public void mouseClicked(MouseEvent e)
	{}
        
        public void mouseReleased(MouseEvent e) {
            if (e.getSource() == slider) {
                try {
                    long skp = -1;
                    // synchronized (model) {
                    skp = (long) ((model.getValue() * 1.0f / modelscale * 1.0f) * byteslength);
                    // }
                    controller.seek(skp);
                } catch (BasicPlayerException e1) {
                    //e1.printStackTrace();
                    ErrorHandler.display(e1.getMessage());
                }
            }
	}

	public void setController(BasicController controller) {
		this.controller = controller;
	}

	public void opened(Object stream, Map properties) {
            slider.setEnabled(true);
            if (properties.containsKey("audio.length.bytes")) {
                byteslength = ((Integer) properties.get("audio.length.bytes")).intValue();
            }
	}
        
        public void stateUpdated(BasicPlayerEvent event) {
		System.out.println("Player Event : " + event);
	}

	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
            float progress = bytesread * 1.0f / this.byteslength * 1.0f;
            model.setValue((int) (progress * modelscale));
	}

	public BasicPlayerListener getPlugin() {
		return this;
	}

	public String getVersion() {
		return "v1.0";
	}

}
