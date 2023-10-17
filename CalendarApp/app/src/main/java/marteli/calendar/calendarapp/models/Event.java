/**
 * Event.java
 * Holds event details such as date, duration, description and if it's an all-day event
 */

package marteli.calendar.calendarapp.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Event {
    private LocalDateTime dateTime;
    private Integer duration; // Null if it's an all-day event
    private String description;
    private boolean allDay;

    public Event(LocalDateTime inDate, Integer inDur, String inDes, boolean inAll) {
        dateTime = inDate;
        duration = inDur;
        description = inDes;
        allDay = inAll;
    }

    /* Getters and setters */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dt) {
        dateTime = dt;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int d) {
        duration = d;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean ad) {
        allDay = ad;
    }

    public void setDescription(String d) {
        description = d;
    }

    @Override
    public String toString() {
        if (allDay) {
            return "Event{" +
                    "dateTime=" + dateTime +
                    ", description='" + description + '\'' +
                    ", allDay=" + allDay +
                    '}';
        }
        return "Event{" +
                "dateTime=" + dateTime +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", allDay=" + allDay +
                '}';
    }

    /* Object Comparison Methods */
    /**
     * Compare two events by date and time
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Event event = (Event) o;
        return allDay == event.allDay && Objects.equals(dateTime, event.dateTime)
                && Objects.equals(duration, event.duration) && Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, duration, description, allDay);
    }

}
