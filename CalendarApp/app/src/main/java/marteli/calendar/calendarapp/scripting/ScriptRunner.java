package marteli.calendar.calendarapp.scripting;

import marteli.calendar.calendarapp.api.CoreAPI;
import marteli.calendar.calendarapp.api.NotificationHandler;
import marteli.calendar.calendarapp.models.CalendarData;
import marteli.calendar.calendarapp.models.Event;
import marteli.calendar.calendarapp.models.Script;

import java.util.ArrayList;
import java.util.Map;
import org.python.util.PythonInterpreter;

import com.kenai.jffi.Array;

public class ScriptRunner implements CoreAPI {

    private PythonInterpreter interpreter;
    private CalendarData calendar;
    private CoreAPI scriptAPI = this; // TODO: Might not be needed

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
        } catch (Exception e) {
            System.out.println("Error executing script" + e.getLocalizedMessage());
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
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return; // Don't add the event if it's invalid
        }
        calendar.addEvent(event);
    }

    @Override
    public void registerForNotifications(NotificationHandler handler) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerForNotifications'");
    }

    public void closeInterpreter() {
        if (interpreter != null) {
            interpreter.close();
        }
    }

}
