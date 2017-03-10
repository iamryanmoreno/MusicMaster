/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;


import java.util.Collection;
import java.util.Properties;

public interface DataPanel {
    public void openPanel(DataPanelManager dataPanelManager);
    public boolean supportsMultipleChannels();
    public void closePanel();
    public Properties getProperties();
    public void setProperty(String key, String value);
}
