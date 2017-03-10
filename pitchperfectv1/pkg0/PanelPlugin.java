
package pitchperfectv1.pkg0;


import com.alee.laf.panel.WebPanel;

public abstract class PanelPlugin extends WebPanel implements PlayerPlugin {
    
    public Class getType() {
        return PanelPlugin.class;
    }
    
    public String getDescription() {
        return "no Description";
    }
    
    public boolean canProcess(Object o) {
        return true;
    }
    
    public boolean matches(Class type, String name, Object object) {
        return true;
    }
    
    public String getVersion() {
        return "1.0";
    }
}


