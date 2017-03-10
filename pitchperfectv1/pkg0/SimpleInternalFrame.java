/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;


import com.alee.laf.panel.WebPanel;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

//A JPanel subclass that has a drop shadow border and that provides a header with icon, title and tool bar.<p>
public class SimpleInternalFrame extends WebPanel {
    
    private JComponent    title;
    private GradientPanel gradientPanel;
    private JPanel        headerPanel;
    private boolean       selected;
    
    public SimpleInternalFrame(String title) {
        this(null, title, null, null);
    }
    
    public SimpleInternalFrame(JComponent title) {
        this(title, null, null);
    }
    
    public SimpleInternalFrame(Icon icon, String title) {
        this(icon, title, null, null);
    }

    public SimpleInternalFrame(String title, JToolBar bar, JComponent content) {
        this(null, title, bar, content);
    }
    
    public SimpleInternalFrame(Icon icon, String title, JToolBar bar, JComponent content) {
      this(new JLabel(title, icon, SwingConstants.LEADING), bar, content);
    }
    
    public SimpleInternalFrame(JComponent title, JToolBar bar, JComponent content) {
        super(new BorderLayout());
        this.title = title;
        this.selected = false;
        
        JPanel top = buildHeader(title, bar);
      	add(top, BorderLayout.NORTH);

        if (content != null) {
            setContent(content);
        }
        
        setBorder(new ShadowBorder());
        setSelected(true);
        updateHeader();
    }
    
    public JComponent getTitle() {
        return title;
    }
    
    public void setTitle(JComponent newTitle) {
        if (title != null) {
          gradientPanel.remove(title);
        }
        
        if (newTitle != null) {
          gradientPanel.add(newTitle, BorderLayout.CENTER);
        }
        
        this.title = newTitle;
        gradientPanel.validate();
        updateHeader();
    }
    
    public JToolBar getToolBar() {
        return headerPanel.getComponentCount() > 1
            ? (JToolBar) headerPanel.getComponent(1)
            : null;
    }
    
    public void setToolBar(JToolBar newToolBar) {
        JToolBar oldToolBar = getToolBar();
        if (oldToolBar == newToolBar) {
            return;
        }
        if (oldToolBar != null) {
            headerPanel.remove(oldToolBar);
        }
        if (newToolBar != null) {
            newToolBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            headerPanel.add(newToolBar, BorderLayout.EAST);
        }
        updateHeader();
        firePropertyChange("toolBar", oldToolBar, newToolBar);
    }
    
    public Component getContent() {
        return hasContent() ? getComponent(1) : null;
    }
    
    public void setContent(Component newContent) {
        Component oldContent = getContent();
        if (hasContent()) {
            remove(oldContent);
        }
        add(newContent, BorderLayout.CENTER);
        firePropertyChange("content", oldContent, newContent);
    }
    

    //Answers if the panel is currently selected (or in other words active) or not. In the selected state, the header background will be rendered differently.

    public boolean isSelected() {
        return selected;
    }
    
    
    //This panel draws its title bar differently if it is selected, which may be used to indicate to the user that this panel
     //has the focus, or should get more attention than other simple internal frames.
    
    public void setSelected(boolean newValue) {
        boolean oldValue = isSelected();
        selected = newValue;
        updateHeader();
        firePropertyChange("selected", oldValue, newValue);
    }
    

    // Building *************************************************************

    /**
     * Creates and answers the header panel, that consists of:
     * an icon, a title label, a tool bar, and a gradient background.
     * 
     * @param label   the label to paint the icon and text
     * @param bar     the panel's tool bar
     * @return the panel's built header area
     */
    private JPanel buildHeader(JComponent label, JToolBar bar) {
        gradientPanel = new GradientPanel(new BorderLayout(), getHeaderBackground());
        label.setOpaque(false);

        gradientPanel.add(label, BorderLayout.CENTER);
        gradientPanel.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 1));

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(gradientPanel, BorderLayout.CENTER);
        setToolBar(bar);
        headerPanel.setBorder(new BottomLineBorder());
        headerPanel.setOpaque(false);
        return headerPanel;
    }

    private void updateHeader() {
        gradientPanel.setBackground(getHeaderBackground());
        gradientPanel.setOpaque(isSelected());
        title.setForeground(getTextForeground(isSelected()));
        headerPanel.repaint();
    }
    
    public void updateUI() {
        super.updateUI();
        if (title != null) {
            updateHeader();
        }
    }


    // Helper Code **********************************************************
    //Checks and answers if the panel has a content component set.
    private boolean hasContent() {
        return getComponentCount() > 1;
    }
    
    //Determines and answers the header's text foreground color.
    public static Color getTextForeground(boolean isSelected) {
        Color c = UIManager.getColor(isSelected ? "SimpleInternalFrame.activeTitleForeground" : "SimpleInternalFrame.inactiveTitleForeground");
        if (c != null) {
            return c;
        }
        return UIManager.getColor(isSelected ? "InternalFrame.activeTitleForeground" : "Label.foreground");

    }

    //Determines and answers the header's background color.
    public static Color getHeaderBackground() {
        Color c = UIManager.getColor("SimpleInternalFrame.activeTitleBackground");
        return c != null ? c : UIManager.getColor("InternalFrame.activeTitleBackground");
    }


    // Helper Classes *******************************************************

    // A custom border for the raised header pseudo 3D effect.
    private static class BottomLineBorder extends AbstractBorder {

        /** serialization version identifier */
        private static final long serialVersionUID = 4096572266433568642L;

        private static final Insets INSETS = new Insets(0, 0, 1, 0);

        public Insets getBorderInsets(Component c) { return INSETS; }

        public void paintBorder(Component c, Graphics g,
            int x, int y, int w, int h) {
                
            g.setColor(UIManager.getColor("controlShadow"));
            g.fillRect(x, y+h-1, w, 1);
        }
    }    

    // A custom border that has a shadow on the right and lower sides.
    private static class ShadowBorder extends AbstractBorder {
        
        private static final Insets INSETS = new Insets(1, 1, 3, 3);

        public Insets getBorderInsets(Component c) { return INSETS; }

        public void paintBorder(Component c, Graphics g,
            int x, int y, int w, int h) {
                
            Color shadow        = UIManager.getColor("controlShadow");
            if (shadow == null) {
                shadow = Color.GRAY;
            }
            Color lightShadow   = new Color(shadow.getRed(), 
                                            shadow.getGreen(), 
                                            shadow.getBlue(), 
                                            170);
            Color lighterShadow = new Color(shadow.getRed(),
                                            shadow.getGreen(),
                                            shadow.getBlue(),
                                            70);
            g.translate(x, y);
            
            g.setColor(shadow);
            g.fillRect(0, 0, w - 3, 1);
            g.fillRect(0, 0, 1, h - 3);
            g.fillRect(w - 3, 1, 1, h - 3);
            g.fillRect(1, h - 3, w - 3, 1);
            // Shadow line 1
            g.setColor(lightShadow);
            g.fillRect(w - 3, 0, 1, 1);
            g.fillRect(0, h - 3, 1, 1);
            g.fillRect(w - 2, 1, 1, h - 3);
            g.fillRect(1, h - 2, w - 3, 1);
            // Shadow line2
            g.setColor(lighterShadow);
            g.fillRect(w - 2, 0, 1, 1);
            g.fillRect(0, h - 2, 1, 1);
            g.fillRect(w-2, h-2, 1, 1);
            g.fillRect(w - 1, 1, 1, h - 2);
            g.fillRect(1, h - 1, w - 2, 1);
            g.translate(-x, -y);
        }
    }

    // A panel with a horizontal gradient background.
    private static class GradientPanel extends JPanel {
        
        private GradientPanel(LayoutManager lm, Color background) {
            super(lm);
            setBackground(background);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!isOpaque()) {
                return;
            }
            Color control = UIManager.getColor("control");
            int width  = getWidth();
            int height = getHeight();

            Graphics2D g2 = (Graphics2D) g;
            Paint storedPaint = g2.getPaint();
            g2.setPaint(new GradientPaint(0, 0, getBackground(), width, 0, control));
            g2.fillRect(0, 0, width, height);
            g2.setPaint(storedPaint);
        }
    }

}

