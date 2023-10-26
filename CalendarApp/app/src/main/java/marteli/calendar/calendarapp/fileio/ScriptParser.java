package marteli.calendar.calendarapp.fileio;

import java.util.ArrayList;
import java.util.List;

import marteli.calendar.calendarapp.models.Script;

public class ScriptParser implements LineParser<Script> {

    @Override
    public List<Script> parseLines(List<String> lines) {
        List<Script> scripts = new ArrayList<>();

        // Flag to track if we are inside a script block
        boolean insideScriptBlock = false;

        // List to accumulate lines of a single script
        List<String> scriptLines = new ArrayList<>();

        for (String line : lines) {
            // If the line is a comment, skip it
            if (line.trim().startsWith("#")) {
                continue;
            }

            if (line.startsWith("script")) {
                // Entering a script block
                insideScriptBlock = true;
                // Reset content for a new script
                scriptLines = new ArrayList<>();
                continue; // Skip to the next line
            }

            if (insideScriptBlock) {
                // Check if line is the end of the script block
                if (line.equals("\"")) {
                    // Exiting a script block
                    insideScriptBlock = false;

                    // Create a Script object and add it to the list
                    Script script = new Script(new ArrayList<>(scriptLines));
                    scripts.add(script);
                    continue;
                }

                // Add the current line to the script lines list
                scriptLines.add(line);
            }
        }

        return scripts;
    }

}
