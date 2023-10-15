package marteli.calendar.calendarapp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InputReader {

    public static void readCalendarFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading calendar file: " + e.getMessage());
        }
    }

    public static void processLine(String line) {
        if (line.startsWith("event")) {
            // Process event lines
            processEvent(line);
        } else if (line.startsWith("plugin")) {
            // Process plugin lines
            processPlugin(line);
        } else if (line.startsWith("script")) {
            // Process script lines
            processScript(line);
        }
    }

    public static void processEvent(String line) {
        // TODO: Implement parsing and storing of event details
        System.out.println("Processing event: " + line);
    }

    public static void processPlugin(String line) {
        // TODO: Implement plugin loading using Java reflection
        System.out.println("Processing plugin: " + line);
    }

    public static void processScript(String line) {
        // TODO: Implement script execution using Jython
        System.out.println("Processing script: " + line);
    }
}
