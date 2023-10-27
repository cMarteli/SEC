/**
 * CalendarApp.java
 * Starting point for the Application.
 * User must provide the complete path to the calendar file as an argument.
 */
package marteli.calendar.calendarapp;

import marteli.calendar.calendarapp.strings.UIStrings;
import marteli.calendar.calendarapp.userinput.Keyboard;
import marteli.calendar.calendarapp.fileio.InputReader;
import marteli.calendar.calendarapp.models.*;

import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;

@SuppressWarnings("PMD.AvoidCatchingGenericException") // See line 77

/**
 * Entry point for the calendar app.
 * User must provide the complete path to the calendar file as an argument.
 * Locale can be set by providing the param --locale=[IETF language tag].
 * Example: ./gradlew run --args="input_files/calendar.utf8.cal --locale=pt-BR"
 */
public class CalendarApp {
    /*
     * Usage strings can't be translated, they must display even without a locale
     */
    private static final String USAGE_STR1 = "Please provide the complete path to the calendar file as an argument.";
    private static final String USAGE_STR2 = "Optional: set locale with --locale=[IETF language tag]";
    private static final String USAGE_STR3 = "eg: ./gradlew run --args=\"input_files/calendar.utf8.cal --locale=pt-BR\"";

    private static CalendarData calendar;
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
            locale = Locale.forLanguageTag(localeTag);
            Locale.setDefault(locale);
        }
        /*
         * Get the labels for the current locale App is fully translated from this point
         */
        UIStrings uiStrings = new UIStrings(locale);

        // initialise application
        try {
            setupLogger();// sets up logging to file
            calendar = InputReader.readCalendarFile(filePath);
            MainMenu home = new MainMenu(calendar);
            home.start();
        } catch (Exception e) { // only generic exception to let program "fail gracefully" still returns error
                                // to user and is logged
            if (LOGR.isLoggable(Level.SEVERE)) {
                LOGR.log(Level.SEVERE, "Unhandled Exception", e);
            }
            System.out.println(uiStrings.errorStr + e);
        } finally {
            Keyboard.close(); // Close the scanner to satisfy PMD
        }
    }

    /**
     * Logger
     */
    private final static Logger LOGR = Logger.getLogger(CalendarApp.class.getName());

    private static void setupLogger() {
        LogManager.getLogManager().reset();
        LOGR.setLevel(Level.ALL);

        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.SEVERE);
        LOGR.addHandler(ch);

        try {
            FileHandler fh = new FileHandler("Mylog.log", true);
            fh.setLevel(Level.FINE);
            LOGR.addHandler(fh);
        } catch (java.io.IOException e) {
            LOGR.log(Level.SEVERE, "Logger broke.", e);
        }
    }
}
