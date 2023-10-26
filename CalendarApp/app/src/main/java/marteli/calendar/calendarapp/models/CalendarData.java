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

    public void addEvent(Event e) {
        events.add(e);
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
