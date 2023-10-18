/**
 * CalendarApp.java
 */
package marteli.calendar.calendarapp;

import marteli.calendar.calendarapp.strings.OutStrings;
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

    private static CalendarData calendar;
    private static boolean loaded = false; // Flag to check if the calendar file was loaded

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
        if (filePath == null) { // No file path provided, print help message
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
        OutStrings.getInstance();

        try { // Try to load the file
            calendar = InputReader.readCalendarFile(filePath);
            loaded = true;

        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }

        if (!loaded) { // If the file was not loaded, exit

            return;
        }

        // initialise application
        try {
            MainMenu home = new MainMenu(calendar);
            home.Start();
        } catch (Exception e) {
            System.out.println("Error starting application: " + e.getMessage());
        } finally {
            Keyboard.close();
        }

    }

    /**
     * Print help message
     */
    private static void help() {
        System.out.println("Please provide the complete path to the calendar file as an argument.");
        System.out.println("Optional: set locale with --locale=[IETF language tag]");
        System.out.println("USAGE: ./gradlew run --args=\"input_files/calendar.utf8.cal --locale=pt-BR\"");
    }

}
