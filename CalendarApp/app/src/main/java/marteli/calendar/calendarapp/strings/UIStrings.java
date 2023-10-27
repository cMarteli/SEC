package marteli.calendar.calendarapp.strings;

import java.util.Locale;
import java.util.ResourceBundle;

public final class UIStrings {

    private static ResourceBundle bundle;

    // All UI strings
    public String errorStr, initPluStr;
    public String eventStr, allDayStr, descriptionStr, datetimeStr, durationStr;
    public String welcomeStr, runningScriptStr, currentDateStr, optionsStr;
    public String forwardOneDayStr, forwardOneWeekStr, forwardOneMonthStr, forwardOneYearStr;
    public String backwardOneDayStr, backwardOneWeekStr, backwardOneMonthStr, backwardOneYearStr;
    public String returnToTodayStr, quitStr, enterChoiceStr, closingAppStr, invalidChoiceStr, newDateStr;

    public UIStrings(Locale locale) {
        bundle = ResourceBundle.getBundle("bundle", locale);

        errorStr = bundle.getString("error_str");
        initPluStr = bundle.getString("init_plu_str");
        eventStr = bundle.getString("event_str");
        allDayStr = bundle.getString("allday_str");
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

    public String[] menuOptionsArray() {
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
                quitStr
        };
    }
}
