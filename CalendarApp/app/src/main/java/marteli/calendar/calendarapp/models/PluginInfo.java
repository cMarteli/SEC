/**
 * Plugin.java
 * Holds plugin id and arguments
 */

package marteli.calendar.calendarapp.models;

import java.util.Map;

public class PluginInfo {
    private String pluginID;
    private Map<String, String> args;

    public PluginInfo(String id, Map<String, String> con) {
        pluginID = id;
        args = con;
    }

    // Getters and setters
    public String getPluginID() {
        return pluginID;
    }

    public void setPluginID(String cn) {
        pluginID = cn;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> c) {
        args = c;
    }

    @Override
    public String toString() {
        return "Plugin{" +
                "ID='" + pluginID + '\'' +
                ", config=" + args +
                '}';
    }

}
