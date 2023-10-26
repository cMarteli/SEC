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
            line = line.trim(); // Trim the line to remove leading and trailing spaces

            if (line.startsWith(PLUGIN_START)) {
                // Entering a plugin block
                insidePluginBlock = true;

                // Parse the plugin name
                Matcher matcher = PLUGIN_START_PATTERN.matcher(line);
                if (matcher.matches()) {
                    pluginName = matcher.group(1);
                }
                continue; // Skip to the next line
            }

            if (insidePluginBlock) {
                if (line.equals(PLUGIN_END)) {
                    // Exiting a plugin block
                    insidePluginBlock = false;

                    // Create a Plugin object and add it to the list
                    Plugin plugin = new Plugin(pluginName, new HashMap<>(pluginAttrs));
                    plugins.add(plugin);

                    // Reset temporary storage
                    pluginName = null;
                    pluginAttrs.clear();
                    continue;
                }

                // Parse plugin attributes
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim().replaceAll("^\"|\"$", "");
                    pluginAttrs.put(key, value);
                }
            }
        }

        return plugins;
    }
}
