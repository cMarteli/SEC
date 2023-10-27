/**
 * DrawCalendar.java
 * Class responsible for drawing the calendar based on the provided
 * CalendarData.
 */
package marteli.calendar.calendarapp.graphics;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import edu.curtin.terminalgrid.TerminalGrid;
import marteli.calendar.calendarapp.models.CalendarData;
import marteli.calendar.calendarapp.models.Event;
import marteli.calendar.calendarapp.strings.UIStrings;

public class DrawCalendar {

    /**
     * Public method to draw the calendar.
     *
     * @param calendar The CalendarData object containing events.
     */
    public static void draw(CalendarData calendar, LocalDate currentDate, UIStrings uiStrings) {
        var terminalGrid = TerminalGrid.create();
        // Use LinkedHashMap to maintain insertion order
        Map<LocalDate, Map<LocalTime, List<Event>>> weekMap = new LinkedHashMap<>();

        // LocalDate currentDate = LocalDate.now();
        populateWeekMap(weekMap, currentDate);

        groupEventsByDateAndHour(calendar, weekMap);

        List<String> rowHeadings = createRowHeadings(uiStrings.allDayStr);
        List<String> colHeadings = createColumnHeadings(weekMap);

        List<List<String>> listMessages = populateRows(weekMap);
        terminalGrid.setTerminalWidth(200); // To avoid wrapping
        terminalGrid.print(listMessages, rowHeadings, colHeadings);
        System.out.println();
    }

    /**
     * Populate the weekMap for the current week starting from the given
     * currentDate.
     *
     * @param weekMap     The map to populate.
     * @param currentDate The starting date.
     */
    private static void populateWeekMap(Map<LocalDate, Map<LocalTime, List<Event>>> weekMap, LocalDate currentDate) {
        for (int i = 0; i < 7; i++) {
            LocalDate day = currentDate.plusDays(i);
            weekMap.put(day, new HashMap<>());
        }
    }

    /**
     * Groups events by their date and hour, and stores them in the weekMap.
     *
     * @param calendar The CalendarData object containing events.
     * @param weekMap  The map to populate.
     */
    private static void groupEventsByDateAndHour(CalendarData calendar,
            Map<LocalDate, Map<LocalTime, List<Event>>> weekMap) {
        for (Event ev : calendar.getEvents()) {
            LocalDate date = ev.getDateTime().toLocalDate();
            LocalTime time = ev.getDateTime().toLocalTime();

            if (weekMap.containsKey(date)) {
                weekMap.get(date).putIfAbsent(time, new ArrayList<>());
                weekMap.get(date).get(time).add(ev);
            }
        }
    }

    /**
     * Creates row headings for the terminal grid display.
     *
     * @return List of row headings as strings.
     */
    private static List<String> createRowHeadings(String allDayStr) {
        List<String> rowHeadings = new ArrayList<>();
        rowHeadings.add(allDayStr);
        for (int i = 0; i < 24; i++) {
            rowHeadings.add(String.format("%02d:00", i));
        }
        return rowHeadings;
    }

    /**
     * Creates column headings based on the weekMap keys.
     *
     * @param weekMap The map containing the days for the column headings.
     * @return List of column headings as strings.
     */
    private static List<String> createColumnHeadings(Map<LocalDate, Map<LocalTime, List<Event>>> weekMap) {
        List<String> colHeadings = new ArrayList<>();
        for (LocalDate day : weekMap.keySet()) {
            colHeadings.add(day.toString());
        }
        return colHeadings;
    }

    /**
     * Populates rows with events for the terminal grid.
     *
     * @param weekMap     The map containing events grouped by date and time.
     * @param rowHeadings The list of row headings.
     * @return A 2D list containing strings to be displayed.
     */
    private static List<List<String>> populateRows(Map<LocalDate, Map<LocalTime, List<Event>>> weekMap) {
        List<List<String>> listMessages = new ArrayList<>();
        populateAllDayRow(listMessages, weekMap);
        populateHourlyRows(listMessages, weekMap);
        return listMessages;
    }

    /**
     * Populates the "All-day" row of the calendar.
     *
     * @param listMessages The 2D list to populate.
     * @param weekMap      The map containing events grouped by date.
     */
    private static void populateAllDayRow(List<List<String>> listMessages,
            Map<LocalDate, Map<LocalTime, List<Event>>> weekMap) {
        List<String> allDayRow = new ArrayList<>();
        for (LocalDate day : weekMap.keySet()) {
            StringBuilder cell = new StringBuilder();
            for (List<Event> events : weekMap.get(day).values()) {
                for (Event ev : events) {
                    if (ev.isAllDay()) {
                        cell.append(ev.getDescription()).append("; "); // TODO: Placeholder
                    }
                }
            }
            allDayRow.add(cell.toString());
        }
        listMessages.add(allDayRow);
    }

    /**
     * Populates the rows based on the hourly events.
     *
     * @param listMessages The 2D list to populate.
     * @param weekMap      The map containing events grouped by date and time.
     * @param rowHeadings  The list of row headings.
     */
    private static void populateHourlyRows(List<List<String>> listMessages,
            Map<LocalDate, Map<LocalTime, List<Event>>> weekMap) {
        for (int i = 0; i < 24; i++) {
            List<String> hourRow = new ArrayList<>();
            LocalTime time = LocalTime.of(i, 0);
            for (LocalDate day : weekMap.keySet()) {
                StringBuilder cell = new StringBuilder();
                if (weekMap.get(day).containsKey(time)) {
                    for (Event ev : weekMap.get(day).get(time)) {
                        if (!ev.isAllDay()) {
                            String duration = " (" + Integer.toString(ev.getDuration()) + "min)";
                            cell.append(ev.getDescription()).append(duration);
                        }
                    }
                }
                hourRow.add(cell.toString());
            }
            listMessages.add(hourRow);
        }
    }
}
