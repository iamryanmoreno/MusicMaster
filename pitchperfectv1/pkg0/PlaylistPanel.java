package pitchperfectv1.pkg0;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

import com.l2fprod.common.swing.JDirectoryChooser;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import pitchperfectv1.basicplayer.BasicPlayer;
import pitchperfectv1.basicplayer.MyCellRenderer;

public class PlaylistPanel extends PanelPlugin implements BasicPlayerListener, ActionListener,
         MouseListener, MouseMotionListener, DropTargetListener, KeyListener {
    private JTextField txtSearch;
    TextPrompt tpSearch;
    JPanel panelFilter;
    private FileDialog fileDialog;
    private JFileChooser fileChooser = null;
    private JDirectoryChooser dirChooser = new JDirectoryChooser();
    private JButton buttonAdd;
    private JButton buttonPlay;
    private JButton buttonStop;
    private JButton buttonPause;
    private JButton buttonNext;
    private JButton buttonPrev;
    private JCheckBox checkbox;
    private JLabel time;
    private QuickFindPanel quickFindPanel;
    private JPanel controlPanel;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem menuItem;
    private ToolTipManager toolTipMgr;
    private JSlider slider;
    private DefaultBoundedRangeModel model = null;
    private int modelscale = 100;
    private int byteslength = -1;
    private int milliseconds = -1;
    private static JList lstPlayList;
    private static DefaultListModel listModel;
    private JPopupMenu popup;
    private static BasicPlayer controller = null;
    //private static javazoom.jlgui.basicplayer.BasicPlayer controller = new javazoom.jlgui.basicplayer.BasicPlayer();
    private int oldRand = -1;
    private PopupListener popupListener;
    private static boolean shuffle = false;
    private static boolean playing = false;
    private static boolean paused = false;
    private int indexSelected = -1;
    private int currentSelection = -1;
    private String fontName = "Arial";
    private int fontSize = 10;
    private int fontStyle = 0;
    public String lastDirSelected, lastFileSelected;
    //private PlayListManager playListMgr = null;
    private boolean recursion = true;
    // For recursive exploration
    public static final int MAXDEPTH = 4;
    // Playlist
    private String currentPlaylistFilename;
    private MyCellRenderer myCellRenderer = new MyCellRenderer();
    JButton loadPlaylist;
    
    public PlaylistPanel() {
        super();
        initPanel();
    }
    
    private void initPanel() {
        Color fg1 = new Color(255, 255, 255);
        Color bg2 = new Color(0, 0, 0);
        Color fg2 = new Color(90, 130, 250);

		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		listModel = new DefaultListModel();
		lstPlayList = new JList(listModel);
		lstPlayList.setCellRenderer(myCellRenderer);
		lstPlayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstPlayList.setSelectedIndex(0);

		// Drag and drop
		lstPlayList.setDragEnabled(true);
		new DropTarget(lstPlayList, DnDConstants.ACTION_COPY_OR_MOVE, this, true);

		lstPlayList.setForeground(fg2);
		lstPlayList.setBackground(bg2);
		lstPlayList.setSelectionBackground(bg2);
		lstPlayList.setSelectionForeground(fg1);
		lstPlayList.addMouseListener(this);
		lstPlayList.addKeyListener(this);

		popupListener = new PopupListener();
		lstPlayList.addMouseListener(popupListener);
		lstPlayList.addMouseMotionListener(this);

		toolTipMgr = ToolTipManager.sharedInstance();
		toolTipMgr.registerComponent(lstPlayList);
		// System.out.println(tpm.isEnabled() + " " + tpm.getReshowDelay());
		toolTipMgr.setReshowDelay(100);
		controlPanel = new JPanel();
                
                buttonPrev = MyClasses.addButton(controlPanel, "Prev", "Prev",
		         "/Images/littlePrev.gif");
		buttonPrev.addActionListener(this);

		buttonPlay = MyClasses.addButton(controlPanel, "Play", "Play",
		         "/Images/littlePlay.gif");
		buttonPlay.addActionListener(this);

		buttonPause = MyClasses.addButton(controlPanel, "Pause", "Pause",
		         "/Images/littlePause.gif");
		buttonPause.addActionListener(this);

		buttonStop = MyClasses.addButton(controlPanel, "Stop", "Stop",
		         "/Images/littleStop.gif");
		buttonStop.addActionListener(this);

		buttonNext = MyClasses.addButton(controlPanel, "Next", "Next",
		         "/IMages/littleNext.gif");
		buttonNext.addActionListener(this);

		buttonAdd = MyClasses.addButton(controlPanel, "Add File", "Add File",
		         "/Images/littleEject.gif");
		buttonAdd.addActionListener(this);
                
                
                loadPlaylist = MyClasses.addButton(controlPanel, "load", "load", null);
                loadPlaylist.setMnemonic(KeyEvent.VK_P);
                loadPlaylist.addActionListener(this);
                

                
		popup = new JPopupMenu("FileInfo");
		JMenuItem mi = new JMenuItem("Play Item");
		mi.setActionCommand("playitem");
		mi.addActionListener(this);
		popup.add(mi);
		popup.addSeparator();

		mi = new JMenuItem("Crop Item");
		mi.setActionCommand("cropitem");
		mi.addActionListener(this);
		popup.add(mi);

		mi = new JMenuItem("Remove Item");
		mi.setActionCommand("removeitem");
		mi.addActionListener(this);
		popup.add(mi);

                
		popup.addSeparator();
		mi = new JMenuItem("File Info");
		mi.setActionCommand("fileinfo");
		mi.addActionListener(this);
		popup.add(mi);
                
                
                mi = new JMenuItem("Save PlayList");
                mi.setActionCommand("save");
		mi.addActionListener(this);
                popup.add(mi);
                
		setLayout(new BorderLayout());
                setMinimumSize(new Dimension(130, 27));
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                txtSearch = new JTextField();
                txtSearch.addKeyListener(this);
                tpSearch = new TextPrompt("Search Song Here", txtSearch);
                panelFilter = new JPanel();
                panelFilter.setLayout(new BorderLayout());
                panelFilter.add(txtSearch, BorderLayout.CENTER);
                mainPanel.add(panelFilter, BorderLayout.NORTH);
    
                
                SimpleInternalFrame playlist = new SimpleInternalFrame(MyClasses.getIcon("icons/channels.gif"),
                "Play list",null,
                mainPanel);        
                add(playlist, BorderLayout.CENTER);
                

		model = new DefaultBoundedRangeModel(0, 1, 0, modelscale);
		slider = new JSlider(model);
		slider.setPreferredSize(new Dimension(100, 15));
		slider.setEnabled(false);
		slider.addMouseListener(this);
		controlPanel.add(slider);

		time = new JLabel("00:00 / 00:00");
		controlPanel.add(time);
                
                // Create scroll pane
		JScrollPane listScrollPane = new JScrollPane(lstPlayList);
		listScrollPane.addKeyListener(this);
                
                // Create quickFindPanel
		quickFindPanel = new QuickFindPanel(this);
		quickFindPanel.addKeyListener(this);
                mainPanel.add(quickFindPanel, BorderLayout.NORTH);
                mainPanel.add(listScrollPane, BorderLayout.CENTER);
                mainPanel.add(controlPanel, BorderLayout.SOUTH);
	}
    
    public String getName() {
        return "PlayList";
    }
    
    public String getVersion() {
        return "v1.0";
    }
    public boolean isIncludeSubFolderForDragAndDrop() {
        return true;
    }
    
    public boolean isShowPlayerButtonsInStatusBar() {
        return true;
    }

    public static File getFileSelected() {
        int index = lstPlayList.getSelectedIndex();
        return (File) listModel.get(index);
    }
    
    public static void setSelectedFile() {
        lstPlayList.setSelectedIndex(0);
    }
    
    public static void setPlaying(boolean v) {
        playing = v;
    }
    
    public JPanel getStatusPanel() {
        return controlPanel;
    }
   
    public Class getType() {
        return PlaylistPanel.class;
    }

    public String getDescription() {
        return "no Description";
    }

    public boolean canProcess(Object o) {
        return true;
    }

    public boolean matches(Class type, String string, Object o) {
        return true;
    }
    
    public void setController(BasicController newController) {
        PlaylistPanel.controller = (BasicPlayer) newController;
    }
    public boolean isShuffle() {
        return shuffle;
    }
   
   public void setShuffle(boolean newValue) {
       shuffle = newValue;
   }

   
   public int getIndexShuffle() {
       int index = listModel.getSize();
       int val;
       Random rand = new Random();
       while ((val = rand.nextInt(index)) == oldRand) {}
       oldRand = val;
       return val;
   }
   
   public static void findNext(String searchText) {
       int listSize = listModel.size();
       int startIndex = lstPlayList.getSelectedIndex();
       // If nothing selected, just start at the beginning
       if (startIndex == -1)
           startIndex = 0;
       int currentIndex = startIndex;
       int count = 0;
       while (true) {
           // If we compared them all, we're done!
           if (count > listSize)
               break;
           // Its ok to loop back to the beginning of the list
           if (++currentIndex > listSize - 1)
               currentIndex = 0;
           if (((File) listModel.get(currentIndex)).getName().toUpperCase().indexOf(searchText.toUpperCase()) != -1) {
               lstPlayList.setSelectedIndex(currentIndex);
               lstPlayList.ensureIndexIsVisible(currentIndex);
               lstPlayList.repaint();
               break;
           }
           ++count;
       }
   }
   
   public static void findPrev(String searchText) {
       int listSize = listModel.size();
       int startIndex = lstPlayList.getSelectedIndex();
       // If nothing selected, just start at the beginning
       if (startIndex == -1)
           startIndex = 0;
       int currentIndex = startIndex;
       int count = 0;
       while (true) {
           // If we compared them all, we're done!
           if (count > listSize)
               break;
           // Its ok to loop back to the beginning of the list
           if (--currentIndex == -1)
               currentIndex = listSize - 1;
           if (((File) listModel.get(currentIndex)).getName().toUpperCase().indexOf(searchText.toUpperCase()) != -1) {
               lstPlayList.setSelectedIndex(currentIndex);
               lstPlayList.ensureIndexIsVisible(currentIndex);
               lstPlayList.repaint();
               break;
           }
           ++count;
       }
   }
   
   public static void getNext() {
       paused = false;
       int taille = listModel.size();
       int index = lstPlayList.getSelectedIndex();
       if (index + 1 < taille) {
           lstPlayList.setSelectedIndex(index + 1);
           lstPlayList.ensureIndexIsVisible(index + 1);
           if (playing) {
               try {
                   controller.open((File) listModel.elementAt(index + 1));
                   controller.play();
               } catch (BasicPlayerException el) {
                   el.printStackTrace();
               }
           }
       } else {
           lstPlayList.setSelectedIndex(0);
           lstPlayList.ensureIndexIsVisible(0);
           if (playing) {
               try {
                   controller.open((File) listModel.elementAt(0));
                   controller.play();
               } catch (BasicPlayerException el) {
                   el.printStackTrace();
               }
           }
       }
   }

   
   public static void getPrev() {
       paused = false;
       int taille = listModel.size();
       int index = lstPlayList.getSelectedIndex();
       if (index - 1 < 0) {
           lstPlayList.setSelectedIndex(taille - 1);
           lstPlayList.ensureIndexIsVisible(taille - 1);
           if (playing) {
               try {
                   controller.open((File) listModel.elementAt(taille - 1));
                   controller.play();
               } catch (BasicPlayerException el) {
                   el.printStackTrace();
               }
           }
       } else {
           lstPlayList.setSelectedIndex(index - 1);
           lstPlayList.ensureIndexIsVisible(index - 1);
           if (playing) {
               try {
                   controller.open((File) listModel.elementAt(index - 1));
                   controller.play();
               } catch (BasicPlayerException el) {
                   el.printStackTrace();
               }
           }
       }
   }
   
   public void savePlaylist() {
       FileDialog fd = new FileDialog(m, "Save playlist", FileDialog.SAVE);
       fd.setDirectory(lastDirSelected);
       fd.setFile(lastFileSelected);
       fd.setVisible(true);
       String dir = fd.getDirectory();
       String file = fd.getFile();
       lastDirSelected = (dir == null) ? lastDirSelected : dir;
       lastFileSelected = (file == null) ? lastFileSelected : file;
       
       try {
           BufferedWriter bw = new BufferedWriter(new FileWriter(dir + file));
           PrintWriter pw = new PrintWriter(bw);
           for (int x = 0; x < listModel.getSize(); x++) {
               pw.write(x + 1 + "|" + listModel.getElementAt(x));
               bw.newLine();
           }
           
           pw.close();
           bw.close();
       } catch (IOException ioex) {
           System.out.println("Unable to write playlist to disk");
       }
   }
   
   public void sortPlaylistByFilename() {
       Object[] list = listModel.toArray();
       Arrays.sort(list, new SortByFilenameComparator());
       listModel.removeAllElements();
       for (int i = 0; i < list.length; ++i)
           listModel.addElement(list[i]);
   }
   
   private class SortByFilenameComparator implements Comparator {
       public int compare(Object o1, Object o2) {
           return ((File) o1).getName().compareTo(((File) o2).getName());
       }
   }
   
   Main m;
   public void loadPlaylist() {
       if (fileDialog == null)
           fileDialog = new FileDialog(m, "Load playlist", FileDialog.LOAD);
       fileDialog.setDirectory(lastDirSelected);
       fileDialog.setFile(lastFileSelected);
       fileDialog.setVisible(true);
       String dir = fileDialog.getDirectory();
       String file = fileDialog.getFile();
       lastDirSelected = (dir == null) ? lastDirSelected : dir;
       lastFileSelected = (file == null) ? lastFileSelected : file;
       readPlaylist(dir + file);
   }
   
   
   private void readPlaylist(String filename) throws HeadlessException, NumberFormatException {
       String line = "";
       StringTokenizer st = null;
       String currentDir = null;
       String absolutePlaylistFilename = null;
       String absoluteSongFilename = null;
       boolean demo_playlist = false;
       // Default case is when the playlist filename is absolute
       absolutePlaylistFilename = filename;
       currentDir = System.getProperty("user.dir");
       
       if (filename.endsWith("demoPlaylist.pls")) {
           demo_playlist = true;
           if (filename.equals("demoPlaylist.pls")) {
               absolutePlaylistFilename = currentDir + File.separator + "demo" + File.separator + filename;
           }
       }
       
       File f = new File(absolutePlaylistFilename);
       
       if (!f.exists()) {
           System.out.println("file " + absolutePlaylistFilename + " does not exists");
       }
       
       if (f.exists()) {
           listModel.clear();
           try {
               BufferedReader r = new BufferedReader(new FileReader(f));
               // The song's filenames
               while ((line = r.readLine()) != null) {
                   if (demo_playlist) {
                       absoluteSongFilename = currentDir + File.separator + line;
                   } else {
                       st = new StringTokenizer(line, "|");
                       st.nextToken();
                       // We are after the first number
                       absoluteSongFilename = st.nextToken();
                   }
                   addElementToPlayList(absoluteSongFilename);
               }
               
               r.close();
           } catch (IOException ioex) {
               System.out.println("Unable to read in playlist");
           } catch (NumberFormatException nfex) {
               JOptionPane.showMessageDialog(this, "Invalid playlist format ");
           }
       }
   }

   public void parcoursRecursif(File f, boolean recu, int depth) {
       if (depth > MAXDEPTH) {
           System.out.println("Stopping recursion, MAXDEPTH = " + MAXDEPTH + " reached");
           return;
       }
       
       File tab[] = f.listFiles();
       String path;
       for (int i = 0; i < tab.length; i++) {
           path = tab[i].getAbsolutePath();
           if (tab[i].isFile() && controller.isFileSupported(path)) {
               listModel.addElement(tab[i]);
           } else if (tab[i].isDirectory()) {
               if (!(tab[i].getName().equals("System Volume Information")) && recu) {
                   parcoursRecursif(tab[i], recu, depth + 1);
               }
           }
       }
   }

   
   public boolean isSingleSongMode() {
       return true;
   }
   public static void addElementToPlayList(String name) {
       File f = new File(name);
       listModel.addElement(f);
           
   }
   
   public void actionPerformed(ActionEvent actionEvent) {
       String actionCommand = actionEvent.getActionCommand();
       if (actionEvent.getSource() == buttonAdd || actionCommand.equals("addfile")) {
           if (fileChooser == null)
               fileChooser = new JFileChooser();
           fileChooser.setMultiSelectionEnabled(true);
           fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
           fileChooser.setDialogTitle("Add a file");
           int returnVal = fileChooser.showOpenDialog(this);
           if (returnVal == JFileChooser.APPROVE_OPTION) {
               File[] files = fileChooser.getSelectedFiles();
               for (int i = 0; i < files.length; i++) {
                   if (controller.isFileSupported(files[i])) {
                       listModel.addElement(files[i]);
                   }
               }
           }
           if (listModel.size() == 1) {
               lstPlayList.setSelectedIndex(0);
           }
       }
       
       else {
           if (actionEvent.getSource() == buttonAdd || actionCommand.equals("adddir")) {
               checkbox = new JCheckBox("Include subdirs, ctrl = multiselection", recursion);
               checkbox.addActionListener(this);
               dirChooser.setAccessory(checkbox);
               dirChooser.setMultiSelectionEnabled(true);
               int choice = dirChooser.showOpenDialog(null);
               if (choice == JDirectoryChooser.APPROVE_OPTION) {
                   File[] selectedFiles = dirChooser.getSelectedFiles();
                   for (int i = 0, l = selectedFiles.length; i < l; i++) {
                       parcoursRecursif(selectedFiles[i], recursion, 0);
                   }
               }
           } else if (actionCommand.equals("remove")) {
               if (listModel.size() != 0 && !(lstPlayList.isSelectionEmpty())) {
                   int index = lstPlayList.getSelectedIndex();
                   listModel.remove(index);
               }
           } else if (actionEvent.getSource() == buttonPlay) {
               try {
                   if (listModel.size() != 0 && !(lstPlayList.isSelectionEmpty())) {
                       int index = lstPlayList.getSelectedIndex();
                       File fd = (File) listModel.elementAt(index);
                       
                       controller.open(fd);
                       controller.stop();
                       controller.play();
                       paused = false;
                       playing = true;
                       slider.setEnabled(true);
                       
                    
       Variables variables = new Variables();   
       DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
       DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
	   //get current date time with Date()
       Date date = new Date();
       System.out.println(dateFormat.format(date) + " " + dateFormat2.format(date));
       variables.setTitle(""+fd);
       variables.setDate(dateFormat.format(date));
       variables.setTime(dateFormat2.format(date));
       variables.Insert();
                   }
               } catch (BasicPlayerException el) {
                   el.printStackTrace();
               }
           } else if (actionEvent.getSource() == buttonPause) {
               try {
                   if (paused == true) {
                       controller.resume();
                       paused = false;
                   } else  {
                       controller.pause();
                       paused = true;
                   }
               } catch (BasicPlayerException el) {
                   el.printStackTrace();
               }
           } else if (actionEvent.getSource() == buttonStop) {
               try {
                   controller.stop();
                   model.setValue(0);
                   paused = false;
               } catch (BasicPlayerException el) {
                   el.printStackTrace();
               } 
               playing = false;
               slider.setValue(0);
               slider.setEnabled(false);
               time.setText("00:00 / " + millisec_to_time(milliseconds));
           } else if (actionCommand.equals("clear")) {
               listModel.removeAllElements();
           } else if (actionCommand.equals("load")) {
               loadPlaylist();
           } else if (actionCommand.equals("save")) {
               savePlaylist();
           } else if (actionCommand.equals("sortbyfilename")) {
               sortPlaylistByFilename();
           } else if (actionEvent.getSource() == buttonNext) {
               getNext();
           } else if (actionEvent.getSource() == buttonPrev) {
               getPrev();
           } else if (actionCommand.equals("playitem")) {
               try {
                   if (listModel.size() != 0 && !(lstPlayList.isSelectionEmpty())) {
                       int index = lstPlayList.getSelectedIndex();
                       controller.open((File) listModel.elementAt(index));
                       controller.play();
                   }
               } catch (BasicPlayerException el) {
                   el.printStackTrace();
               }
               playing = true;
               slider.setEnabled(true);
           } else if (actionCommand.equals("cropitem")) {
               if (listModel.size() != 0 && !(lstPlayList.isSelectionEmpty())) {
                   int index = lstPlayList.getSelectedIndex();
                   File f = (File) listModel.getElementAt(index);
                   listModel.removeAllElements();
                   listModel.addElement(f);
               }
           } else if (actionCommand.equals("removeitem")) {
               if (listModel.size() != 0 && !(lstPlayList.isSelectionEmpty())) {
                   int index = lstPlayList.getSelectedIndex();
                   listModel.remove(index);
               }
           } else if (actionCommand.equals("quickfind")) {
               quickFindPanel.setVisible(true);
           } else if (actionEvent.getSource() == checkbox) {
               recursion = checkbox.isSelected();
           }
       }
   }

   
   public String getToolTipText(MouseEvent e) {
       int index = lstPlayList.locationToIndex(e.getPoint());
       File f = (File) listModel.getElementAt(index);
       if (f != null)
           return f.getName();
       return null;
   }
   
   class PopupListener extends MouseAdapter {
       public void mousePressed(MouseEvent e) {
           maybeShowPopup(e);
       }
       
       public void mouseReleased(MouseEvent e) {
           maybeShowPopup(e);
       }
       
       public void maybeShowPopup(MouseEvent e) {
           if (e.isPopupTrigger()) {
               int index = lstPlayList.getSelectedIndex();
               if (index != -1) {
                   popup.show(e.getComponent(), e.getX(), e.getY());
                   popup.revalidate();
               }
           }
       }
   }
   
   public void mousePressed(MouseEvent e) {
       popupListener.maybeShowPopup(e);
       int index = lstPlayList.locationToIndex(e.getPoint());
       lstPlayList.setSelectedIndex(index);
       indexSelected = index;
   }
   
   public void mouseReleased(MouseEvent e) {
       if (e.getSource() == slider) {
           try {
               long skp = -1;
               synchronized (model) {
                   skp = (long) ((model.getValue() * 1.0f / modelscale * 1.0f) * byteslength);
               }
               controller.seek(skp);
           } catch (BasicPlayerException e1) {
               e1.printStackTrace();
           }
       }
   }
   
   public void mouseExited(MouseEvent e) {}
   
   public void mouseEntered(MouseEvent e) {}
   
   public void mouseClicked(MouseEvent e) {
       // If double click
       if (e.getClickCount() == 2) {
           int index = lstPlayList.locationToIndex(e.getPoint());
           if (index != -1) {
               try {
                   File fd = (File) listModel.elementAt(index);
                   controller.open(fd);
                   controller.play();
                   playing = true;
                   slider.setEnabled(true);
               } catch (BasicPlayerException el) {
                   el.printStackTrace();
               }
           }
       }
   }
   
   public String millisec_to_time(long time_ms) {
       int seconds = (int) Math.floor(time_ms / 1000);
       int minutes = (int) Math.floor(seconds / 60);
       int hours = (int) Math.floor(minutes / 60);
       minutes = minutes - hours * 60;
       seconds = seconds - minutes * 60 - hours * 3600;
       String strhours = "" + hours;
       String strminutes = "" + minutes;
       String strseconds = "" + seconds;
       
       if (strseconds.length() == 1) {
           strseconds = "0" + strseconds;
       }
       
       if (strminutes.length() == 1) {
           strminutes = "0" + strminutes;
       }
       
       if (strhours.length() == 1) {
           strhours = "0" + strhours;
       }
       return (/* strhours + ":" + */strminutes + ":" + strseconds);
   }

   public void opened(Object stream, Map properties) {
       if (properties.containsKey("audio.length.bytes")) {
           byteslength = ((Integer) properties.get("audio.length.bytes")).intValue();
       }
       
       if (properties.containsKey("duration")) {
           milliseconds = (int) (((Long) properties.get("duration")).longValue()) / 1000;
       }
   }
   
   public String getDuration() {
       return null;
   }
   
   public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
       // long elapsedMilliseconds = (long) (microseconds / 1000);
       float progress = bytesread * 1.0f / this.byteslength * 1.0f;
       long progressMilliseconds = (long) (progress * milliseconds);
       time.setText(millisec_to_time(progressMilliseconds) + " / " + millisec_to_time(milliseconds));
       model.setValue((int) (progress * modelscale));
       
       //if (isSingleSongMode())
           //return;
       if (Math.abs(progressMilliseconds - milliseconds) <= 1000) {
           System.out.println("We do play next song ! diff = "  + Math.abs(progressMilliseconds - milliseconds));
           System.out.println("_________*_________________________________________");
           getNext();
       }
   }
   public int getNextIndex(int index) {
       int taille = listModel.size();
       if (index < taille)
           return index + 1;
       return 0;
   }
   
   public void stateUpdated(BasicPlayerEvent event) {}
   
   public void focusGained(FocusEvent e) {}
   public void focusLost(FocusEvent e) {}
   
   public void mouseDragged(MouseEvent e) {
       int index = lstPlayList.locationToIndex(e.getPoint());
       if (currentSelection != index && indexSelected != index) {
           currentSelection = index;
           if (index > indexSelected) {
               File f = (File) listModel.getElementAt(index - 1);
               listModel.removeElementAt(index - 1);
               listModel.insertElementAt(f, index);
               lstPlayList.setSelectedIndex(index);
               indexSelected = index;
           } else {
               File f = (File) listModel.getElementAt(index + 1);
               listModel.removeElementAt(index + 1);
               listModel.insertElementAt(f, index);
               lstPlayList.setSelectedIndex(index);
               indexSelected = index;
           }
       }
   }
   
   public void mouseMoved(MouseEvent e) {}
   
   public BasicPlayerListener getPlugin() {
       return this;
   }
   public void dragEnter(DropTargetDragEvent e) {
       if (isDragOk(e) == false) {
           e.rejectDrag();
           return;
       }
   }
   
   public void dragOver(DropTargetDragEvent e) {
       if (isDragOk(e) == false) {
           e.rejectDrag();
           return;
       }
   }
   
   public void dragExit(DropTargetEvent e) {}
   
   public void dropActionChanged(DropTargetDragEvent e) {
       if (isDragOk(e) == false) {
           e.rejectDrag();
           return;
       }
   }
   
   public void drop(DropTargetDropEvent e) {
       // Verification DataFlavor
       DataFlavor[] dfs = e.getCurrentDataFlavors();
       DataFlavor tdf = null;
       
       for (int i = 0; i < dfs.length; i++) {
           if (DataFlavor.javaFileListFlavor.equals(dfs[i]) || DataFlavor.stringFlavor.equals(dfs[i])) {
               tdf = dfs[i];
               break;
           }
       }
       
       if (tdf != null) {
           if ((e.getSourceActions() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
               e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
           } else {
               return;
           }
           
           try {
               Transferable t = e.getTransferable();
               Object data = t.getTransferData(tdf);
               
               if (data instanceof java.util.List) {
                   java.util.List al = (java.util.List) data;
                   if (al.size() > 0) {
                       File file = null;
                       ListIterator li = al.listIterator();
                       while (li.hasNext()) {
                           file = (File) li.next();
                           if (file.isFile()) {
                               if (file != null && controller.isFileSupported(file)) {
                                   listModel.addElement(file);
                               }
                           } else if (file.isDirectory()) {
                               parcoursRecursif(file, isIncludeSubFolderForDragAndDrop(), 0);
                           }
                       }
                   }
               } else if (data instanceof java.lang.String) {
                   parseData((String) data);
               }
           } catch (IOException ioe) {
               e.dropComplete(false);
               return;
           } catch (UnsupportedFlavorException ufe) {
               e.dropComplete(false);
               return;
           } catch (Exception ex) {
               e.dropComplete(false);
               return;
           }
           e.dropComplete(true);
       }
   }
   
   public void parseData(String data) {
       StringTokenizer st = new StringTokenizer(data, "\n");
       while (st.hasMoreTokens()) {
           listModel.addElement(new File(st.nextToken()));
       }
   }
   
   private boolean isDragOk(DropTargetDragEvent e) {
       // verification DataFlavor
       DataFlavor[] dfs = e.getCurrentDataFlavors();
       DataFlavor tdf = null;
       for (int i = 0; i < dfs.length; i++) {
           if (DataFlavor.javaFileListFlavor.equals(dfs[i]) || DataFlavor.stringFlavor.equals(dfs[i])) {
               tdf = dfs[i];
               break;
           }
       }
       if (tdf != null) {
           if ((e.getSourceActions() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
               return true;
           }
           return false;
       }
       return false;
   }

        
        
        
    public void keyTyped(KeyEvent e) {
        
    }
    
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F && (e.getModifiers() & KeyEvent.CTRL_MASK) != 0)
            quickFindPanel.setVisible(true);
        else if (e.getKeyCode() == KeyEvent.VK_F3 && (e.getModifiers() & KeyEvent.SHIFT_MASK) != 0)
            quickFindPanel.findPrev();
        else if (e.getKeyCode() == KeyEvent.VK_F3)
            quickFindPanel.findNext();
    }
    
    public void keyReleased(KeyEvent e) {}
  
 
  
}

