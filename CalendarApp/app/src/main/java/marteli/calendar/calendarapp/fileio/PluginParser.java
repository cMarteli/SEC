package marteli.calendar.calendarapp.fileio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import marteli.calendar.calendarapp.models.Plugin;

public class PluginParser implements LineParser<Plugin> {

    private static final Pattern PLUGIN_START_PATTERN = Pattern.compile("plugin\\s+(.*)");
    private static final String PLUGIN_END = "}";
    private static final String PLUGIN_START = "plugin";

    @Override
    public List<Plugin> parseLines(List<String> lines) {
        List<Plugin> plugins = new ArrayList<>();

        // Flag to track if we are inside a plugin block
        boolean insidePluginBlock = false;

        // Temporary storage for plugin attributes
        String pluginName = null;
        Map<String, String> pluginAttrs = new HashMap<>();

        for (String line : lines) {
            String trimmedLine = line.trim(); // Create a new variable to hold the trimmed line

            // Detect the start of a plugin block
            if (trimmedLine.startsWith(PLUGIN_START)) {
                // Validate the line using regex
                Matcher matcher = PLUGIN_START_PATTERN.matcher(trimmedLine);
                if (matcher.matches()) {
                    pluginName = matcher.group(1);
                    insidePluginBlock = true; // Mark that we're inside a plugin block
                } else {
                    // Handle invalid line format
                    System.err.println("Invalid plugin start line: " + line);
                }
                continue;
            }

            // If we are inside a plugin block
            if (insidePluginBlock) {
                // Detect the end of a plugin block
                if (trimmedLine.equals(PLUGIN_END)) {
                    // Create a Plugin object
                    Plugin plugin = new Plugin(pluginName, new HashMap<>(pluginAttrs));
                    plugins.add(plugin);

                    // Reset state
                    insidePluginBlock = false;
                    pluginName = null;
                    pluginAttrs.clear();
                } else {
                    // Parse plugin attributes, e.g., "key: value"
                    String[] parts = trimmedLine.split(":");
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim().replaceAll("^\"|\"$", "");
                        pluginAttrs.put(key, value);
                    } else {
                        // Handle invalid attribute line
                        System.err.println("Invalid attribute line: " + line);
                    }
                }
            }
        }

        return plugins;
    }
}
