package marteli.calendar.calendarapp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import marteli.calendar.calendarapp.models.Event;
import marteli.calendar.calendarapp.models.Plugin;
import marteli.calendar.calendarapp.models.Script;

public class CalendarData {

    private List<Event> events;
    private List<Script> scripts;
    private List<Plugin> plugins;

    public CalendarData(List<Event> e, List<Script> s, List<Plugin> p) {
        events = e;
        scripts = s;
        plugins = p;
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Script> getScripts() {
        return scripts;
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public void addEvent(Event e) {
        events.add(e);
    }

    /* Search for events from current date up to a year in the future */
    // public List<Event> searchEvents(String searchStr) {
    // searchStr = searchStr.toLowerCase();
    // String temp = "";
    // ArrayList<Event> searchResults = new ArrayList<>();
    // for (Event e : events) {
    // temp = e.getDescription().toLowerCase();
    // if (temp.contains(searchStr)) {
    // searchResults.add(e);
    // }
    // }
    // return searchResults;
    // }

    /*
     * Search for events from current date up to a year in the future
     * Returns the first event found that matches the search string
     * Returns empty optional if no event is found
     **/
    public Optional<Event> searchEvents(String searchStr, LocalDate inDate) {
        // Set search date range
        LocalDateTime startDate = inDate.atStartOfDay();
        LocalDateTime endDate = startDate.plus(1, ChronoUnit.YEARS);
        String temp = "";
        searchStr = searchStr.toLowerCase();

        for (Event e : events) {
            /* Check if event falls within search date range */
            LocalDateTime eventDateTime = e.getDateTime();
            if (eventDateTime.isAfter(startDate) && eventDateTime.isBefore(endDate)) {
                temp = e.getDescription().toLowerCase();
                if (temp.contains(searchStr)) {
                    return Optional.of(e);
                }
            }
        }
        return Optional.empty();
    }

    public void printData() {
        if (!events.isEmpty()) {
            System.out.println("Events:");
            for (Event e : events) {
                System.out.println(e);
            }
        }
        if (!scripts.isEmpty()) {
            System.out.println("Scripts:");
            for (Script s : scripts) {
                System.out.println(s);
            }
        }
        if (!plugins.isEmpty()) {
            System.out.println("Plugins:");
            for (Plugin p : plugins) {
                System.out.println(p);
            }
        }
    }
}
