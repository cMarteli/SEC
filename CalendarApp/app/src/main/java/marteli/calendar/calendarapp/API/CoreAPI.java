package marteli.calendar.calendarapp.API;

import java.util.Map;
import java.time.LocalDateTime;

// Interface for core API accessible by plugins and scripts
public interface CoreAPI {

    // For plugins: Access to key-value pair arguments
    public abstract Map<String, String> getArguments(String pluginID);

    // For plugins and scripts: Create a new calendar event
    public abstract void createEvent(LocalDateTime inDate, Integer inDur, String inDes, boolean inAll);

    // For plugins and scripts: Register to receive notifications for events
    public abstract void registerForNotifications(NotificationHandler handler);
}
