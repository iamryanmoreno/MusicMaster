/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;


import java.awt.Component;

import javax.swing.*;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;



// JSplitPane subclass that try to remove the divider border. - because this feature is not supported by look&amp feels

public final class UIFSplitPane extends JSplitPane {
    
    //serialization version identifier
    private static final long serialVersionUID = -7098718826113323984L;

    //Holds the name of the bound property that tries to show or hide the split pane's divider border.
    public static final String PROPERTYNAME_DIVIDER_BORDER_VISIBLE = "dividerBorderVisible";
    
    //Determines whether the divider border shall be removed when the UI is updated.
    private boolean dividerBorderVisible;

   
    // Instance Creation *****************************************************
    
    //Constructs a UIFSplitPane configured to arrange the child components side-by-side horizontally with no continuous layout, using two buttons for the components.
    public UIFSplitPane() {
        this(JSplitPane.HORIZONTAL_SPLIT, false,
                new JButton(UIManager.getString("SplitPane.leftButtonText")),
                new JButton(UIManager.getString("SplitPane.rightButtonText")));
    }


    //Constructs a UIFSplitPane configured with the specified orientation and no continuous layout.
    public UIFSplitPane(int newOrientation) {
        this(newOrientation, false);
    }
    
    public UIFSplitPane(int newOrientation, boolean newContinuousLayout) {
        this(newOrientation, newContinuousLayout, null, null);
    }


    //Constructs a UIFSplitPane with the specified orientation and the given componenents.
    
    public UIFSplitPane(int orientation, Component leftComponent, Component rightComponent) {
        this(orientation, false, leftComponent, rightComponent);
    }
    
    
    //Constructs a <code>UIFSplitPane</code> with the specified orientation, redrawing style, and given components.
    public UIFSplitPane(int orientation, boolean continuousLayout, Component leftComponent, Component rightComponent){
        super(orientation, continuousLayout, leftComponent, rightComponent);
        dividerBorderVisible = false;
    }
    
    
    //Constructs a UIFSplitPane, JSplitPane that has no borders.
    public static UIFSplitPane createStrippedSplitPane(int orientation, Component leftComponent, Component rightComponent) {
        UIFSplitPane split = new UIFSplitPane(orientation, leftComponent, rightComponent);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setOneTouchExpandable(false);
        return split;
    }
    
    
    // Accessing Properties **************************************************
    
    //Checks and answers whether the divider border shall be visible or invisible.
    public boolean isDividerBorderVisible() {
        return dividerBorderVisible;
    }
    
    
    //Makes the divider border visible or invisible.
    public void setDividerBorderVisible(boolean newVisibility) {
        boolean oldVisibility = isDividerBorderVisible();
        if (oldVisibility == newVisibility)
            return;
        dividerBorderVisible = newVisibility;
        firePropertyChange(PROPERTYNAME_DIVIDER_BORDER_VISIBLE, oldVisibility, newVisibility);
    }
    

    // Changing the Divider Border Visibility *********************************
    
    //Updates the UI and sets an empty divider border. The divider border may be restored by a L&F at UI installation time. And so, we try to reset it each time the UI is changed.
    public void updateUI() {
        super.updateUI();
        if (!isDividerBorderVisible())
            setEmptyDividerBorder();
    }
    

    //Sets an empty divider border if and only if the UI is an instance of BasicSplitPaneUI.
    private void setEmptyDividerBorder() {
        SplitPaneUI splitPaneUI = getUI();
        if (splitPaneUI instanceof BasicSplitPaneUI) {
            BasicSplitPaneUI basicUI = (BasicSplitPaneUI) splitPaneUI;
            basicUI.getDivider().setBorder(BorderFactory.createEmptyBorder());
        }
    }
    
    
}
