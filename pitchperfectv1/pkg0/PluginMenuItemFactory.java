/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;

import fr.unice.plugin.Plugin;
import fr.unice.plugin.PluginManager;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.JMenu;


public class PluginMenuItemFactory {
    
    private JMenu menu;
    private PluginManager loader;
    private ActionListener listener;
    
    public PluginMenuItemFactory(JMenu menu, PluginManager loader, ActionListener listener) {
        this.menu = menu;
        this.loader = loader;
        this.listener = listener;
    }
    
    public void buildMenu(Class type) {
        if (loader == null) {
            return;
        }
        
        menu.removeAll();
        
        Plugin[] instancesPlugins = loader.getPluginInstances(type);
        
        PluginMenuItem item;
        
        for (int i = 0; i < instancesPlugins.length; i++) {
            Plugin plugin = instancesPlugins[i];
            String mi = plugin.getName();
            item = new PluginMenuItem(mi);
            item.setPlugin(instancesPlugins[i]);
            menu.add(item);
            item.addActionListener(listener);
        }
    }
    
    public JMenu getMenu() {
        return menu;
    }
}
