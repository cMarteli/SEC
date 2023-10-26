/**
 * UIStrings.java
 * Singleton class used to stored references to UI strings.
 */

package marteli.calendar.calendarapp.strings;

import java.util.Locale;
import java.util.ResourceBundle;

public final class UIStrings {

    private static UIStrings single_instance = null;
    private static ResourceBundle bundle;

    // CalendarApp.java
    public static String errorStr;
    // labels
    public static String eventStr, alldayStr, descriptionStr, datetimeStr, durationStr;

    //
    public static String welcomeStr;
    public static String runningScriptStr;
    public static String currentDateStr;
    public static String optionsStr;

    // Menu
    public static String forwardOneDayStr;
    public static String forwardOneWeekStr;
    public static String forwardOneMonthStr;
    public static String forwardOneYearStr;
    public static String backwardOneDayStr;
    public static String backwardOneWeekStr;
    public static String backwardOneMonthStr;
    public static String backwardOneYearStr;
    public static String returnToTodayStr;
    public static String quitStr;
    public static String enterChoiceStr;
    public static String closingAppStr;
    public static String invalidChoiceStr;
    public static String newDateStr;

    // Private constructor
    private UIStrings(Locale locale) {
        bundle = ResourceBundle.getBundle("bundle", locale);

        // Initialise Tab fields after bundle is set

        // CalendarApp.java
        errorStr = bundle.getString("error_str");
        // usage1Str = bundle.getString("usage_str1");
        // usage2Str = bundle.getString("usage_str2");
        // usage3Str = bundle.getString("usage_str3");

        eventStr = bundle.getString("event_str");
        alldayStr = bundle.getString("allday_str");
        descriptionStr = bundle.getString("description_str");
        datetimeStr = bundle.getString("datetime_str");
        durationStr = bundle.getString("duration_str");

        welcomeStr = bundle.getString("welcome_str");
        runningScriptStr = bundle.getString("running_first_script_str");
        currentDateStr = bundle.getString("current_date_str");
        optionsStr = bundle.getString("options_str");
        closingAppStr = bundle.getString("closing_app_str");

        forwardOneDayStr = bundle.getString("forward_one_day_str");
        forwardOneWeekStr = bundle.getString("forward_one_week_str");
        forwardOneMonthStr = bundle.getString("forward_one_month_str");
        forwardOneYearStr = bundle.getString("forward_one_year_str");
        backwardOneDayStr = bundle.getString("backward_one_day_str");
        backwardOneWeekStr = bundle.getString("backward_one_week_str");
        backwardOneMonthStr = bundle.getString("backward_one_month_str");
        backwardOneYearStr = bundle.getString("backward_one_year_str");
        returnToTodayStr = bundle.getString("return_to_today_str");
        quitStr = bundle.getString("quit_str");
        enterChoiceStr = bundle.getString("enter_choice_str");
        invalidChoiceStr = bundle.getString("invalid_choice_str");
        newDateStr = bundle.getString("new_date_str");
    }

    /**
     *
     * Returns an array of strings for the options menu
     *
     * @return
     */
    public static String[] menuOptionsArray() {
        return new String[] {
                optionsStr,
                forwardOneDayStr,
                forwardOneWeekStr,
                forwardOneMonthStr,
                forwardOneYearStr,
                backwardOneDayStr,
                backwardOneWeekStr,
                backwardOneMonthStr,
                backwardOneYearStr,
                returnToTodayStr,
                quitStr };
    }

    // Static method to create an instance of the Singleton class
    public static synchronized UIStrings getInstance(Locale locale) {
        if (single_instance == null) {
            if (locale == null) {
                locale = Locale.getDefault();
            }
            single_instance = new UIStrings(locale);
        }
        return single_instance;
    }
}
