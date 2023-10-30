/**
 * Repeat.java
 * Plugin to create repeating events
 */
package edu.curtin.calplugins;

import java.util.Map;
import java.time.LocalDateTime;

import marteli.calendar.calendarapp.api.*;

public class Repeat implements Plugin {
    private CoreAPI coreAPI;
    private static final String PLUGIN_ID = "edu.curtin.calplugins.Repeat";
    private LocalDateTime dateTime; // initial date and time of event
    private String eventTitle;
    private int duration; // Duration of the event
    private int repeatFrequency; // Frequency of repeating the event

    @Override
    public void start(CoreAPI api) {
        coreAPI = api;
        // System.out.println(PLUGIN_ID + " started"); //DEBUG
        initialiseEventData(coreAPI.getArguments(PLUGIN_ID));

        scheduleRepeatEventsForOneYear();
    }

    private void initialiseEventData(Map<String, String> args) {
        setDateTime(args);
        setTitle(args);
        setDuration(args);
        setRepeatFrequency(args);
    }

    private void setTitle(Map<String, String> args) {
        eventTitle = args.getOrDefault("title", "Default Title");
    }

    private void setDateTime(Map<String, String> args) {
        String startTimeStr = args.getOrDefault("startTime", "00:00:00"); // Default to midnight
        String startDateStr = args.get("startDate");
        dateTime = LocalDateTime.parse(startDateStr + "T" + startTimeStr);
    }

    /* sets the length of the event in minutes */
    private void setDuration(Map<String, String> args) {
        duration = Integer.parseInt(args.getOrDefault("duration", "1440")); // Default to 1440 minutes (24 hours)
    }

    /* sets the duration in days between repeats of the event */
    private void setRepeatFrequency(Map<String, String> args) {
        repeatFrequency = Integer.parseInt(args.getOrDefault("repeat", "1"));
    }

    /* Logic to perform repetition for a year */
    private void scheduleRepeatEventsForOneYear() {
        LocalDateTime endDate = dateTime.plusYears(1);
        LocalDateTime dateToSchedule = dateTime;

        // Repeat until a year after startDate
        while (dateToSchedule.isBefore(endDate)) {
            // If duration is 1440 (24hrs), create an all-day event
            if (duration == 1440) {
                coreAPI.createEvent(eventTitle, dateToSchedule.toString());
            } else {
                coreAPI.createEvent(eventTitle, dateToSchedule.toString(), duration);
            }
            dateToSchedule = dateToSchedule.plusDays(repeatFrequency);
        }
    }
}
