package marteli.calendar.calendarapp.api;

import marteli.calendar.calendarapp.CalendarData;
import marteli.calendar.calendarapp.models.PluginInfo;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginLoader {

    /* Logger */
    private final static Logger LOGR = Logger.getLogger(PluginLoader.class.getName());

    private Map<String, PluginInfo> plugins; // Map to hold pluginID to Plugin object mapping
    private Map<String, Map<String, String>> pluginArgs; // Map to hold pluginID to arguments mapping
    private CalendarData calendar;

    public PluginLoader(CalendarData c) {
        calendar = c;
        plugins = new HashMap<>();
        pluginArgs = new HashMap<>();
    }

    public void loadPlugins() {

        /* Initialise all plugins */
        for (PluginInfo plugin : calendar.getPlugins()) {
            if (LOGR.isLoggable(Level.INFO)) {
                LOGR.log(Level.INFO, "Trying to load plugin: " + plugin.toString());
            }
            try {
                // Get Class object
                Class<?> pluginClass = Class.forName(plugin.getPluginID());
                /* Imposing a no-arg constructor for the class exists */
                PluginInterface pluginInstance = (PluginInterface) pluginClass.getConstructor().newInstance();

                // Add to map
                plugins.put(plugin.getPluginID(), (PluginInfo) pluginInstance);

                // Add arguments
                pluginArgs.put(plugin.getPluginID(), plugin.getConfig());

            } catch (ReflectiveOperationException | ClassCastException e) {
                // TODO: LOG THIS
                System.out.println("Error loading plugin: " + e.getClass().getName() + "\nMessage: " + e.getMessage());
                if (LOGR.isLoggable(Level.SEVERE)) {
                    LOGR.log(Level.SEVERE, "Error loading plugin: " + plugin.getPluginID(), e);
                }
            }

            PluginApiImpl pluginApi = new PluginApiImpl(calendar);
            startPlugins(pluginApi);
        }
    }

    // TODO: Need to test
    private void startPlugins(CoreAPI api) {
        for (PluginInfo plugin : plugins.values()) {
            plugin.start(api);
        }
    }

    /* Getters */

    public Map<String, PluginInfo> getPlugins() {
        return plugins;
    }

    public Map<String, String> getArguments(String pluginID) {
        return pluginArgs.get(pluginID);
    }

}
