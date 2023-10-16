package marteli.calendar.calendarapp;

/**
 * InputReader.java
 * Reads the calendar file
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class InputReader {

    EventParser eventParser = new EventParser();

    /**
     * Reads the calendar file and returns a list of lines
     *
     * @param filePath path to the calendar file
     * @return list of lines
     */
    public List<String> readCalendarFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading calendar file: " + e.getMessage());
        }
        return lines;
    }
}
