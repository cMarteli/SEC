// Event Class
public class CalendarEvent {
    private String name;
    private String date; // in YYYY-MM-DD format
    private String startTime; // in HH:MM format
    private int duration; // in minutes

    // Getters and setters omitted for brevity
}

// Calendar API Interface
public interface CalendarAPI {

    // Add a new event to the calendar
    void addEvent(CalendarEvent event);

    // Interface for listener to get notified when an event starts
    interface EventStartListener {
        void onEventStart(CalendarEvent event);
    }

    // Register a listener for a particular named event
    void registerListener(String eventName, EventStartListener listener);

}
