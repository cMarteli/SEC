/**
 * InputReader.java
 * Reads the calendar file
 */

package marteli.calendar.calendarapp.fileio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import marteli.calendar.calendarapp.models.*;

import java.util.ArrayList;

public class InputReader {

    private static LineParser<Event> eventParser = new EventParser();
    private static LineParser<Script> scriptParser = new ScriptParser();
    // TODO: Implement PluginParser
    // private LineParser<Plugin> pluginParser = new PluginParser();

    public static CalendarData readCalendarFile(String filePath) {
        List<String> lines = new ArrayList<>();
        List<Event> events = new ArrayList<>();
        List<Script> scripts = new ArrayList<>();
        List<Plugin> plugins = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            events = eventParser.parseLines(lines);
            scripts = scriptParser.parseLines(lines);
            // plugins = pluginParser.parseLines(lines);

        } catch (IOException e) {
            System.out.println("Error reading calendar file: " + e.getMessage());
        }

        return new CalendarData(events, scripts, plugins);
    }
}
