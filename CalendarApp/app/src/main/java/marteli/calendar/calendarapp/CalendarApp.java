/**
 * App.java
 */
package marteli.calendar.calendarapp;

public class CalendarApp {

    public static void main(String[] args) {
        // first command line argument is the complete path to the file
        if (args.length < 1) {
            help();
            return;
        }
        if (args.length > 1) {
            System.out.println("Too many arguments provided.");
            help();
            return;
        }
        if (args[0].equals("-h") || args[0].equals("--help")) {
            help();
            return;
        }
        String filePath = args[0];
        try {
            InputReader.readCalendarFile(filePath);
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        // GfxTest.gfxTest();
    }

    /* Displays help message to user */
    private static void help() {
        System.out.println("Please provide the complete path to the calendar file as an argument.");
        System.out.println("USAGE: ./gradlew run --args=\"input_files/calendar.utf8.cal\"");
    }

}
