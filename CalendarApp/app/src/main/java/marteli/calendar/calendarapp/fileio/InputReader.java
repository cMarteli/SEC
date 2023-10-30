/**
 * InputReader.java
 * Reads the calendar file
 */

package marteli.calendar.calendarapp.fileio;

import marteli.calendar.calendarapp.CalendarApp;
import marteli.calendar.calendarapp.CalendarData;
import marteli.calendar.calendarapp.models.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputReader {

    /* Logger */
    private final static Logger LOGR = Logger.getLogger(CalendarApp.class.getName());

    private static LineParser<Event> eventParser = new EventParser();
    private static LineParser<Script> scriptParser = new ScriptParser();
    private static LineParser<PluginInfo> pluginParser = new PluginParser();

    public static CalendarData readCalendarFile(String filePath) {
        List<String> lines = new ArrayList<>();
        List<Event> events;
        List<Script> scripts;
        List<PluginInfo> plugins;

        Charset charset = StandardCharsets.UTF_8; // Default to UTF-8
        if (filePath.endsWith(".utf16.cal")) {
            charset = StandardCharsets.UTF_16;
        }
        // else if (filePath.endsWith(".utf32.cal")) {
        // charset = Charset.forName("UTF-32"); // TODO: UTF32 doesn't work
        // }

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), charset))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            events = eventParser.parseLines(lines);
            scripts = scriptParser.parseLines(lines);
            plugins = pluginParser.parseLines(lines);

            return new CalendarData(events, scripts, plugins);

        } catch (IOException e) {
            if (LOGR.isLoggable(Level.FINE)) {
                LOGR.log(Level.FINE, "Error reading file: " + filePath, e);
            }
        }

        return null;
    }
}
