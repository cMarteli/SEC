/**
 * Plugin.java
 * Holds plugin details
 */

package marteli.calendar.calendarapp.models;

import java.util.Map;

public class Plugin {
    private String pluginID;
    private Map<String, String> config;

    public Plugin(String id, Map<String, String> con) {
        pluginID = id;
        config = con;
    }

    // Getters and setters
    public String getPluginID() {
        return pluginID;
    }

    public void setPluginID(String cn) {
        pluginID = cn;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> c) {
        config = c;
    }

    @Override
    public String toString() {
        return "Plugin{" +
                "className='" + pluginID + '\'' +
                ", config=" + config +
                '}';
    }

}
