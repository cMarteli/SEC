package marteli.calendar.calendarapp;

import java.util.ArrayList;
import java.util.List;

import edu.curtin.terminalgrid.TerminalGrid;
import marteli.calendar.calendarapp.models.CalendarData;
import marteli.calendar.calendarapp.models.Event;
import marteli.calendar.calendarapp.strings.ResourceStrings;

public class DrawCalendar {

    public static void draw(CalendarData calendar) {
        var terminalGrid = TerminalGrid.create();

        // Row and column headers
        List<String> rowHeadings = new ArrayList<>();
        for (int i = 1; i <= calendar.getEvents().size(); i++) {
            rowHeadings.add("Event " + i);
        }

        List<String> colHeadings = List.of(ResourceStrings.descriptionString, ResourceStrings.datetimeString,
                ResourceStrings.durationString);

        // Populate data as rows
        List<List<String>> listMessages = new ArrayList<>();
        for (Event ev : calendar.getEvents()) {
            List<String> row = drawRow(ev); // Call to drawRow method
            listMessages.add(row);
        }

        terminalGrid.print(listMessages, rowHeadings, colHeadings);
        System.out.println();
    }

    // Draws a single row for an event
    private static List<String> drawRow(Event ev) {
        List<String> row = new ArrayList<>();
        row.add(ev.getDescription());
        row.add(ev.getDateTime().toString());
        row.add(ev.isAllDay() ? ResourceStrings.alldayString : Integer.toString(ev.getDuration()));
        return row;
    }
}