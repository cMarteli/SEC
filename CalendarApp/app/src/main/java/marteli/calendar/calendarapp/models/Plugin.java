package marteli.calendar.calendarapp.models;

/**
 * Plugin.java
 * Holds plugin details
 */

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

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "Plugin{" +
                "className='" + pluginID + '\'' +
                ", config=" + config +
                '}';
    }

}
