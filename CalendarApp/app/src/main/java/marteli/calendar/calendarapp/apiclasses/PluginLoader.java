package marteli.calendar.calendarapp.apiclasses;

import marteli.calendar.calendarapp.api.*;
import marteli.calendar.calendarapp.CalendarApp;
import marteli.calendar.calendarapp.CalendarData;
import marteli.calendar.calendarapp.models.PluginInfo;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginLoader {

    /* Logger */
    private final static Logger LOGR = Logger.getLogger(CalendarApp.class.getName());

    private Map<String, Plugin> plugins; // Map to hold pluginID to Plugin object mapping
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
                Plugin pluginInstance = (Plugin) pluginClass.getConstructor().newInstance();

                // Add to map
                plugins.put(plugin.getPluginID(), (Plugin) pluginInstance);

                // Add arguments
                pluginArgs.put(plugin.getPluginID(), plugin.getArgs());
                System.out.println("Trying to load plugin: " + plugin.toString() + " Class: "
                        + pluginInstance.getClass().getName());

            } catch (ReflectiveOperationException | ClassCastException e) {
                if (LOGR.isLoggable(Level.SEVERE)) {
                    LOGR.log(Level.SEVERE, "Error loading plugin: " + e.getClass().getName(), e);
                }
            }

            PluginApiImpl pluginApi = new PluginApiImpl(calendar);
            startPlugins(pluginApi);
        }
    }

    // TODO: Need to test
    private void startPlugins(CoreAPI api) {
        for (Plugin plugin : plugins.values()) {
            plugin.start(api);
        }
    }

    /* Getters */

    public Map<String, Plugin> getPlugins() {
        return plugins;
    }

    public Map<String, String> getArguments(String pluginID) {
        return pluginArgs.get(pluginID);
    }

}
