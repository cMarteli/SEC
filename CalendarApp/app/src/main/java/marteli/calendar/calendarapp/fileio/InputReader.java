/**
 * InputReader.java
 * Reads the calendar file
 */

package marteli.calendar.calendarapp.fileio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import marteli.calendar.calendarapp.CalendarApp;
import marteli.calendar.calendarapp.CalendarData;
import marteli.calendar.calendarapp.models.*;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads the calendar file
 * Returns null in case of error
 */
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
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
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
                LOGR.log(Level.FINE, "Error reading file: " + filePath);
            }
        }

        return null;
    }
}
