package marteli.calendar.calendarapp.scripting;

import org.python.core.PyObject;
import org.python.icu.text.SimpleDateFormat;
import org.python.util.PythonInterpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import marteli.calendar.calendarapp.models.Script;

public class ScriptRunner {

    private PythonInterpreter interpreter;

    public ScriptRunner() {
        // Initialize the PythonInterpreter
        interpreter = new PythonInterpreter();
        interpreter.set("scriptAPI", this); // Expose this classes API(interface) to Jython
        // Use it in Python code
        interpreter.exec("scriptAPI.test()"); // Calls the test() method
    }

    public void runScript(Script script) {

        try {
            // Use exec() method to run the script content from Script object
            interpreter.exec(script.getContent());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while running the script.");
        }
    }

    public PyObject getVariable(String variableName) {
        return interpreter.get(variableName);
    }

    public void setVariable(String variableName, PyObject value) {
        interpreter.set(variableName, value);
    }

    public void deleteVariable(String variableName) {
        interpreter.exec("del " + variableName);
    }

    public void setHolidays() {
        // Create a SimpleDateFormat to parse date strings
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Create a HashMap to store the holidays
        Map<String, Date> publicHolidays = new HashMap<>();

        try {
            publicHolidays.put("New Year's Day", dateFormat.parse("2023-01-01"));
            publicHolidays.put("Australia Day", dateFormat.parse("2023-01-26"));
            publicHolidays.put("Labour Day", dateFormat.parse("2023-03-06"));
            publicHolidays.put("Good Friday", dateFormat.parse("2023-04-07"));
            publicHolidays.put("Easter Sunday", dateFormat.parse("2023-04-09"));
            publicHolidays.put("Easter Monday", dateFormat.parse("2023-04-10"));
            publicHolidays.put("Anzac Day", dateFormat.parse("2023-04-25"));
            publicHolidays.put("Western Australia Day", dateFormat.parse("2023-06-05"));
            publicHolidays.put("King's Birthday", dateFormat.parse("2023-09-25"));
            publicHolidays.put("Christmas Day", dateFormat.parse("2023-12-25"));
            publicHolidays.put("Boxing Day", dateFormat.parse("2023-12-26"));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
