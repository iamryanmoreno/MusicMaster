/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pitchperfectv1.pkg0;

import fr.unice.plugin.Plugin;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;


public class PluginMenuItem extends JCheckBoxMenuItem {
	private Plugin plugin;
        public PluginMenuItem() {
            super();
	}
        
        public PluginMenuItem(String text) {
            super(text);
	}
        
        public PluginMenuItem(String text, Plugin plugin) {
            super(text);
            this.plugin = plugin;
	}
        
        public PluginMenuItem(String text, Icon icon) {
            super(text, icon);
	}
        
        public void setPlugin(Plugin plugin) {
            this.plugin = plugin;
	}
        
        public Plugin getPlugin() {
            return plugin;
	}
}

