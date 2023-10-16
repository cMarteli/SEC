package marteli.calendar.calendarapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import marteli.calendar.calendarapp.models.Event;

public class EventParser {

    public List<Event> parseEvents(List<String> lines) {
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

    private Event parseEvent(String line) {
        Pattern pattern = Pattern.compile("event (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) (\\d+) \"(.*)\"");
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            String dateTimeStr = matcher.group(1);
            int duration = Integer.parseInt(matcher.group(2));
            String description = matcher.group(3);

            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // TODO: should be true with allday events
            return new Event(dateTime, duration, description, false);
        }

        return null; // Return null if the line doesn't match the expected format
    }
}
