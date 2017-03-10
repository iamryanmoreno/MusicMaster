/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;

//A class to manage all the data panels.
public class DataPanelManager {
    private static DataPanelManager instance;
    
    //A reference to the data panel container for the data panels to add their ui component too.
    private DataPanelContainer dataPanelContainer;
    //A list of all the data panels.
    private List<DataPanel> dataPanels;
    
    
    public static DataPanelManager getInstance() {
        if (instance == null) {
            instance = new DataPanelManager();
        }
        return instance;
    }
    
    //The constructor for the data panel manager. This initializes the data panel container and the list of registered data panels.
    private DataPanelManager() {
		dataPanelContainer = new DataPanelContainer();
                dataPanels = new ArrayList<DataPanel>();
    }
    
    public DataPanelContainer getDataPanelContainer() {
        return dataPanelContainer;
    }
    
    public void closeDataPanel(DataPanel dataPanel) {
        dataPanel.closePanel();
        dataPanels.remove(dataPanel);
    }
     
    //Calls the closePanel method on each registered data panel.
    public void closeAllDataPanels() {
        DataPanel dataPanel;
        for (int i=dataPanels.size()-1; i>=0; i--) {
            dataPanel = (DataPanel)dataPanels.get(i);
            closeDataPanel(dataPanel);
        }
    }
    
    public List<DataPanel> getDataPanels() {
        return dataPanels;
    }
}