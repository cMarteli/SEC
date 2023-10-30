package marteli.calendar.calendarapp.api;

import java.util.Map;

// Interface for core API accessible by plugins and scripts
public interface CoreAPI {

    // For plugins: Access to key-value pair arguments
    Map<String, String> getArguments(String pluginID);

    // For plugins and scripts: Create a new calendar event
    void createEvent(String inDes, String inDate, Integer inDur);

    // For all-day events
    void createEvent(String inDes, String inDate);

    // For plugins and scripts: Register to receive notifications for events
    void registerForNotifications(NotificationHandler handler);

}
