/**
 * Singleton class that uses the given locale to create a ResourceBundle
 */

package marteli.calendar.calendarapp.strings;

import java.util.Locale;
import java.util.ResourceBundle;

public final class ResourceStrings {

    private static ResourceStrings single_instance = null;
    private ResourceBundle bundle;

    // Strings
    public static String processString;
    public static String eventString;
    public static String alldayString;
    public static String descriptionString;
    public static String datetimeString;
    public static String durationString;

    // Private constructor
    private ResourceStrings() {
        bundle = ResourceBundle.getBundle("bundle", Locale.getDefault());

        // Initialise Tab fields after bundle is set
        processString = bundle.getString("processing_str");
        eventString = bundle.getString("event_str");
        alldayString = bundle.getString("allday_str");
        descriptionString = bundle.getString("description_str");
        datetimeString = bundle.getString("datetime_str");
        durationString = bundle.getString("duration_str");
    }

    // Static method to create an instance of the Singleton class
    public static synchronized ResourceStrings getInstance() {
        if (single_instance == null) {
            single_instance = new ResourceStrings();
        }
        return single_instance;
    }
}
