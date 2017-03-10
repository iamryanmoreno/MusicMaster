
package pitchperfectv1.pkg0;

import java.awt.*;
import javax.swing.*;

public final class Factory {

    // the margin used in toolbar buttons
    private static final Insets TOOLBAR_BUTTON_MARGIN = new Insets(1, 1, 1, 1);

    //Creates and answers a JScrollPane that has an empty border.
    public static JScrollPane createStrippedScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    //Creates and returns a <code>JSplitPane</code> that has empty borders.
    //Useful to avoid duplicate decorations, for example if the split pane
    //is contained by other components that already provide a border.
     
    public static JSplitPane createStrippedSplitPane(int orientation,
            Component comp1, Component comp2, double resizeWeight) {
        JSplitPane split = UIFSplitPane.createStrippedSplitPane(orientation, comp1, comp2);
        split.setResizeWeight(resizeWeight);
        return split;
    }
    
    //Creates and answers an AbstractButton configured for use in a JToolBar
    public static AbstractButton createToolBarButton(Action action) {
        JButton button = new JButton(action);
        button.setFocusPainted(false);
        button.setMargin(TOOLBAR_BUTTON_MARGIN);
        //button.setHorizontalTextPosition(SwingConstants.CENTER);
        //button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setText("");
        return button;
    }


}
