/**
 * CalendarApp.java
 * Starting point for the Application.
 * NOTE: This class is not fully translated as user needs to provide the language tag as an argument.
 */
package marteli.calendar.calendarapp;

import marteli.calendar.calendarapp.strings.UIStrings;
import marteli.calendar.calendarapp.userInput.Keyboard;
import marteli.calendar.calendarapp.fileio.InputReader;
import marteli.calendar.calendarapp.models.*;

import java.util.Locale;

/**
 * Entry point for the calendar app.
 * User must provide the complete path to the calendar file as an argument.
 * Locale can be set by providing the param --locale=[IETF language tag].
 * Example: ./gradlew run --args="input_files/calendar.utf8.cal --locale=pt-BR"
 */
public class CalendarApp {

    private static final String USAGE_STR1 = "Please provide the complete path to the calendar file as an argument.";
    private static final String USAGE_STR2 = "Optional: set locale with --locale=[IETF language tag]";
    private static final String USAGE_STR3 = "USAGE: \"./gradlew run --args=\\\"input_files/calendar.utf8.cal --locale=pt-BR\\\"\"";

    private static CalendarData calendar;
    private static boolean loaded = false; // Flag to check if the calendar file was loaded
    private static Locale locale = Locale.getDefault();

    /**
     * Print help message
     */
    private static void help() {
        System.out.println(USAGE_STR1 + "\n" + USAGE_STR2 + "\n" + USAGE_STR3);
    }

    public static void main(String[] args) {
        String filePath = null;
        String localeTag = null;

        for (String arg : args) { // Parse command line arguments
            if (arg.startsWith("--locale=")) {
                localeTag = arg.substring("--locale=".length());
            } else if (filePath == null) {
                filePath = arg;
            } else { // Invalid arguments, print help message
                help();
                return;
            }
        }
        if (filePath == null) { // No file path provided, print help message
            help();
            return;
        }
        if (localeTag != null) { // Set a new locale if provided as an argument
            try {
                locale = Locale.forLanguageTag(localeTag);
                Locale.setDefault(locale);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage()); // Invalid tag
            }
        }
        /*
         * Get the labels for the current locale App is fully translated from this point
         */
        UIStrings.getInstance(locale);

        try { // Try to load the file
            calendar = InputReader.readCalendarFile(filePath);
            loaded = true;

        } catch (Exception e) {
            System.out.println(UIStrings.errorStr + e.getLocalizedMessage());
        }

        if (!loaded) { // If the file was not loaded, exit

            return;
        }

        // initialise application
        try {
            MainMenu home = new MainMenu(calendar);
            home.Start();
        } catch (Exception e) {
            System.out.println(UIStrings.errorStr + e.getLocalizedMessage());
        } finally {
            Keyboard.close(); // Close the scanner
        }

    }

}
