/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;


import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class QuickFindPanel extends JPanel implements ActionListener, KeyListener {
    private JTextField quickFindField;
    private JButton quickFindCloseButton;
    private JButton quickFindNextButton;
    private JButton quickFindPrevButton;
    private TextPrompt tpSearch;
    private JButton loadPlaylist;
    private FileDialog fileDialog;
    public QuickFindPanel(PlaylistPanel playlistPanel) {
        super();
        // Build Quick Find Panel
        addKeyListener(playlistPanel);
        
        //quickFindCloseButton = MyClasses.addButton(this, "Close QuickFind", "Close QuickFind","/Images/littleStop.gif");
	//quickFindCloseButton.addActionListener(this);
	//quickFindCloseButton.addKeyListener(playlistPanel);
                
	//add(quickFindCloseButton);
	add(new JLabel("Find:"));
	quickFindField = new JTextField(20);
	quickFindField.addKeyListener(this);
	quickFindField.addKeyListener(playlistPanel);
        tpSearch = new TextPrompt("Search Song Here", quickFindField);
	add(quickFindField);

	quickFindPrevButton = MyClasses.addButton(this, "Find Prev", "Find Prev","/Images/littlePrev.gif");
	quickFindPrevButton.setMnemonic(KeyEvent.VK_P);
	quickFindPrevButton.addActionListener(this);
	quickFindPrevButton.addKeyListener(playlistPanel);
	add(quickFindPrevButton);

	quickFindNextButton = MyClasses.addButton(this, "Find Next", "Find Next","/Images/littleNext.gif");
	quickFindNextButton.setMnemonic(KeyEvent.VK_N);
	quickFindNextButton.addActionListener(this);
	quickFindNextButton.addKeyListener(playlistPanel);
	add(quickFindNextButton);
    }
    
    
    public void actionPerformed(ActionEvent e) {
        //if (e.getSource() == quickFindCloseButton) {
            //this.setVisible(false);
        //} else 
        if (e.getSource() == quickFindNextButton) {
            findNext();
        } else if (e.getSource() == quickFindPrevButton) {
            findPrev();
        } else if (e.getActionCommand() == "load"){
        }
    }
    
    public void keyPressed(KeyEvent e) {}
    
    public void keyReleased(KeyEvent e) {}
    
    public void keyTyped(KeyEvent e) {
        (new FindNextThread()).start();
    }
    
    @Override
    public void setVisible(boolean arg0) {
        super.setVisible(arg0);
        quickFindField.requestFocus();
        quickFindField.selectAll();
    }
    
    public void findNext() {
        // Search for the value of the textfield in the playlist
        String searchText = quickFindField.getText();
        System.out.println("Next searchText = " + searchText);
        if (searchText != null && searchText.equals("") == false)
            PlaylistPanel.findNext(searchText);
    }
    
    public void findPrev() {
        // Search for the value of the textfield in the playlist
        String searchText = quickFindField.getText();
        System.out.println("Prev searchText = " + searchText);
        if (searchText != null && searchText.equals("") == false)
            PlaylistPanel.findPrev(searchText);
    }
    
    private class FindNextThread extends Thread {
        public void run() {
            try {
                Thread.sleep(10);
                findNext();
            } catch (Exception ex) {
            }
        }
    }
}
