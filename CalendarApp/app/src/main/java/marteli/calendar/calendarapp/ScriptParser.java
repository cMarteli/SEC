package marteli.calendar.calendarapp;

import java.util.ArrayList;
import java.util.List;

import marteli.calendar.calendarapp.models.Script;

public class ScriptParser implements LineParser<Script> {

    @Override
    public List<Script> parseLines(List<String> lines) {
        List<Script> scripts = new ArrayList<>();

        // Flag to track if we are inside a script block
        boolean insideScriptBlock = false;

        // StringBuilder to accumulate lines of a single script
        StringBuilder scriptContent = new StringBuilder();

        for (String line : lines) {
            if (line.startsWith("script")) {
                // Entering a script block
                insideScriptBlock = true;
                // Reset content for a new script
                scriptContent = new StringBuilder();
                continue; // Skip to the next line
            }

            if (insideScriptBlock) {
                // Check if line is the end of the script block
                if (line.equals("\"")) {
                    // Exiting a script block
                    insideScriptBlock = false;

                    // Create a Script object and add it to the list
                    // Replace escaped double quotes with a single double quote before creating
                    // Script object
                    String adjustedScriptContent = scriptContent.toString().replace("\"\"", "\"");
                    Script script = new Script(adjustedScriptContent);

                    if (script != null) {
                        scripts.add(script);
                    }
                    continue;
                }

                // Append the current line to the script content
                scriptContent.append(line).append("\n");
            }
        }

        return scripts;
    }

}
