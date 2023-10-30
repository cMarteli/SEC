/**
 * NotificationHandler.java
 * Interface to handle notifications
 * @Author Victor Marteli - October 2023
 */
package marteli.calendar.calendarapp.api;

import java.util.Date;

public interface NotificationHandler {
    void handleEvent(String title, Date startDate, Date endDate);
}
