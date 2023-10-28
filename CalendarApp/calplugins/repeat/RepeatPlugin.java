package calplugins.repeat;

import java.util.Map;

import marteli.calendar.calendarapp.api.CoreAPI;

// Example plugin with ID "edu.curtin.calplugins.Repeat"
public class RepeatPlugin {
    private CoreAPI coreAPI;
    private String id = "RepeatPlugin"; // Plugin ID

    // Constructor with no arguments
    public RepeatPlugin() {
    }

    // Constructor accepting CoreAPI
    // public RepeatPlugin(CoreAPI coreAPI) {
    // this.coreAPI = coreAPI;

    // // Register for notifications
    // coreAPI.registerForNotifications(this);
    // }

    @Override
    public void handleEvent(String eventName, Map<String, String> eventData) {
        // Logic to handle the event and perform repetition

        // Fetch the plugin-specific arguments
        Map<String, String> args = coreAPI.getArguments(id);

        // For demonstration, let's assume there's a "repeatCount" argument
        String repeatCountStr = args.getOrDefault("repeatCount", "1");
        int repeatCount = Integer.parseInt(repeatCountStr);

        // Now create the same event 'repeatCount' times
        for (int i = 0; i < repeatCount; ++i) {
            String description = eventData.get("description");
            String date = eventData.get("date");
            String durationStr = eventData.get("duration");
            Integer duration = Integer.parseInt(durationStr);

            coreAPI.createEvent(description, date, duration);
        }
    }
}
