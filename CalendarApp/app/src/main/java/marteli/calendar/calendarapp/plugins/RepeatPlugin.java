package marteli.calendar.calendarapp.plugins;

import java.util.Map;

import marteli.calendar.calendarapp.API.CoreAPI;

// Example plugin with ID "edu.curtin.calplugins.Repeat"
public class RepeatPlugin {
    private final CoreAPI api;

    public RepeatPlugin(CoreAPI api) {
        this.api = api;
    }

    public void init() {
        // Access own arguments
        Map<String, String> args = api.getArguments("edu.curtin.calplugins.Repeat");
        String title = args.get("title");
        String startDate = args.get("startDate");
        String startTime = args.getOrDefault("startTime", "00:00:00");
        String duration = args.getOrDefault("duration", "60");

        // Do something with these arguments, like creating events
        // ...
    }
}
