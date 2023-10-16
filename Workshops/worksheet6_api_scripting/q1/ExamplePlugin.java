public class ExamplePlugin {

    public static void main(String[] args) {
        // Assuming calendarAPI is an instantiated object of a class that implements CalendarAPI
        CalendarAPI calendarAPI = getCalendarAPIInstance();

        // Create a new event
        CalendarEvent meeting = new CalendarEvent();
        meeting.setName("Team Meeting");
        meeting.setDate("2023-11-01");
        meeting.setStartTime("14:00");
        meeting.setDuration(60);

        // Add the event to the calendar
        calendarAPI.addEvent(meeting);

        // Register a listener for the "Team Meeting" event
        calendarAPI.registerListener("Team Meeting", new CalendarAPI.EventStartListener() {
            @Override
            public void onEventStart(CalendarEvent event) {
                System.out.println("The event " + event.getName() + " is starting now!");
            }
        });
    }

    // For demonstration purposes
    private static CalendarAPI getCalendarAPIInstance() {
        // Normally, you'd get an actual implementation of the CalendarAPI here.
        return null;
    }
}
