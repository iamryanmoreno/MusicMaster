/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;


import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.util.*;
import javax.swing.*;



// A container to hold the UI components for the data panels. They may add and remove UI components as needed.

public class DataPanelContainer extends WebPanel implements DragGestureListener, DragSourceListener {
    
    private static final long serialVersionUID = -2496258563984574021L;
    
    //A list of docked UI components.
    ArrayList<Component> dataPanels;
	
    //Display the data panels horizontally.
    public static int HORIZONTAL_LAYOUT = 0;
	
    //Display the data panels vertically.
    public static int VERTICAL_LAYOUT = 1;
	
    //The current layout.
    private int layout;
  
    //The layout manager.
    private GridLayout gridLayout;
  
    //The drag gesture recognizers for the components.
    private HashMap<Component,DragGestureRecognizer> dragGestures;
  
    //The position of components that were in this container.
    private HashMap<JComponent, Integer> previousPositions;
	
    //Create the container and set the default layout to horizontal.
    //SoundDetector soundDetector;
    
    public DataPanelContainer() {
        super();
        setBorder(null);
        gridLayout = new GridLayout(1, 1, 8, 8);
        setLayout(gridLayout);
        initLogo();
        dataPanels = new ArrayList<Component>();
        dragGestures =  new HashMap<Component,DragGestureRecognizer>();
        layout = HORIZONTAL_LAYOUT;
        previousPositions = new HashMap<JComponent, Integer>();
    }
    
    //Add the logo as the initial background.
    
    private void initLogo() {
    WebLabel backgroundImage = new WebLabel(MyClasses.getIcon("Images/pitchperfect1.png"));
    backgroundImage.setPreferredSize(new Dimension(1,1));
    backgroundImage.setMinimumSize(new Dimension(1,1));
    add(backgroundImage);
    }
    
    //Add a data panel UI component to this container.
    public void addDataPanel(JComponent component) {
        Integer position = previousPositions.get(component);
        if (position == null || position < 0 || position > dataPanels.size()) {
            dataPanels.add(component);
        } else {
            dataPanels.add(position, component);
        }
        
        DragSource dragSource = DragSource.getDefaultDragSource();
        DragGestureRecognizer dragGesture = dragSource.createDefaultDragGestureRecognizer(component, DnDConstants.ACTION_MOVE, this);
        dragGestures.put(component, dragGesture);
        layoutDataPanels();
        
        System.out.println("Added data panel to container (total = " + dataPanels.size() + ").");
    }

	//Remove the data panel UI component from this container.
    
    public void removeDataPanel(JComponent component) {
        DragGestureRecognizer dragGesture = (DragGestureRecognizer)dragGestures.remove(component);
        dragGesture.setComponent(null);
        previousPositions.put(component, new Integer(dataPanels.indexOf(component)));
        dataPanels.remove(component);
        layoutDataPanels();
        System.out.println("Removed data panel container (total = " + dataPanels.size() + ").");
    }
	
    
    //Set the layout for the data panels.
    public void setLayout(int layout) {
        if (this.layout != layout) {
            this.layout = layout;
            layoutDataPanels();
        }
    }
    
    //Layout the data panel acording the layout setting and in the order in which they were added to the container.
    private void layoutDataPanels() {
        int numberOfDataPanels = dataPanels.size();
        if (numberOfDataPanels > 0) {
            int gridDimension = (int)Math.ceil(Math.sqrt(numberOfDataPanels));
            int rows = gridDimension;
            int columns;
            if (numberOfDataPanels > Math.pow(gridDimension, 2)*(gridDimension-1)/gridDimension) {
                columns = gridDimension;
            } else {
                columns = gridDimension-1;
            }
            
            if (layout == HORIZONTAL_LAYOUT) {
                gridLayout.setRows(columns);
                gridLayout.setColumns(rows);
            } else {
                gridLayout.setRows(rows);
                gridLayout.setColumns(columns);
            }
        }
        
        removeAll();
        JComponent component;
        for (int i=0; i<numberOfDataPanels; i++) {
            component = (JComponent)dataPanels.get(i);
            add(component);
        }
        
        validate();
        repaint();
    }
    
    private void moveBefore(Component moveComponent, Component beforeComponent) {
        int beforeComponentIndex = getComponentIndex(beforeComponent);
        if (beforeComponentIndex != -1) {
            dataPanels.remove(moveComponent);
            dataPanels.add(beforeComponentIndex, moveComponent);
            layoutDataPanels();
        }
    }
    
    private int getComponentIndex(Component c) {
        for (int i=0; i<dataPanels.size(); i++) {
            Component component = (Component)dataPanels.get(i);
            if (c == component) {
                return i;
            }
        }  
        return -1;
    }
    
    public void dragGestureRecognized(DragGestureEvent e) {
        e.startDrag(DragSource.DefaultMoveDrop, new StringSelection(""), this);
    }
    
    public void dragEnter(DragSourceDragEvent e) {}
    
    public void dragOver(DragSourceDragEvent e) {
        Point dragPoint = e.getLocation();
        Point containerLocation = getLocationOnScreen();
        dragPoint.translate(-containerLocation.x, -containerLocation.y);

        Component overComponent = getComponentAt(dragPoint);
        Component dragComponent = e.getDragSourceContext().getComponent();
        
        if (overComponent != null && overComponent != dragComponent) {
            moveBefore(dragComponent, overComponent);
        }
    }

  public void dropActionChanged(DragSourceDragEvent dsde) {}

  public void dragExit(DragSourceEvent dse) {}

  public void dragDropEnd(DragSourceDropEvent dsde) {}
}

