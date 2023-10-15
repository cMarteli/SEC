package marteli.calendar.calendarapp.strings;

/**
 * Singleton class that uses the given locale to create a ResourceBundle
 */
import java.util.Locale;
import java.util.ResourceBundle;

public final class ResourceStrings {

    private static ResourceStrings single_instance = null;
    private ResourceBundle bundle;

    // Strings
    public static String processString;
    public static String languageString;

    // Private constructor
    private ResourceStrings() {
        bundle = ResourceBundle.getBundle("bundle", Locale.getDefault());

        // Initialize Tab fields after bundle is set
        processString = bundle.getString("processing_str");
        languageString = bundle.getString("language_str");
    }

    // Static method to create an instance of the Singleton class
    public static synchronized ResourceStrings getInstance() {
        if (single_instance == null) {
            single_instance = new ResourceStrings();
        }
        return single_instance;
    }
}
