package marteli.calendar.calendarapp.apiclasses;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import marteli.calendar.calendarapp.api.CoreAPI;
import marteli.calendar.calendarapp.api.NotificationHandler;
import marteli.calendar.calendarapp.CalendarApp;
import marteli.calendar.calendarapp.CalendarData;
import marteli.calendar.calendarapp.models.Event;
import marteli.calendar.calendarapp.models.PluginInfo;

public class PluginApiImpl implements CoreAPI {

    /* Logger from CalendarApp.java */
    private final static Logger LOGR = Logger.getLogger(CalendarApp.class.getName());

    private Map<Event, Timer> eventTimers = new HashMap<>(); // To keep track of Timers for each event
    private List<NotificationHandler> notificationHandlers; // List of objects that will receive notifications
    private CalendarData calendar;

    public PluginApiImpl(CalendarData c) {
        calendar = c;
        notificationHandlers = new ArrayList<>();
    }

    @Override
    public void createEvent(String inDes, String inDate, Integer inDur) {
        // Get the default date format for the desired locale
        Event event;
        try {
            event = new Event(inDes, inDate, inDur); // DATE NEEDS TO INCLUDE A START TIME
        } catch (IllegalArgumentException | DateTimeParseException e) {
            if (LOGR.isLoggable(Level.FINE)) {
                LOGR.log(Level.FINE, "Error creating event: ", e);
            }
            return; // Don't add the event if it's invalid
        }
        calendar.addEvent(event);
        scheduleNotification(event);
    }

    @Override
    public void createEvent(String inDes, String inDate) {
        // Get the default date format for the desired locale
        Event event;
        try {
            event = new Event(inDes, inDate);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            if (LOGR.isLoggable(Level.FINE)) {
                LOGR.log(Level.FINE, "Error creating event: ", e);
            }
            return; // Don't add the event if it's invalid
        }
        calendar.addEvent(event);
        scheduleNotification(event);
    }

    /* Schedules the notification for when the event starts */
    private void scheduleNotification(Event event) {
        Timer timer = new Timer();
        long delay = calculateDelay(event.getDateTime());

        TimerTask notificationTask = new TimerTask() {
            @Override
            public void run() {
                notifyHandlers(event);
            }
        };

        timer.schedule(notificationTask, delay);
        eventTimers.put(event, timer); // Store the timer associated with the event.
    }

    private long calculateDelay(LocalDateTime eventStart) {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, eventStart).toMillis();
    }

    /**
     * Cancels all scheduled notifications and shuts down their corresponding
     * timers.
     */
    public void shutdownAllTimers() {
        for (Timer timer : eventTimers.values()) {
            timer.cancel(); // Cancel each timer, stopping any scheduled tasks from executing.
        }
        eventTimers.clear(); // Clear the map to allow garbage collection of Timer objects.
        if (LOGR.isLoggable(Level.INFO)) {
            LOGR.log(Level.INFO, "All timers cancelled and cleared.");
        }
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
        if (LOGR.isLoggable(Level.INFO)) {
            LOGR.log(Level.INFO, "Registered for notifications: " + handler.getClass().getName());
        }
    }

    @Override
    public Map<String, String> getArguments(String pluginID) throws IllegalArgumentException {
        for (PluginInfo info : calendar.getPlugins()) {
            if (info.getPluginID().equals(pluginID)) {
                return info.getArgs();
            }
        }
        throw new IllegalArgumentException("Plugin ID not found");
    }

}