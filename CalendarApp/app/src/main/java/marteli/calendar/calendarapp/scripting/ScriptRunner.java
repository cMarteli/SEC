package marteli.calendar.calendarapp.scripting;

import org.python.util.PythonInterpreter;

import marteli.calendar.calendarapp.models.Script;

public class ScriptRunner {

    public static void runScript(Script script) {

        // Initialize the PythonInterpreter
        try (PythonInterpreter pyInterp = new PythonInterpreter()) {

            // Use exec() method to run the script content from Script object
            pyInterp.exec(script.getContent());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while running the script.");
        }
    }
}
