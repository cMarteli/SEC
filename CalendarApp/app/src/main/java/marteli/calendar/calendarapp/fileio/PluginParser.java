/**
 * PluginParser.java
 * Parses plugin information from a list of strings, creating PluginInfo
 * objects.
 */

package marteli.calendar.calendarapp.fileio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import marteli.calendar.calendarapp.CalendarApp;
import marteli.calendar.calendarapp.models.PluginInfo;

public class PluginParser implements LineParser<PluginInfo> {

    private static final Pattern PLUGIN_START_PATTERN = Pattern.compile("plugin\\s+(.*)");
    private static final String ARG_DELIMITER = "=";
    private static final String PLUGIN_START = "plugin";
    private static final String PLUGIN_END = "}";
    private static final Logger LOGGER = Logger.getLogger(CalendarApp.class.getName());

    private boolean isParsingPlugin = false;
    private String currentPluginName = null;
    private Map<String, String> currentPluginAttributes = new HashMap<>();

    /**
     * Parses each line to construct PluginInfo objects.
     *
     * @param lines List of strings, each representing a single line of input.
     * @return A list containing all parsed PluginInfo objects.
     */
    @Override
    public List<PluginInfo> parseLines(List<String> lines) {
        List<PluginInfo> plugins = new ArrayList<>();

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (isPluginStart(trimmedLine)) {
                currentPluginName = extractPluginName(trimmedLine);
                isParsingPlugin = currentPluginName != null;

                // Check if the plugin block also ends on the same line
                if (isPluginEnd(trimmedLine)) {
                    // Handle inline attributes
                    parseInlineAttributes(trimmedLine);
                    plugins.add(constructPluginInfo());
                    resetParsingState();
                }
            } else if (isParsingPlugin) {
                if (isPluginEnd(trimmedLine)) {
                    plugins.add(constructPluginInfo());
                    resetParsingState();
                } else {
                    parsePluginAttribute(trimmedLine);
                }
            }
        }

        return plugins;
    }

    /* Checks if the line indicates the start of a plugin block. */
    private boolean isPluginStart(String line) {
        // Check if the line starts with the PLUGIN_START pattern
        Matcher matcher = PLUGIN_START_PATTERN.matcher(line);
        return matcher.find();
    }

    /* Extracts the plugin name from the start pattern. */
    private String extractPluginName(String pluginStartLine) {
        // The plugin name is followed by either the end of the line or a '{' character
        Matcher matcher = PLUGIN_START_PATTERN.matcher(pluginStartLine);
        if (matcher.find()) {
            String pluginDeclaration = pluginStartLine.substring(0, matcher.end()).trim();
            // Check if '{' exists and extract the plugin name accordingly
            int startOfAttributesIndex = pluginDeclaration.indexOf('{');
            if (startOfAttributesIndex == -1) {
                // No '{' means the plugin name continues until the end of the line
                return pluginDeclaration.substring(PLUGIN_START.length()).trim();
            } else {
                // The plugin name is before '{'
                return pluginDeclaration.substring(PLUGIN_START.length(), startOfAttributesIndex).trim();
            }
        } else {
            logInvalidLine("Invalid plugin format", pluginStartLine);
            return null;
        }
    }

    /* Checks if the line indicates the end of a plugin block. */
    private boolean isPluginEnd(String line) {
        // Check if the line ends with the PLUGIN_END pattern
        return line.endsWith(PLUGIN_END);
    }

    /* Constructs a PluginInfo object from the current state. */
    private PluginInfo constructPluginInfo() {
        return new PluginInfo(currentPluginName, new HashMap<>(currentPluginAttributes));
    }

    /* Resets the state after finishing parsing a plugin block. */
    private void resetParsingState() {
        isParsingPlugin = false;
        currentPluginName = null;
        currentPluginAttributes.clear();
    }

    /* Parses a line containing a plugin attribute and stores it. */
    private void parsePluginAttribute(String line) {
        String[] parts = line.split(ARG_DELIMITER);
        if (parts.length == 2) {
            String key = parts[0].trim();
            String value = parts[1].trim().replaceAll("^\"|\",?$", ""); // Remove quotes and trailing commas
            currentPluginAttributes.put(key, value);
        } else {
            logInvalidLine("Invalid attribute line", line);
        }
    }

    /* Logs a message if a line is not in the expected format. */
    private void logInvalidLine(String message, String line) {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, message + ": " + line);
        }
    }

    /*
     * Parses a line for inline attributes if the plugin definition is on a single
     * line.
     */
    private void parseInlineAttributes(String line) {
        // Extract substring between { and }
        int start = line.indexOf('{') + 1;
        int end = line.indexOf('}');
        if (start != -1 && end != -1 && start < end) {
            String attributesStr = line.substring(start, end).trim();
            String[] attributes = attributesStr.split(",");
            for (String attribute : attributes) {
                parsePluginAttribute(attribute);
            }
        }
    }
}
