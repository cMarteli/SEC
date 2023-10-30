package marteli.calendar.calendarapp.api;

import java.util.Map;

public interface Plugin {
    void start(CoreAPI api);

    void handleEvent(String eventName, Map<String, String> eventData);
}
