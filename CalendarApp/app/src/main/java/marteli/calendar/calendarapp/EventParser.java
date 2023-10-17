/*
 * EventParser.java
 * Reads an input file line by line and
 * returns a list of events
 */

package marteli.calendar.calendarapp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import marteli.calendar.calendarapp.models.Event;

public class EventParser implements LineParser<Event> {

    @Override
    public List<Event> parseLines(List<String> lines) {
        List<Event> events = new ArrayList<>();

        for (String line : lines) {
            if (line.startsWith("event")) {
                Event event = parseEvent(line);
                if (event != null) {
                    events.add(event);
                }
            }
        }

        return events;
    }

    /**
     * Parses a single event from a line
     * Assumptions: If event is all-day sets the start of the day for the
     * LocalDateTime and duration is set to 0
     *
     * @param line
     * @return
     */
    private Event parseEvent(String line) {
        // Timed event pattern
        Pattern pattern1 = Pattern.compile("event (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) (\\d+) \"(.*)\"");
        // Pattern for all-day events
        Pattern pattern2 = Pattern.compile("event (\\d{4}-\\d{2}-\\d{2}) all-day \"(.*)\"");

        Matcher matcher1 = pattern1.matcher(line);
        Matcher matcher2 = pattern2.matcher(line);

        if (matcher1.matches()) {
            String dateTimeStr = matcher1.group(1);
            int duration = Integer.parseInt(matcher1.group(2));
            String description = matcher1.group(3);

            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            return new Event(dateTime, duration, description, false);
        } else if (matcher2.matches()) { // if it's an all-day event
            String dateStr = matcher2.group(1);
            String description = matcher2.group(2);

            LocalDate date = LocalDate.parse(dateStr,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime dateTime = date.atStartOfDay();

            return new Event(dateTime, 0, description, true); // true for all-day
        }

        return null; // Return null if the line doesn't match any expected format
    }

}
