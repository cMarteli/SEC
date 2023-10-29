package marteli.calendar.calendarapp.api;

import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import marteli.calendar.calendarapp.CalendarApp;
import marteli.calendar.calendarapp.CalendarData;
import marteli.calendar.calendarapp.models.Event;

public class PluginApiImpl implements CoreAPI {

    /* Logger from CalendarApp.java */
    private final static Logger LOGR = Logger.getLogger(CalendarApp.class.getName());

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
    }

    // // Notify all registered NotificationHandler objects
    // private void notifyHandlers(Event event) {
    // /* Get offset in seconds */
    // ZoneOffset offset =
    // ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset() / 1000);
    // Date startDate = Date.from(event.getDateTime().toInstant(offset));
    // Date endDate =
    // Date.from(event.getDateTime().plusMinutes(event.getDuration()).toInstant(offset));
    // for (NotificationHandler handler : notificationHandlers) {
    // handler.handleEvent(event.getDescription(), startDate, endDate);
    // }
    // }

    @Override
    public void registerForNotifications(NotificationHandler handler) {
        notificationHandlers.add(handler);
    }

    @Override
    public Map<String, String> getArguments(String pluginID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArguments'");
    }

    @Override
    public String test() {
        return calendar.toString();
    }

}