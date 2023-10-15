/**
 * App.java
 */
package marteli.calendar.calendarapp;

import marteli.calendar.calendarapp.strings.ResourceStrings;
import java.util.Locale;

/**
 * Entry point for the calendar app.
 * User must provide the complete path to the calendar file as an argument.
 * Locale can be set by providing the param --locale=[IETF language tag].
 * Example: ./gradlew run --args="input_files/calendar.utf8.cal --locale=pt-BR"
 */
public class CalendarApp {

    public static void main(String[] args) {
        // Initialize variables
        String filePath = null;
        String localeTag = null;

        // Parse command line arguments
        for (String arg : args) {
            if (arg.startsWith("--locale=")) {
                localeTag = arg.substring("--locale=".length());
            } else if (filePath == null) {
                filePath = arg;
            } else {
                System.out.println("Too many arguments provided.");
                help();
                return;
            }
        }

        // Check for required arguments
        if (filePath == null) {
            help();
            return;
        }

        // Set locale if provided
        if (localeTag != null) {
            try {
                Locale.setDefault(Locale.forLanguageTag(localeTag));
                System.out.println("Locale set to: " + Locale.getDefault().toLanguageTag());
                // Get the labels for the current locale
                ResourceStrings.getInstance();
            } catch (Exception e) {
                System.out.println("Error setting locale: " + e.getMessage());
            }
        }

        try {
            // Load the calendar file
            InputReader ir = new InputReader();
            ir.readCalendarFile(filePath);
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        // GfxTest.gfxTest();
    }

    /**
     * Print help message
     */
    private static void help() {
        System.out.println("Please provide the complete path to the calendar file as an argument.");
        System.out.println("Optional: set locale with --locale=[IETF language tag]");
        System.out.println("USAGE: ./gradlew run --args=\"input_files/calendar.utf8.cal --locale=en-US\"");
    }

}
