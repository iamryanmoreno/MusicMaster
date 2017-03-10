
package pitchperfectv1.pkg0;

import com.alee.laf.button.WebButton;
import com.alee.laf.menu.WebCheckBoxMenuItem;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.text.WebTextField;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.*;



public class MyClasses extends AbstractAction{
    boolean selected = false;
    private static final Map<String, ImageIcon> iconCache = new ConcurrentHashMap<String, ImageIcon>();;

    public WebMenuItem setJMenuItem(WebMenuItem mnuitem, String sCaption, String imgLocation){		
		mnuitem.setText(sCaption);
		mnuitem.setIcon(new ImageIcon(imgLocation));
		mnuitem.setCursor(new Cursor(Cursor.HAND_CURSOR));
		mnuitem.setFont(new Font("Dialog", Font.PLAIN, 12));  
		mnuitem.setForeground(new Color(0,0,0));
                mnuitem.addActionListener(this);
		return 	mnuitem;							
    }//Create a MenuItem
    
    public WebMenuItem myJMenuItem(WebMenuItem menuItem, String title, ImageIcon img, String accelerator, char mnemonic){
        menuItem.setText(title);
        menuItem.setIcon(img);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(accelerator));
        menuItem.setMnemonic(mnemonic);
        menuItem.addActionListener(this);
        return menuItem;
    }
    public WebMenuItem myJMenuItem(WebMenuItem menuItem, String title, ImageIcon img, String accelerator){
        menuItem.setText(title);
        menuItem.setIcon(img);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(accelerator));
        menuItem.addActionListener(this);
        return menuItem;
    }
    
    public WebCheckBoxMenuItem myJCheckBoxItem (WebCheckBoxMenuItem checkbox, String title, boolean b, ImageIcon img, String accelerator){
        checkbox.setText(title);
        checkbox.setIcon(img);
        checkbox.setSelected(b);
        checkbox.setAccelerator(KeyStroke.getKeyStroke(accelerator));
        //checkbox.setMnemonic(mnemonic);
        checkbox.addActionListener(this);
        return checkbox;
        
    }
    public JButton myJButton(WebButton button, String title, ImageIcon img){
        button.setActionCommand(title);
        button.setIcon(img);
        button.addActionListener(this);
        return button;
    }
    
    public void myNumberValidator(WebTextField txtField){
	txtField.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) ||(c == KeyEvent.VK_BACK_SPACE) ||(c == KeyEvent.VK_DELETE))) {
                e.consume();
                }
            }
        });
    }
    
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        if (this.selected == selected) {
            return;
        }
        this.selected = selected;
        firePropertyChange("selected", null, Boolean.valueOf(selected));
    }
    public static ImageIcon getIcon(String iconFileName) {
        
        if (iconFileName == null) {
            return null;
        }
        
        // see if the icon is in the cache
        ImageIcon icon = iconCache.get(iconFileName);
        if (icon != null) {
            return icon;
        }
        
        URL iconURL = Thread.currentThread().getContextClassLoader().getResource(iconFileName);
        if (iconURL != null) {
            icon = new ImageIcon(iconURL);
            // cache the icon for future requests
            iconCache.put(iconFileName, icon);
        }
        return icon;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String comStr = e.getActionCommand();
        System.out.println("[" + comStr + "] is  selected.");
    }
    
    
    public static JButton addButton(JComponent p, String name, String tooltiptext, String imageName) {
        JButton b;
        if ((imageName == null) || (imageName.equals(""))) {
            b = (JButton) p.add(new JButton(name));
        } else {
            URL u = p.getClass().getResource(imageName);
            if (u != null) {
                ImageIcon im = new ImageIcon(u);
                b = (JButton) p.add(new JButton(im));
            } else {
                b = (JButton) p.add(new JButton(name));
            }
        }
        
        b.setToolTipText(tooltiptext);
	Insets insets = new Insets(0, 0, 0, 0);
	b.setMargin(insets);
        return b;
    }
    
    
    public static Image getImage(String imageFileName) {
        ImageIcon icon = getIcon(imageFileName);
        if (icon != null) {
            return icon.getImage();
        } else {
            return null;
        }
    }
}

