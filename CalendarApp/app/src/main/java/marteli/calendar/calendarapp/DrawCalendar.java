package marteli.calendar.calendarapp;

import java.util.ArrayList;
import java.util.List;

import edu.curtin.terminalgrid.TerminalGrid;
import marteli.calendar.calendarapp.models.CalendarData;
import marteli.calendar.calendarapp.models.Event;

public class DrawCalendar {

    public static void draw(CalendarData calendar) {
        var terminalGrid = TerminalGrid.create();

        List<List<String>> listMessages = new ArrayList<>();

        // Initialize columns
        List<String> dateTimes = new ArrayList<>();
        List<String> durations = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();

        // Populate data
        for (Event ev : calendar.getEvents()) {
            dateTimes.add(ev.getDateTime().toString());
            durations.add(ev.isAllDay() ? "All day" : Integer.toString(ev.getDuration()));
            descriptions.add(ev.getDescription());
        }

        listMessages.add(dateTimes);
        listMessages.add(durations);
        listMessages.add(descriptions);

        // Row and column headers
        List<String> rowHeadings = new ArrayList<>();
        for (int i = 1; i <= dateTimes.size(); i++) {
            rowHeadings.add("Event " + i);
        }

        List<String> colHeadings = List.of("DateTime", "Duration", "Description");

        terminalGrid.print(listMessages, rowHeadings, colHeadings);
        System.out.println();
    }
}
