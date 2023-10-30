package edu.curtin.calplugins;

import java.util.Date;

import marteli.calendar.calendarapp.api.*;

// Example plugin with ID "edu.curtin.calplugins.Notify"
public class Notify implements Plugin, NotificationHandler {
    private CoreAPI coreAPI;
    private String text;
    private static final String PLUGIN_ID = "edu.curtin.calplugins.Notify";

    @Override
    public void start(CoreAPI api) {
        coreAPI = api;
        System.out.println("Plugin: " + PLUGIN_ID + " started"); // DEBUG
        text = coreAPI.getArguments(PLUGIN_ID).getOrDefault("text", "Default Text");
        coreAPI.registerForNotifications(this);
    }

    @Override
    public void handleEvent(String title, Date startDate, Date endDate) {
        // Check if the event description contains the specified text
        if (title.contains(text)) {
            // Output the complete event details to the user
            System.out.println("Event: " + title + ", Start: " + startDate + ", End: " + endDate);
        }
    }
}
