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
        String filePath = null;
        String localeTag = null;

        for (String arg : args) { // Parse command line arguments
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
        if (filePath == null) { // Check for required arguments
            help();
            return;
        }
        if (localeTag != null) { // Set locale if provided
            try {
                Locale.setDefault(Locale.forLanguageTag(localeTag));
                System.out.println("Locale set to: " + Locale.getDefault().toLanguageTag());
            } catch (Exception e) {
                System.out.println("Error setting locale: " + e.getMessage());
            }
        }
        /* Get the labels for the current locale */
        ResourceStrings.getInstance();

        try { // Load the calendar file
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
