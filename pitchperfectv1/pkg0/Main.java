
package pitchperfectv1.pkg0;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.menu.*;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.rootpane.*;
import fr.unice.plugin.PluginManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.awt.dnd.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.*;

import javazoom.jlgui.basicplayer.BasicPlayerListener;
import pitchperfectv1.basicplayer.*;

import com.alee.laf.panel.WebPanel;
public class Main extends WebFrame implements DropTargetListener, MouseMotionListener, ComponentListener{
    
    SplashScreen splashScreen = new SplashScreen();
    Thread threadSplash = new Thread(splashScreen);
    Container con;
    public Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    
    WebMenuBar menuBar;
    WebMenu mediaMenu, viewMenu, editMenu, helpMenu, toolsMenu, skinsMenu, pluginsMenu;
    WebMenuItem openItem, saveItem, exitItem, aboutItem, settingItem, listsItem, pitchItem;
    WebCheckBoxMenuItem cbShowStatus, cbShowButtonPanel, cbFullScreen, cbPiano, cbLyrics, cbPlayList;
    MyClasses settings;
    
    public JLabel throbber;
    public Icon throbberStop;
    public Icon throbberAnim;
   
    ImageIcon aboutImg = new ImageIcon(getClass().getResource("/Images/about.png"));
    ImageIcon saveImg = new ImageIcon(getClass().getResource("/Images/littleSave.gif"));
    ImageIcon exitImg = new ImageIcon(getClass().getResource("/Images/exit.png"));
    
    private PanelPlugin playlist;
    private ArrayList listComponent = new ArrayList();
    public BasicPlayer controller = null;
    public CompositePlayer bplayer = null;
    private WebMenu menuPlugins = null;
    private static boolean playlistActivate = false;
    
    public DataPanelManager dataPanelManager;
    private JFrame frame;
    public GridBagConstraints c;
    public PlaylistPanel playListPanel;
    public LyricsPanel lyricsPanel;
    public JSplitPane leftPanel;
    public JPanel rightPanel;
    public DataPanelContainer dataPanelContainer;
    public JSplitPane splitPane;
    public JLabel lblStatus = new JLabel("Default");
    SystemClock clock;
    WebPanel panelStatus;
    
    StatusPanel statusPanel;
    SoundDetector soundDetector;
    OscilloscopePanel oscilloscopePanel;
    JTabbedPane spectrumPane, soundPane;
    public Main() throws MalformedURLException{
        setTitle("Pitch Perfect v1.0");
        //setIconImage(new ImageIcon(getClass().getResource("/Images/pitchperfect_logo.png")).getImage());
        setJMenuBar(createJMenuBar());
        setUndecorated(true);
        bplayer = new CompositePlayer();
        controller = bplayer;
        
        
        loadSplashScreen();
        splashScreen.dispose();
        addMouseMotionListener(this);
        addComponentListener(this);

        
        //PlaylistPanel p = new PlaylistPanel();
       // p.setController(controller);
       // bplayer.addBasicPlayerListener(p.getPlugin());
        
        InfoPlugin listenerInfo =  new InfoPlugin();
        listenerInfo.setController(controller);
        bplayer.addBasicPlayerListener(listenerInfo.getPlugin());
        validate();
        Thread t = new Thread(listenerInfo);
        t.start();
        
        
        this.dataPanelManager = DataPanelManager.getInstance();
        initFrame();
        initPanelStatus();
        buildUI();
    }
    
    private void initPanelStatus(){
        statusPanel = new StatusPanel();
        add(statusPanel, BorderLayout.PAGE_END);
        Indicator.printMessage("Panel Status");
        statusPanel.setController(controller);
        bplayer.addBasicPlayerListener(statusPanel.getPlugin());
        
    }
    
    private void initFrame() {
                //frame = Application.getInstance(Main.class).getMainFrame();
                setLayout(new BorderLayout());
                c = new GridBagConstraints();
                
                
                initPlayListPanel();
                initLyricsPanel();
		initLeftPanel();
                initRightPanel();   
		initDataPanelContainer();
                //initPianoPanel();
                //pianoPanel.setVisible(false);
                initSplitPane();
    
	}
  	
  		
        
        private void initPlayListPanel() {
            playListPanel = new PlaylistPanel();
            playListPanel.setMinimumSize(new Dimension(0, 0));
            playListPanel.setPreferredSize(new Dimension(300,300));
            playListPanel.setController(controller);
            bplayer.addBasicPlayerListener(playListPanel.getPlugin());
            //add(playListPanel, BorderLayout.WEST);
            Indicator.printMessage("Play List Panel");
	}
        
        private void initLyricsPanel() {
            lyricsPanel = new LyricsPanel();
            lyricsPanel.setController(controller);
            bplayer.addBasicPlayerListener(lyricsPanel.getPlugin());
            //add(lyricsPanel, BorderLayout.CENTER);
            Indicator.printMessage("Lyrics Panel");
        }
        
        private void initLeftPanel() {
            leftPanel = Factory.createStrippedSplitPane(JSplitPane.VERTICAL_SPLIT, playListPanel, lyricsPanel, 0.65f);
            leftPanel.setContinuousLayout(true);
            leftPanel.setBorder(new EmptyBorder(8, 8, 8, 0));
            Indicator.printMessage("Left Panel");
        }
	
	private void initRightPanel() {
            rightPanel = new JPanel();
            rightPanel.setMinimumSize(new Dimension(0, 0));
            rightPanel.setLayout(new GridBagLayout());
	}
	
	private void initDataPanelContainer() {
            
            dataPanelContainer = dataPanelManager.getDataPanelContainer();
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.ipadx = 0;
            c.ipady = 0;
            c.insets = new java.awt.Insets(8,0,8,6);
            c.anchor = GridBagConstraints.NORTHWEST;
            spectrumPane = new JTabbedPane();
            soundDetector = new SoundDetector();
            oscilloscopePanel = new OscilloscopePanel();
            spectrumPane.addTab("Spectrum", null, oscilloscopePanel, "View Spectrum");
            spectrumPane.addTab("Sound Detector", null, soundDetector, "View Sound Detector"); 
            rightPanel.add(spectrumPane, c);
            Indicator.printMessage("Panel Container");
	}
        
        private void initPianoPanel() {
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0;
            c.weighty = 0;
            c.gridx = 0;
            c.gridy = 3;
            c.gridwidth = 2;
            c.gridheight = 1;
            c.ipadx = 0;
            c.ipady = 0;
            c.insets = new java.awt.Insets (0, 0, 8, 6);
            c.anchor = GridBagConstraints.SOUTHWEST;
            //rightPanel.add (jSplitPane1, c);
            Indicator.printMessage("Piano Panel");
            
        }
        
        private void initSplitPane() {
            splitPane = Factory.createStrippedSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPanel,rightPanel,0.2f);
            splitPane.setContinuousLayout(true);
            add(splitPane, BorderLayout.CENTER);
	}


    
    private void buildUI() {
        JPanel panelup = new JPanel();
        panelup.setLayout(new BorderLayout());
        InfoPlugin listenerInfo = new InfoPlugin();
        listenerInfo.setController(controller);
        bplayer.addBasicPlayerListener(listenerInfo.getPlugin());
        panelup.add(listenerInfo, BorderLayout.WEST);
        validate();
        Thread t = new Thread(listenerInfo);
        t.start();
        
        Box panelUI = Box.createHorizontalBox();
        StopPlaySeekPlugin listenerSPS = new StopPlaySeekPlugin();
        listenerSPS.setController(bplayer);
        bplayer.addBasicPlayerListener(listenerSPS.getPlugin());

            //PanGainPlugin listenerPG = (PanGainPlugin) searchPlugin(PanGainPlugin.NAME);
            //listenerPG.setController(controller);
            //bplayer.addBasicPlayerListener(listenerPG.getPlugin());
        panelUI.add(listenerSPS);
        //panelUI.add(listenerPG);
        panelup.add(panelUI, BorderLayout.CENTER);
        add(panelup, BorderLayout.NORTH);
	}
    
    
    
    
    
    protected void loadSplashScreen() {
      //Start the thread
      threadSplash.start();
      while(!splashScreen.isShowing()){
          try {       //Display the FormSplash for 10 seconds
              Thread.sleep(10000);
              System.out.println("Splash Screen has been loaded.");
          } catch(InterruptedException ex){
              ErrorHandler.display(ex.getMessage());
          }
      }
    }//Load Splash Screen
    
    
    protected WebMenuBar createJMenuBar(){
        menuBar = new WebMenuBar();
        mediaMenu = new WebMenu("Media");
        viewMenu = new WebMenu("View");
        editMenu = new WebMenu("Edit");
        helpMenu = new WebMenu("Help");
        toolsMenu = new WebMenu("Tools");
        skinsMenu = new WebMenu("Skins");
        
        JMenu menu[] = {mediaMenu, /*editMenu,*/viewMenu,helpMenu /*toolsMenu,*/ };
        for(int i = 0; i <= 2; i++){
            menuBar.add(menu[i]);
        }
        settings = new MyClasses();
        saveItem = new WebMenuItem();
        openItem = new WebMenuItem();
        exitItem = new WebMenuItem();
        settingItem = new WebMenuItem();
        aboutItem = new WebMenuItem();
        listsItem = new WebMenuItem();
        pitchItem = new WebMenuItem();
        
        cbShowStatus = new WebCheckBoxMenuItem("Show Panel Status");
        cbShowStatus.addActionListener(new AbstractAction());
        cbFullScreen = new WebCheckBoxMenuItem("Full Screen");
        cbLyrics = new WebCheckBoxMenuItem("Show Lyrics");
        cbLyrics.addActionListener(new AbstractAction());
        cbPiano = new WebCheckBoxMenuItem("Show Piano");
        cbPiano.addActionListener(new AbstractAction());
        cbPlayList = new WebCheckBoxMenuItem("Show Play List");
        cbPlayList.addActionListener(new AbstractAction());
        
        mediaMenu.add(settings.myJMenuItem(pitchItem, "Pitch Detector", null, null));
        mediaMenu.add(settings.myJMenuItem(openItem, "Open", null, "ctrl N", 'N'));
        mediaMenu.add(settings.myJMenuItem(saveItem, "Save", saveImg,"ctrl S", 'S'));
        mediaMenu.add(settings.myJMenuItem(exitItem, "Exit", exitImg,null));
        
        
        //viewMenu.add(settings.myJMenuItem(listsItem, "Lists", null, "ctrl L", 'L'));
        viewMenu.add(settings.myJCheckBoxItem(cbShowStatus, "Show Status",true, null, null));
        viewMenu.add(settings.myJCheckBoxItem(cbLyrics, "Lyrics", true, null, null));
        viewMenu.add(settings.myJCheckBoxItem(cbPlayList, "Show Playlist", true, null, null));
        //editMenu.add(settings.myJMenuItem(settingItem, "Settings", null, null));
        helpMenu.add(settings.myJMenuItem(aboutItem, "About", aboutImg, null));
        
        menuBar.add(Box.createHorizontalGlue());
        throbberStop = settings.getIcon("pitchperfectv1/Images/throbber.png");
        throbberAnim = settings.getIcon("pitchperfectv1/Images/throbber_anim.gif");
        throbber = new JLabel(throbberStop);
        throbber.setBorder(new EmptyBorder(0,0,0,4));
        
        
        actions();
        preparationToQuit();
        return menuBar;
    }
    public void actions(){
        WebMenuItem menuAll[] = {saveItem, openItem, exitItem, settingItem, aboutItem, listsItem, pitchItem};
        for (int i = 0; i <= 6; i++){
            menuAll[i].addActionListener(new AbstractAction());
            //menuAll[i].addActionListener(this);
        }
    }
    
    private void preparationToQuit() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                int out = WebOptionPane.showConfirmDialog(null, "Are you sure you want to exit the system?","Pitch Perfect v1.0",JOptionPane.YES_NO_OPTION);
                if(out == 0)
                    System.exit(0);
                System.out.println("The System is closing.");
            }
        });
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {}

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}

    
    
    public class AbstractAction implements ActionListener{
            
        @Override
            public void actionPerformed(ActionEvent e){
            
            if(e.getSource() == settingItem){
                
            } else if(e.getSource() == openItem){
            } else if(e.getSource() == exitItem){
                int out = WebOptionPane.showConfirmDialog(null, "Are you sure you want to exit the system?","Pitch Perfect v1.0",JOptionPane.YES_NO_OPTION);
                if(out == 0)
                    System.exit(0);
            } else if(e.getSource() == cbPlayList){
                if(cbPlayList.isSelected()){
                    playListPanel.setVisible(true);
                } else{
                    playListPanel.setVisible(false);
                }
            } else if (e.getSource() == aboutItem){
                WebOptionPane.showMessageDialog(null,"Developed by: Group 6");
            } else if (e.getSource() == cbFullScreen){
            } else if (e.getSource() == pitchItem){
                
            } else if (e.getSource() == cbShowStatus){
                if(cbShowStatus.isSelected()){
                    statusPanel.setVisible(true);
                } else{
                    statusPanel.setVisible(false);
                }
            } else if (e.getSource() == cbLyrics){
                if(cbLyrics.isSelected()){
                    lyricsPanel.setVisible(true);
                } else{
                    lyricsPanel.setVisible(false);
                }
            } else if (e.getSource() == cbPiano){
                if(cbPiano.isSelected()){
                } else{
                    //pianoPanel.setVisible(false);
                }
                
            }
         
        }
    }
    private void startThrobber() {
        throbber.setIcon(throbberAnim);
    }
    
    private void stopThrobber() {
        throbber.setIcon(throbberStop);
    }
    
    
    public static boolean isActivate() {
        return playlistActivate;
    }
    
    public void run(){
        setVisible(true);
        setLocation(0,0);
	setSize(screen);
    }
    public static void main(String[] args) throws MalformedURLException{
        try{
            // Setting up WebLookAndFeel style
            UIManager.setLookAndFeel (WebLookAndFeel.class.getCanonicalName());
            WebLookAndFeel.initializeManagers ();
        } catch ( Throwable e ) {
            ErrorHandler.display(e.getMessage());
        }
        
        Main m = new Main();
        m.run();
        
    }
}
