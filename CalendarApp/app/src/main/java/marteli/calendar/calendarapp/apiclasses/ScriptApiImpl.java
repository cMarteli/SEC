package marteli.calendar.calendarapp.apiclasses;

import marteli.calendar.calendarapp.api.*;
import marteli.calendar.calendarapp.CalendarApp;
import marteli.calendar.calendarapp.CalendarData;
import marteli.calendar.calendarapp.models.Event;
import marteli.calendar.calendarapp.models.Script;

import java.lang.reflect.Method;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.python.core.PyException;
import org.python.util.PythonInterpreter;

public class ScriptApiImpl implements CoreAPI {

    /* Logger from CalendarApp.java */
    private final static Logger LOGR = Logger.getLogger(CalendarApp.class.getName());

    private static final String SCRIPT_API = "scriptAPI";
    private PythonInterpreter interpreter;
    private CalendarData calendar;

    private List<NotificationHandler> notificationHandlers; // List of objects that will receive notifications

    public ScriptApiImpl(CalendarData c) {
        calendar = c;
        // Initialize the PythonInterpreter
        interpreter = new PythonInterpreter();
        interpreter.set(SCRIPT_API, this); // Expose this classes API(interface) to
        // Jython
        // Use it in Python code
        // interpreter.exec("scriptAPI.test()"); // Calls the test() method
    }

    public void executeScript(Script script) {
        if (LOGR.isLoggable(Level.INFO)) {
            LOGR.log(Level.INFO, "Executing script: " + script.toString());
        }
        ArrayList<String> scriptContent = (ArrayList<String>) script.getContent();
        for (String line : scriptContent) {
            executeLine(line);
        }
    }

    private void executeLine(String line) {
        try {
            String methodName = line.split("\\(")[0].trim();
            String rawArgs = line.split("\\(")[1].split("\\)")[0].trim();

            // Split rawArgs by commas into an array
            String[] args = rawArgs.split(",");

            // Trim each argument to remove any extra spaces
            for (int i = 0; i < args.length; i++) {
                args[i] = args[i].trim();
            }

            // Collect all method names from CoreAPI
            Set<String> apiMethodNames = new HashSet<>();
            for (Method method : CoreAPI.class.getMethods()) {
                apiMethodNames.add(method.getName());
            }

            // Check if the method exists in the API
            if (apiMethodNames.contains(methodName)) {
                if (LOGR.isLoggable(Level.INFO)) {
                    LOGR.log(Level.INFO, "Calling method: " + "scriptAPI." + line);
                }
                interpreter.exec("scriptAPI." + line);
            } else {
                if (LOGR.isLoggable(Level.INFO)) {
                    LOGR.log(Level.INFO, "Method not found in the API, executing: " + line);
                }
                interpreter.exec(line);
            }
        } catch (PyException | IllegalArgumentException e) {
            if (LOGR.isLoggable(Level.SEVERE)) {
                LOGR.log(Level.SEVERE, "Error executing script: ", e);
            }
        }
    }

    @Override
    public Map<String, String> getArguments(String pluginID) {
        throw new UnsupportedOperationException("Arguments not supported for scripts");
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

    @Override
    public void registerForNotifications(NotificationHandler handler) {
        if (handler != null) {
            notificationHandlers.add(handler);
            if (LOGR.isLoggable(Level.INFO)) {
                LOGR.log(Level.INFO, "Registered for notifications: " + handler.getClass().getName());
            }
        }
    }

    public void close() {
        if (interpreter != null) {
            interpreter.close();
        }
    }

}
