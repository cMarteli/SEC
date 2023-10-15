package marteli.calendar.calendarapp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import marteli.calendar.calendarapp.strings.ResourceStrings;

public class InputReader {

    public void readCalendarFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading calendar file: " + e.getMessage());
        }
    }

    public void processLine(String line) {
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

    public void processEvent(String line) {
        // TODO: Implement parsing and storing of event details
        System.out.println(ResourceStrings.processString + line);
    }

    public void processPlugin(String line) {
        // TODO: Implement plugin loading using Java reflection
        System.out.println(ResourceStrings.processString + line);
    }

    public void processScript(String line) {
        // TODO: Implement script execution using Jython
        System.out.println(ResourceStrings.processString + line);
    }
}
