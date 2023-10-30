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
    private CalendarData calendar;
    private List<Plugin> pluginsLoaded = new ArrayList<>();

    public PluginLoader(CalendarData c) {
        calendar = c;
    }

    private void loadPlugins() {

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

                pluginsLoaded.add(pluginInstance); // add to list of loaded plugins

            } catch (ReflectiveOperationException | ClassCastException e) {
                if (LOGR.isLoggable(Level.SEVERE)) {
                    LOGR.log(Level.SEVERE, "Error loading plugin: " + e.getClass().getName(), e);
                }
            }
        }
    }

    /* Public method to start plugins */
    public void startPlugins() {
        loadPlugins();
        PluginApiImpl pluginApi = new PluginApiImpl(calendar);
        for (Plugin plugin : pluginsLoaded) {
            plugin.start(pluginApi);
        }
    }

}
