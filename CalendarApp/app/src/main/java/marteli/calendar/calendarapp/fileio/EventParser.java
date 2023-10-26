/*
 * EventParser.java
 * Reads an input file line by line and
 * returns a list of events
 */

package marteli.calendar.calendarapp.fileio;

import marteli.calendar.calendarapp.models.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventParser implements LineParser<Event> {

    private static final Pattern TIMED_EVENT_PATTERN = Pattern
            .compile("event (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) (\\d+) \"(.*)\"");
    private static final Pattern ALL_DAY_EVENT_PATTERN = Pattern
            .compile("event (\\d{4}-\\d{2}-\\d{2}) all-day \"(.*)\"");
    private static final String EVENT_START = "event";

    @Override
    public List<Event> parseLines(List<String> lines) {
        List<Event> events = new ArrayList<>();

        for (String line : lines) {
            if (line.startsWith(EVENT_START)) {
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

        Matcher timedMatcher = TIMED_EVENT_PATTERN.matcher(line);
        Matcher allDayMatcher = ALL_DAY_EVENT_PATTERN.matcher(line);

        if (timedMatcher.matches()) {
            String dateTimeStr = timedMatcher.group(1);
            int duration = Integer.parseInt(timedMatcher.group(2));
            String description = timedMatcher.group(3);

            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            return new Event(dateTime, duration, description, false);
        } else if (allDayMatcher.matches()) { // if it's an all-day event
            String dateStr = allDayMatcher.group(1);
            String description = allDayMatcher.group(2);

            LocalDate date = LocalDate.parse(dateStr,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime dateTime = date.atStartOfDay();

            return new Event(dateTime, 0, description, true); // true for all-day
        }

        return null; // Return null if the line doesn't match any expected format
    }

}
