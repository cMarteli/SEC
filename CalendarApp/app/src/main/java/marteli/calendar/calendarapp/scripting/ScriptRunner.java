package marteli.calendar.calendarapp.scripting;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import marteli.calendar.calendarapp.models.Script;

public class ScriptRunner implements ScriptAPI {

    private PythonInterpreter interpreter;

    public ScriptRunner() {
        // Initialize the PythonInterpreter
        interpreter = new PythonInterpreter();
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
}
