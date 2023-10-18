/**
 * Singleton class that uses the given locale to create a ResourceBundle
 */

package marteli.calendar.calendarapp.strings;

import java.util.Locale;
import java.util.ResourceBundle;

public final class OutStrings {

    private static OutStrings single_instance = null;
    private ResourceBundle bundle;

    // Strings
    public static String eventString;
    public static String alldayString;
    public static String descriptionString;
    public static String datetimeString;
    public static String durationString;

    public static String welcomeString;
    public static String runningFirstScriptString;
    public static String currentDateString;
    public static String optionsString;

    public static String forwardOneDayString;
    public static String forwardOneWeekString;
    public static String forwardOneMonthString;
    public static String forwardOneYearString;
    public static String backwardOneDayString;
    public static String backwardOneWeekString;
    public static String backwardOneMonthString;
    public static String backwardOneYearString;
    public static String returnToTodayString;
    public static String quitString;
    public static String enterChoiceString;

    // Private constructor
    private OutStrings() {
        bundle = ResourceBundle.getBundle("bundle", Locale.getDefault());

        // Initialise Tab fields after bundle is set
        eventString = bundle.getString("event_str");
        alldayString = bundle.getString("allday_str");
        descriptionString = bundle.getString("description_str");
        datetimeString = bundle.getString("datetime_str");
        durationString = bundle.getString("duration_str");

        welcomeString = bundle.getString("welcome_str");
        runningFirstScriptString = bundle.getString("running_first_script_str");
        currentDateString = bundle.getString("current_date_str");
        optionsString = bundle.getString("options_str");

        forwardOneDayString = bundle.getString("forward_one_day_str");
        forwardOneWeekString = bundle.getString("forward_one_week_str");
        forwardOneMonthString = bundle.getString("forward_one_month_str");
        forwardOneYearString = bundle.getString("forward_one_year_str");
        backwardOneDayString = bundle.getString("backward_one_day_str");
        backwardOneWeekString = bundle.getString("backward_one_week_str");
        backwardOneMonthString = bundle.getString("backward_one_month_str");
        backwardOneYearString = bundle.getString("backward_one_year_str");
        returnToTodayString = bundle.getString("return_to_today_str");
        quitString = bundle.getString("quit_str");
        enterChoiceString = bundle.getString("enter_choice_str");
    }

    // Static method to create an instance of the Singleton class
    public static synchronized OutStrings getInstance() {
        if (single_instance == null) {
            single_instance = new OutStrings();
        }
        return single_instance;
    }
}
