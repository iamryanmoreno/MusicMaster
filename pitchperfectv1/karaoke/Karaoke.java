
package pitchperfectv1.karaoke;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.sound.midi.*;
import javax.swing.*;

import pitchperfectv1.basicplayer.*;
import pitchperfectv1.pkg0.Iconifiable;

//MIDI Karaoke player
public class Karaoke extends JFrame implements MetaEventListener {

	private KaraokePane karaokePane;
	private Vector song = null;
	private Vector frames = null;
	private KaraokeProperties props = null;
	private Sequencer sequencer = null;
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel toolbar = new JPanel();
	JScrollBar tempoSlider = new JScrollBar();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JPanel jPanel1 = new JPanel();
	JButton jButtonPreferences = new JButton();
	private String artist;
	private String songTitle;
	JPopupMenu jPopupMenu1 = new JPopupMenu();
	JMenuItem jMenuItem1 = new JMenuItem();
	JCheckBoxMenuItem jCheckBoxMenuItem1 = new JCheckBoxMenuItem();
	JMenuItem jMenuItem3 = new JMenuItem();

	private Iconifiable playerUI;

	public Karaoke(Sequencer sequencer)
	{

		this.sequencer = sequencer;
		sequencer.addMetaEventListener(this);
		props = new KaraokeProperties();
		karaokePane = new KaraokePane(props);

		try
		{
			jbInit();
			//getContentPane().add(karaokePane, BorderLayout.CENTER);
			MouseListener popupListener = new PopupListener();
			addMouseListener(popupListener);


		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setPlayerUI(Iconifiable playerUI)
	{
		this.playerUI = playerUI;
	}

        /*
	protected void goToFullScreen()
	{
		toolbar.setVisible(false);
		jPanel1.setVisible(false);

		playerUI.minimize();
		super.goToFullScreen();
		// karaokePane.redrawAll();
	}

	protected void goToWindowedMode()
	{
		toolbar.setVisible(true);
		jPanel1.setVisible(true);

		playerUI.setToOriginalSize();
		super.goToWindowedMode();
		// karaokePane.redrawAll();
	}

*/
	class PopupListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e)
		{
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e)
		{
			if (e.isPopupTrigger())
			{
				jPopupMenu1.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	/**
	 * We redefine the inherited method so that visibility is always set to false
	 */
	public void exiting()
	{
		setVisible(false);

	}


	// The window will appear centered
	public void setVisibleOld(boolean flag)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width - frameSize.width) / 2,
		         (screenSize.height - frameSize.height) / 2);
		super.setVisible(flag);
	}

	/**
	 * Positioning in the sequence
	 * 
	 * @param pos
	 *           new position in ticks
	 */
	public void seek(long pos)
	{
		karaokePane.seek(pos, true);
	}

	/**
	 * Loads new sequence, formats the text, sets text to the karaoke pane if seq != null and closes
	 * song if seq = null. Returns true if we have a karaoke file (.kar or midi with text events)
	 * 
	 * @param seq
	 *           sequence to process
	 */
	public boolean setSong(Sequence seq) throws Exception
	{
		Lyrics.setCurrentCharset(props.charset);
		song = Lyrics.read(seq);

		System.out.println("Nb frames dans le morceau : " + song.size());
		if (song.size() == 0)
			return false;

		Lyrics.preformat(song, props.cols);
		artist = Lyrics.getArtist();
		songTitle = Lyrics.getTitle();
		System.out.println("***SET SONG*** : " + artist + " " + songTitle);

		frames = Lyrics.format(song, props.readLine, props.lines, seq.getTickLength());
		karaokePane.setSong(song, frames, props);
		tempoSlider.setEnabled(seq != null);
		// make sure visualKaraoke Frame is visible
		setVisible(true);
		return true;
	}

	/**
	 * Callback method for meta event listener. Sifts text events and sends them to karaoke pane
	 * 
	 * @param mm
	 *           MIDI meta message
	 */
	public void meta(MetaMessage mm)
	{
		byte[] data = mm.getData();
		if (data.length > 0 && data[0] == '@')
			return;

		if (mm.getType() == Syllable.ST_TEXT || mm.getType() == Syllable.ST_LYRICS)
		{
			karaokePane.seek(sequencer.getTickPosition(), false);
		}
	}

	/**
	 * Class that promotes pulsation ribbon every 25 milliseconds When playback is stopped, method
	 * stopIt is called to redraw control buttons.
	 */

	public void pulse(long pos)
	{
		karaokePane.pulse(pos);
	}

	/**
	 * Sets tempo. x should be x greater than zero
	 * 
	 * @param x
	 *           tempo factor
	 */
	void setTempo(float x)
	{
		if (sequencer != null)
			sequencer.setTempoFactor(x);
	}

	public Karaoke()
	{
		super("Kar Midi Lyrics Player");
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		this.getContentPane().setLayout(borderLayout1);
		this.getContentPane().setBackground(UIManager.getColor("Tree.selectionBorderColor"));
		tempoSlider.setMaximum(10);
		tempoSlider.setMinimum(1);
		tempoSlider.setOrientation(JScrollBar.HORIZONTAL);
		// tempoSlider.setValue(5);
		tempoSlider.setValues(5, 1, 0, 10);
		tempoSlider.addAdjustmentListener(new Karaoke_tempoSlider_adjustmentAdapter(this));
		jLabel1.setText("Slower");
		jLabel2.setText("Faster");
		toolbar.setLayout(gridBagLayout1);
		toolbar.setBackground(UIManager.getColor("textInactiveText"));
		jButtonPreferences.setText("Preferences");
		jButtonPreferences.addActionListener(new Karaoke_jButtonPreferences_actionAdapter(this));
		jMenuItem1.setText("Preferences");
		jMenuItem1.addActionListener(new Karaoke_jMenuItem1_actionAdapter(this));
		jCheckBoxMenuItem1.setText("Antialiasing");
		jCheckBoxMenuItem1.addActionListener(new Karaoke_jCheckBoxMenuItem1_actionAdapter(this));
		jMenuItem3.setText("Sho/Hide toolbar");
		jMenuItem3.addActionListener(new Karaoke_jMenuItem3_actionAdapter(this));
		this.getContentPane().add(toolbar, BorderLayout.NORTH);
		toolbar.add(jLabel2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
		         GridBagConstraints.NONE, new Insets(2, 0, 0, 10), 0, 0));
		toolbar.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
		         GridBagConstraints.NONE, new Insets(2, 8, 0, 0), 0, 0));
		toolbar.add(tempoSlider, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
		         GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 0, 0),
		         258, -4));
		this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
		jPanel1.add(jButtonPreferences, null);
		jPopupMenu1.add(jMenuItem1);
		jPopupMenu1.add(jCheckBoxMenuItem1);
		jPopupMenu1.add(jMenuItem3);
	}

	void tempoSlider_adjustmentValueChanged(AdjustmentEvent e)
	{
		setTempo((float) e.getValue() / 5);
	}

	void jButtonPreferences_actionPerformed(ActionEvent e)
	{
		showPreferencesDialog();
	}

	private void showPreferencesDialog()
	{Lyrics.setCharsetHint(props.charsetHint);
	}

	public String getSongTitle()
	{
		return songTitle;
	}

	public String getArtist()
	{
		return artist;
	}

	void jMenuItem1_actionPerformed(ActionEvent e)
	{
		showPreferencesDialog();
	}

	void jCheckBoxMenuItem1_actionPerformed(ActionEvent e)
	{
		karaokePane.setAntialiasing(jCheckBoxMenuItem1.isSelected());
	}

	void jMenuItem3_actionPerformed(ActionEvent e)
	{
		toolbar.setVisible(!toolbar.isVisible());
	}
}

class Karaoke_tempoSlider_adjustmentAdapter implements java.awt.event.AdjustmentListener
{
	Karaoke adaptee;

	Karaoke_tempoSlider_adjustmentAdapter(Karaoke adaptee)
	{
		this.adaptee = adaptee;
	}

	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		adaptee.tempoSlider_adjustmentValueChanged(e);
	}
}

class Karaoke_jButtonPreferences_actionAdapter implements java.awt.event.ActionListener
{
	Karaoke adaptee;

	Karaoke_jButtonPreferences_actionAdapter(Karaoke adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.jButtonPreferences_actionPerformed(e);
	}
}

class Karaoke_jMenuItem1_actionAdapter implements java.awt.event.ActionListener
{
	Karaoke adaptee;

	Karaoke_jMenuItem1_actionAdapter(Karaoke adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.jMenuItem1_actionPerformed(e);
	}
}

class Karaoke_jCheckBoxMenuItem1_actionAdapter implements java.awt.event.ActionListener
{
	Karaoke adaptee;

	Karaoke_jCheckBoxMenuItem1_actionAdapter(Karaoke adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.jCheckBoxMenuItem1_actionPerformed(e);
	}
}

class Karaoke_jMenuItem3_actionAdapter implements java.awt.event.ActionListener
{
	Karaoke adaptee;

	Karaoke_jMenuItem3_actionAdapter(Karaoke adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.jMenuItem3_actionPerformed(e);
	}
}
