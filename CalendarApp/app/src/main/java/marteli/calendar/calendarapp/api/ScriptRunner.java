package marteli.calendar.calendarapp.api;

import marteli.calendar.calendarapp.CalendarApp;
import marteli.calendar.calendarapp.models.CalendarData;
import marteli.calendar.calendarapp.models.Event;
import marteli.calendar.calendarapp.models.Script;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.python.core.PyException;
import org.python.util.PythonInterpreter;

public class ScriptRunner implements CoreAPI {

    /* Logger from CalendarApp.java */
    private final static Logger LOGR = Logger.getLogger(CalendarApp.class.getName());

    private PythonInterpreter interpreter;
    private CalendarData calendar;

    public ScriptRunner(CalendarData c) {
        calendar = c;
        // Initialize the PythonInterpreter
        interpreter = new PythonInterpreter();
        interpreter.set("scriptAPI", this); // Expose this classes API(interface) to
        // Jython
        // Use it in Python code
        // interpreter.exec("scriptAPI.test()"); // Calls the test() method
    }

    public void executeScript(Script script) {
        System.out.println("Executing script: " + script.toString()); // DEBUG

        ArrayList<String> scriptContent = (ArrayList<String>) script.getContent();
        for (String line : scriptContent) {
            executeLine(line);
        }
    }

    private void executeLine(String line) {
        try {
            interpreter.exec("scriptAPI." + line);
        } catch (PyException | IllegalArgumentException e) {
            if (LOGR.isLoggable(Level.FINE)) {
                LOGR.log(Level.FINE, "Error executing script: ", e);
            }
        }
    }

    @Override
    public Map<String, String> getArguments(String pluginID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArguments'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerForNotifications'");
    }

    public void close() {
        if (interpreter != null) {
            interpreter.close();
        }
    }

}
