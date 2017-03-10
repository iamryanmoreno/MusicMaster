/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import pitchperfectv1.basicplayer.BasicPlayer;

import pitchperfectv1.karaoke.*;

public class LyricsPanel extends PanelPlugin implements BasicPlayerListener, ActionListener{
  
    
  private long tempsMp3;
  //private static BasicPlayer controllerMp3 = null;
  
  private BasicPlayer controllerMp3;
  private Timer timer;
  private int current_row;
  private CdgDataChunk[] cdgDataChunksArray;
  private Color[] colormap = new Color[16];
  private Rectangle damagedRectangle;
  private CdgGraphicBufferedImage panelLyrics = new CdgGraphicBufferedImage();
  private boolean windowedMode = true;
  private boolean cdgFileLoaded = false;
  private boolean pausedPlay = false;
  private boolean seeking = false;
  private int nbCdgInstructions = 10;
  
  private JPopupMenu jPopupMenu1 = new JPopupMenu();
  private JMenuItem jMenuItemHelp = new JMenuItem();
  private JMenuItem jMenuItemFullScreensOptions = new JMenuItem();
  private JMenuItem jMenuItemLyricsSyncOptions = new JMenuItem();
  private ChooseFullScreenModeDIalog chooseFullScreenDialog;
  private JMenuItem jMenuItemFullScreenWindow = new JMenuItem();
  // Size multiples
  private JMenuItem jMenuItemOneX = new JMenuItem();
  private JMenuItem jMenuItemTwoX = new JMenuItem();
  private JMenuItem jMenuItemThreeX = new JMenuItem();

  private boolean hardwareAcceleratedFullScreenModeSupported = false;
  private JCheckBoxMenuItem jCheckBoxMenuItemUseHardwareFullScreenMode = new JCheckBoxMenuItem();

  private GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
  private GraphicsDevice myDevice = env.getDefaultScreenDevice();
  private DisplayMode oldDisplayMode = myDevice.getDisplayMode();

  private static DisplayMode[] BEST_DISPLAY_MODES = new DisplayMode[]
	{ new DisplayMode(640, 480, 32, 0), new DisplayMode(640, 480, 16, 0),
	         new DisplayMode(640, 480, 8, 0) };

  
	// For full screen mode
	private DisplayMode userDisplayMode = null;

	private int oldWindowDecorationStyle;
	
        private int defaultWidth = 369;

	private int defaultHeight = 299;

	// Construct the frame
	private int oldPosX;

	private int oldPosY;

	private int oldWidth;

	private int oldHeight;

  public LyricsPanel() {
      
    initPanel();
  }
  
  private void initPanel() {
    setBorder(null);
    setLayout(new BorderLayout());
    setMinimumSize(new Dimension(150, 50)); //130,27
    setPreferredSize(new Dimension(500, 500));
    
    SimpleInternalFrame infoViewFrame = new SimpleInternalFrame(
        MyClasses.getIcon(null),
        "Lyrics",null,
        panelLyrics);
    jMenuItemOneX.setText("1x");
    jMenuItemOneX.addActionListener(this);
    jMenuItemTwoX.setText("2x");
    jMenuItemTwoX.addActionListener(this);
    jMenuItemThreeX.setText("3x");
    jMenuItemThreeX.addActionListener(this);
    jCheckBoxMenuItemUseHardwareFullScreenMode.setText("Use HW Full Screen Mode (win+linux only)");
    jCheckBoxMenuItemUseHardwareFullScreenMode.addActionListener(this);
    jMenuItemFullScreensOptions.setText("Choose Fullscreen Resolution");
    jMenuItemFullScreensOptions.addActionListener(this);
    
    jMenuItemFullScreenWindow.setText("Fullscreen/Window");
    jMenuItemFullScreenWindow.addActionListener(this);
    
    jMenuItemHelp.setText("Help");
    jPopupMenu1.addSeparator();
    jPopupMenu1.add(jMenuItemOneX);
    jPopupMenu1.add(jMenuItemTwoX);
    jPopupMenu1.add(jMenuItemThreeX);
    jPopupMenu1.addSeparator();
    jPopupMenu1.add(jCheckBoxMenuItemUseHardwareFullScreenMode);
    jPopupMenu1.add(jMenuItemFullScreensOptions);
    jPopupMenu1.add(jMenuItemFullScreenWindow);
    jPopupMenu1.addSeparator();
    jPopupMenu1.add(jMenuItemHelp);
    add(infoViewFrame, BorderLayout.CENTER);
  }
  
  
	private void jbInit() throws Exception
	{

		jMenuItemOneX.setText("1x");
		jMenuItemOneX.addActionListener(this);
		jMenuItemTwoX.setText("2x");
		jMenuItemTwoX.addActionListener(this);
		jMenuItemThreeX.setText("3x");
		jMenuItemThreeX.addActionListener(this);

		jCheckBoxMenuItemUseHardwareFullScreenMode
		         .setText("Use HW Full Screen Mode (win+linux only)");
		jCheckBoxMenuItemUseHardwareFullScreenMode.addActionListener(this);

		jMenuItemFullScreensOptions.setText("Choose Fullscreen Resolution");
		jMenuItemFullScreensOptions.addActionListener(this);

		jMenuItemFullScreenWindow.setText("Fullscreen/Window");
		jMenuItemFullScreenWindow.addActionListener(this);

		jMenuItemHelp.setText("Help");

		jPopupMenu1.addSeparator();
		jPopupMenu1.add(jMenuItemOneX);
		jPopupMenu1.add(jMenuItemTwoX);
		jPopupMenu1.add(jMenuItemThreeX);
		jPopupMenu1.addSeparator();
		jPopupMenu1.add(jCheckBoxMenuItemUseHardwareFullScreenMode);
		jPopupMenu1.add(jMenuItemFullScreensOptions);
		jPopupMenu1.add(jMenuItemFullScreenWindow);
		jPopupMenu1.addSeparator();
		jPopupMenu1.add(jMenuItemHelp);
	}


	

	public String getName()
	{
		return "Lyrics";
	}

	public String getVersion()
	{
		return "v1.0";
	}

	/**
	 * Stop CDG only
	 */
	public void stopCdgOnly()
	{
		if (timer != null)
		{
			//*setVisible(false);
                    panelLyrics.pixelsChanged();/**/
			timer.stop();
			cdgFileLoaded = false;
		}
	}

	public void setNbCdgInstructions(int nbCdgInstructions)
	{
		this.nbCdgInstructions = nbCdgInstructions;
	}

	/**
	 * Loads a cdg file whose basename is taken from the mp3File parameter
	 * 
	 * @param mp3File :
	 *           the name of the mp3File, its basename will be used for getting the cdg file
	 *           associated
	 */
	public void loadCdgFile(File mp3File)
	{
		try
		{
			int length = mp3File.getAbsolutePath().length();
			String cdgFileName = mp3File.getAbsolutePath().substring(0, length - 4) + ".cdg";
			File f = new File(cdgFileName);
			if (f.exists())
			{
				System.out.println("Opening cdg file : " + cdgFileName);
				CdgFileObject cdg = new CdgFileObject(cdgFileName);
				cdgDataChunksArray = cdg.getCdgDataChunksArray();
				cdgFileLoaded = true;
				setVisible(true);
			}
			else
			{
				// No cdg file associated
				cdgFileLoaded = false;
				//*setVisible(false);
                                panelLyrics.pixelsChanged();/**/
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			cdgFileLoaded = false;
			//*setVisible(false);
                        panelLyrics.pixelsChanged();/**/
		}
	}

	private void startTimedPlay() throws NumberFormatException
	{

		if (timer != null)
			timer.stop();

		current_row = 0;

		final int delay = (int) (nbCdgInstructions * 3.33);

		System.out.println("---We launch timer with delay = " + (nbCdgInstructions * 3.33) + "---");
		// each cdg instruction lasts 0.00333333 s
		timer = new Timer(delay, new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{

				if (!pausedPlay)
				{
					for (int i = 0; i < nbCdgInstructions; i++)
					{
						playCdgInstruction();
						current_row++;
					}

					// After we played the buffer of instructions, do the
					// "asservissement"
					if (tempsMp3 != 0)
					{
						if ((current_row * 3.33) > (tempsMp3 + 100))
						{
							// we check if previously we were speeding up...
							if (timer.getDelay() < delay)
							{
								// we were speeding up, let's go back to the
								// "normal" value
								timer.setDelay(delay);
								// jLabel1.setText("standard delay\t\t " +
								// (int)(current_row * 3.33) + "\t>\t" +
								// tempsMp3 + "\t\tdelay : " +
								// timer.getDelay());
							}
							else
							{
								// let's continue slowing down
								// System.out.println("ralentit " + (current_row
								// * 3.33) + " > " + tempsMp3);
								timer.setDelay(timer.getDelay() + 100);
								// jLabel1.setText("slowing down\t\t " +
								// (int)(current_row * 3.33) + "\t>\t" +
								// tempsMp3 + "\t\tdelay : " +
								// timer.getDelay());
							}

						}
						else if ((current_row * 3.33) < (tempsMp3 - 100))
						{
							// we check if previously we were slowing down...
							if (timer.getDelay() > delay)
							{
								// we were slowing down, let's go back to the
								// "normal" value
								timer.setDelay(delay);
								// jLabel1.setText("standard delay\t\t " +
								// (int)(current_row * 3.33) + "\t>\t" +
								// tempsMp3 + "\t\tdelay : " +
								// timer.getDelay());
							}
							else
							{

								// System.out.println("acc�l�re " + (current_row
								// * 3.33) + " < " + tempsMp3);
								if (timer.getDelay() > 0)
								{
									timer.setDelay(timer.getDelay() - 1);
									// jLabel1.setText("speeding up\t\t " +
									// (int) (current_row * 3.33) +
									// "\t>\t" + tempsMp3 + "\t\tdelay : " +
									// timer.getDelay());
								}
							}
						}
					}
				}
			}
		});
		timer.start();
	}

	public void playCdgInstruction()
	{
		boolean ret = false;

		if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_LOAD_COL_TABLE_LOW)
		{
			// Allocate the colors 0-7 of the colormap
			CdgLoadColorTable.setColormap(cdgDataChunksArray[current_row].getCdgData(), 0, colormap);
			// panelColormap.setColormap(colormap);
			panelLyrics.setColormapLow(colormap);
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_LOAD_COL_TABLE_HIGH)
		{
			// Allocate the colors 8-15 of the colormap
			CdgLoadColorTable.setColormap(cdgDataChunksArray[current_row].getCdgData(), 8, colormap);
			// panelColormap.setColormap(colormap);
			panelLyrics.setColormapHigh(colormap);
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_TILE_NORMAL)
		{
			damagedRectangle = CdgTileBlock.drawTile(cdgDataChunksArray[current_row].getCdgData(),
			         panelLyrics.getPixels(), false);
			panelLyrics.pixelsChanged(damagedRectangle);
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_TILE_XOR)
		{
			damagedRectangle = CdgTileBlock.drawTile(cdgDataChunksArray[current_row].getCdgData(),
			         panelLyrics.getPixels(), true);
			panelLyrics.pixelsChanged(damagedRectangle);
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_MEMORY_PRESET)
		{
			if (CdgMemoryPreset.clearScreen(cdgDataChunksArray[current_row].getCdgData(), panelLyrics
			         .getPixels()))
			{
				// if previous instructions returned false, the screen has
				// already
				// been cleared in a previous call...
				panelLyrics.pixelsChanged();
			}
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_BORDER_PRESET)
		{
			CdgBorderPreset.drawBorder(cdgDataChunksArray[current_row].getCdgData(), panelLyrics
			         .getPixels());

			panelLyrics.pixelsChanged();
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_SCROLL_COPY)
		{
			panelLyrics.savePixels();
			ret = CdgScrollCopy.scroll(cdgDataChunksArray[current_row].getCdgData(), panelLyrics
			         .getPixels());

			panelLyrics.pixelsChanged();
			if (!ret)
				panelLyrics.restorePixels();
		}
		else if (cdgDataChunksArray[current_row].getCdgInstruction() == CdgDataChunk.CDG_SCROLL_PRESET)
		{
			panelLyrics.savePixels();
			ret = CdgScrollPreset.scroll(cdgDataChunksArray[current_row].getCdgData(), panelLyrics
			         .getPixels());

			panelLyrics.pixelsChanged();
			if (!ret)
				panelLyrics.restorePixels();
		}
	}

	/**
	 * Choose best disply mode. For full screen support.
	 */
	public static void chooseBestDisplayMode(GraphicsDevice device)
	{
		DisplayMode best = getBestDisplayMode(device);
		if (best != null)
		{
			device.setDisplayMode(best);
		}
	}

	/**
	 * Get best disply mode
	 * 
	 * @param device
	 * @return DisplayMode
	 */
	private static DisplayMode getBestDisplayMode(GraphicsDevice device)
	{
		for (int x = 0; x < BEST_DISPLAY_MODES.length; x++)
		{
			DisplayMode[] modes = device.getDisplayModes();
			for (int i = 0; i < modes.length; i++)
			{
				if (modes[i].getWidth() == BEST_DISPLAY_MODES[x].getWidth()
				         && modes[i].getHeight() == BEST_DISPLAY_MODES[x].getHeight()
				         && modes[i].getBitDepth() == BEST_DISPLAY_MODES[x].getBitDepth())
				{
					return BEST_DISPLAY_MODES[x];
				}
			}
		}
		return null;
	}

	/**
	 * Switch back and forth between full screen and window mode
	 */
	private void switchFullScreenWindowedMode()
	{
		if (windowedMode)
		{
			System.out.println("Go TO FULL SCREEN");
			// Go to full screen
			try
			{
				// go fullscreen
				goToFullScreen();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				goToWindowedMode();
			}
		}
		else
		{
			goToWindowedMode();
		}
	}

	public void displayDecorations(boolean flag)
	{
		//dispose();
		// must be displayable (disposed) to call this...
		setUndecorated(!flag);
		//pack();
	}

	/**
	 * Switch to full screen mode
	 */
	private void goToFullScreen()
	{
		if (!hardwareAcceleratedFullScreenModeSupported)
			return;

		// Store old pos and size
		oldPosX = getX();
		oldPosY = getY();
		oldWidth = getWidth();
		oldHeight = getHeight();

		if (windowedMode)
		{
			// No borders, close buttons etc...
			// displayDecorations(false);
			oldWindowDecorationStyle = getRootPane().getWindowDecorationStyle();

			getRootPane().setWindowDecorationStyle(JRootPane.NONE);

			//myDevice.setFullScreenWindow(BasicPlayerWindow.this);
			if (myDevice.isDisplayChangeSupported())
			{
				panelLyrics.setWindowedMode(false);
				if (userDisplayMode == null)
				{
					chooseBestDisplayMode(myDevice);
				}
				else
				{
					myDevice.setDisplayMode(userDisplayMode);
				}
			}
			//controllerMp3.getPlayerUI().minimize();
			// we can't modify full screen options while in full screen
			windowedMode = false;
		}
	}

	/**
	 * Switch to windowed mode
	 */
	private void goToWindowedMode()
	{
		if (!hardwareAcceleratedFullScreenModeSupported)
			return;

		if (!windowedMode)
		{
			// Go back to the old display mode, windowed, with decorations...
			myDevice.setDisplayMode(oldDisplayMode);
			myDevice.setFullScreenWindow(null);
			panelLyrics.setWindowedMode(true);

			// restore decorations
			getRootPane().setWindowDecorationStyle(oldWindowDecorationStyle);

			restorePositionAndSize();
			setVisible(true);
			// !!!

			// de-iconify the main interface of the player
			//controllerMp3.getPlayerUI().setToOriginalSize();

			// setUndecorated(true);
			// we can only modify full screen options while in window mode
			windowedMode = true;
		}
	}

	private void restorePositionAndSize()
	{
		// no hardware acceleration mode, just restore the window to its
		// previous pos and size
		setLocation(oldPosX, oldPosY);
		setSize(oldWidth, oldHeight);
	}

	private class MyMouseListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			// Double click switches between "full screen" and "windowed" modes.
			if (e.getClickCount() == 2)
				switchFullScreenWindowedMode();
		}
	}

	private class MyKeyListener extends KeyAdapter
	{
		/**
		 * Key pressed
		 */
		public void keyPressed(KeyEvent e)
		{
			System.out.println("Key Event: " + e.getKeyCode());

			// If escape key is pressed, go to windowed mode!
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				goToWindowedMode();
		}
	}

	private class PopupListener extends MouseAdapter
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

	// ----------------------------------------------
	// M�thods required by the pluglin implementation
	// ----------------------------------------------
	/**
	 * A handle to the BasicPlayer, plugins may control the player through the controller (play,
	 * stop, etc...)
	 * 
	 * @param controller :
	 *           a handle to the player
	 */
	public void setController(BasicController controller)
	{
		this.controllerMp3 = (BasicPlayer) controller;
	}

	/**
	 * Open callback, stream is ready to play. properties map includes audio format dependant
	 * features such as bitrate, duration, frequency, channels, number of frames, vbr flag, ...
	 * 
	 * @param stream
	 *           could be File, URL or InputStream
	 * @param properties
	 *           audio stream properties.
	 */
	public void opened(Object stream, Map properties)
	{
		if (!seeking)
		{
			String audiotype = null;

			if (stream != null)
			{
				if (stream instanceof File)
				{
					System.out.println("File : " + ((File) stream).getAbsolutePath());
					System.out.println("------------------");
					System.out.println("Trying to Load cdg file...");
					System.out.println("------------------");
					loadCdgFile((File) stream);
					if (!cdgFileLoaded)
					{
						System.out.println("No Cdg file associated !");
						return;
					}

					setVisible(true);
				}
				else if (stream instanceof URL)
				{
					System.out.println("URL : " + ((URL) stream).toString());
				}
			}
			Iterator it = properties.keySet().iterator();
			StringBuffer jsSB = new StringBuffer();
			StringBuffer extjsSB = new StringBuffer();
			StringBuffer spiSB = new StringBuffer();
			if (properties.containsKey("audio.type"))
			{
				audiotype = ((String) properties.get("audio.type")).toLowerCase();
			}

			while (it.hasNext())
			{
				String key = (String) it.next();
				Object value = properties.get(key);
				if (key.startsWith("audio"))
				{
					jsSB.append(key + "=" + value + "\n");
				}
				else if (key.startsWith(audiotype))
				{
					spiSB.append(key + "=" + value + "\n");
				}
				else
				{
					extjsSB.append(key + "=" + value + "\n");
				}
			}
			System.out.println(jsSB.toString());
			System.out.println(extjsSB.toString());
			System.out.println(spiSB.toString());
		}
	}

	/**
	 * Progress callback while playing. This method is called severals time per seconds while
	 * playing. properties map includes audio format features such as instant bitrate, microseconds
	 * position, current frame number, ...
	 * 
	 * @param bytesread
	 *           from encoded stream.
	 * @param microseconds
	 *           elapsed (<b>reseted after a seek !</b>).
	 * @param pcmdata
	 *           PCM samples.
	 * @param properties
	 *           audio stream parameters.
	 */
	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties)
	{
		if (cdgFileLoaded)
		{
			tempsMp3 = microseconds / 1000;
		}
		if (seeking)
		{
			// tempsMp3 = microseconds / 1000;
			String value = "" + properties.get("mp3.position.microseconds");
			System.out.println(("---microseconds--- = " + value));
			tempsMp3 = Long.parseLong(value);
			// System.exit(0);
			tempsMp3 /= 1000;
			System.out.println("tempsMp3 = " + tempsMp3);
			System.out.println("currentRow avant " + current_row);
			current_row = (int) ((tempsMp3 / 3.33) + 0.5);
			seeking = false;
			System.out.println("-----");
			System.out.println("Current row recalcul�");
			System.out.println("currentRow apr�s " + current_row);
			System.out.println("-----");

		}
	}

	/**
	 * Notification callback of javazoom.jlgui.player.test state.
	 * 
	 * @param event
	 */
	public void stateUpdated(BasicPlayerEvent event)
	{
		System.out.println("RECU BASICPLAYEREVBNT = " + event.getCode());
		if (cdgFileLoaded)
		{
			if (event.getCode() == BasicPlayerEvent.PLAYING)
			{
				System.out.println("RECU BASICPLAYEREVBNT = PLAYING");
				if (!seeking)
				{
					System.out.println("NOT SEEKING !");
					startTimedPlay();
				}
			}
			else
			{
				if (event.getCode() == BasicPlayerEvent.STOPPED)
				{
					System.out.println("RECU BASICPLAYEREVBNT STOPPED");
					if (!seeking)
					{
						System.out.println("stopCdgOnly()");
						stopCdgOnly();
					}
				}
				else
				{
					if (event.getCode() == BasicPlayerEvent.PAUSED)
					{
						System.out.println("RECU BASICPLAYEREVBNT PAUSED");
						pause();
					}
					else if (event.getCode() == BasicPlayerEvent.RESUMED)
					{
						System.out.println("RECU BASICPLAYEREVBNT RESUMED");
						resume();
					}
					else if (event.getCode() == BasicPlayerEvent.SEEKED)
					{
						System.out.println("RECU BASICPLAYEREVBNT SEEKED");
						seeking = true;
					}
					else if (event.getCode() == BasicPlayerEvent.SEEKING)
					{
						System.out.println("RECU BASICPLAYEREVBNT SEEKING");
						seeking = true;
					}
				}
			}

		}
	}

	// ----------------------------------------------
	// End of M�thods required by the pluglin implementation
	// ----------------------------------------------

	private void pause()
	{
		pausedPlay = true;
	}

	private void resume()
	{
		pausedPlay = false;
	}

	/**
	 * getPlugin
	 * 
	 * @return BasicPlayerListener
	 */
	public BasicPlayerListener getPlugin()
	{
		return this;
	}

	private void setFullScreenModeOption()
	{
		jCheckBoxMenuItemUseHardwareFullScreenMode
		         .setSelected(hardwareAcceleratedFullScreenModeSupported);
		jMenuItemFullScreensOptions.setEnabled(hardwareAcceleratedFullScreenModeSupported);
		jMenuItemFullScreenWindow.setEnabled(hardwareAcceleratedFullScreenModeSupported);
	}

	/**
	 * Popup actions
	 */
	public void actionPerformed(ActionEvent event)
	{
		
		// Set size 1x
		if (event.getSource().equals(jMenuItemOneX))
		{
			goToWindowedMode();
			setSize(defaultWidth, defaultHeight);
			validate();
		}

		// Set size 2x
		else if (event.getSource().equals(jMenuItemTwoX))
		{
			goToWindowedMode();
			setSize(defaultWidth * 2, defaultHeight * 2);
			validate();
		}

		// Set size 3x
		else if (event.getSource().equals(jMenuItemThreeX))
		{
			goToWindowedMode();
			setSize(defaultWidth * 3, defaultHeight * 3);
			validate();
		}

		else if (event.getSource().equals(jCheckBoxMenuItemUseHardwareFullScreenMode))
		{
			// Support for hardware full screen mode
			hardwareAcceleratedFullScreenModeSupported = !hardwareAcceleratedFullScreenModeSupported;
			setFullScreenModeOption();
		}

		// Full screen options
		else if (event.getSource().equals(jMenuItemFullScreensOptions))
		{
			goToWindowedMode();
			chooseFullScreenDialog.setVisible(true);
			userDisplayMode = chooseFullScreenDialog.getSelectedDisplayMode();
			panelLyrics.redrawFullImage();
		}

		// Switch back and forth between "Full Screen" and "Windowed" mode
		else if (event.getSource().equals(jMenuItemFullScreenWindow))
		{
			switchFullScreenWindowedMode();
		}
	}
}
  
  