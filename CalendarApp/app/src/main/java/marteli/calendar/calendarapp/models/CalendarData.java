package marteli.calendar.calendarapp.models;

import java.util.List;

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
}
