package marteli.calendar.calendarapp.api;

import marteli.calendar.calendarapp.models.CalendarData;
import marteli.calendar.calendarapp.models.Event;
import marteli.calendar.calendarapp.models.Plugin;

import java.time.ZoneOffset;
import java.util.*;

public class PluginLoader implements CoreAPI {

    private Map<String, Plugin> plugins; // Map to hold pluginID to Plugin object mapping
    private Map<String, Map<String, String>> pluginArgs; // Arguments for each plugin
    private List<NotificationHandler> notificationHandlers; // List of objects that will receive notifications
    private CalendarData calendar;

    public PluginLoader(CalendarData c) {
        calendar = c;
        plugins = new HashMap<>();
        pluginArgs = new HashMap<>();
    }

    public void loadPlugin(Plugin plugin) {
        System.out.println("Loading plugin: " + plugin.toString());
        try {
            // Get Class object
            Class<?> pluginClass = Class.forName(plugin.getPluginID());
            /* Assuming no-arg constructor */
            Plugin pluginInstance = (Plugin) pluginClass.getConstructor().newInstance();
            // Create a new instance
            // Object pluginInstance =
            // pluginClass.getDeclaredConstructor(CoreAPI.class).newInstance(this);

            // Add to map
            plugins.put(plugin.getPluginID(), (Plugin) pluginInstance);

            // Add arguments
            pluginArgs.put(plugin.getPluginID(), plugin.getConfig());

        } catch (ReflectiveOperationException | ClassCastException e) {
            System.out.println("Error loading plugin: " + plugin.getPluginID()); // TODO log
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> getArguments(String pluginID) {
        return pluginArgs.getOrDefault(pluginID, new HashMap<>());
    }

    @Override
    public void createEvent(String inDes, String inDate, Integer inDur) {
        // Get the default date format for the desired locale
        Event event;
        try {
            event = new Event(inDes, inDate, inDur); // DATE NEEDS TO INCLUDE A START TIME
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return; // Don't add the event if it's invalid
        }
        calendar.addEvent(event);
    }

    @Override
    public void createEvent(String inDes, String inDate) {
        // Get the default date format for the desired locale
        Event event;
        try {
            event = new Event(inDes, inDate);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return; // Don't add the event if it's invalid
        }
        calendar.addEvent(event);
    }

    // Notify all registered NotificationHandler objects
    private void notifyHandlers(Event event) {
        /* Get offset in seconds */
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset() / 1000);
        Date startDate = Date.from(event.getDateTime().toInstant(offset));
        Date endDate = Date.from(event.getDateTime().plusMinutes(event.getDuration()).toInstant(offset));
        for (NotificationHandler handler : notificationHandlers) {
            handler.handleEvent(event.getDescription(), startDate, endDate);
        }
    }

    @Override
    public void registerForNotifications(NotificationHandler handler) {
        notificationHandlers.add(handler);
    }

}
