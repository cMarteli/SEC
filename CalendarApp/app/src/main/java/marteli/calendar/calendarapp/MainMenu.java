package marteli.calendar.calendarapp;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringJoiner;

import marteli.calendar.calendarapp.api.*;
import marteli.calendar.calendarapp.graphics.DrawCalendar;
import marteli.calendar.calendarapp.models.*;
import marteli.calendar.calendarapp.strings.UIStrings;
import marteli.calendar.calendarapp.userinput.Keyboard;

public class MainMenu {

    private CalendarData calendar;
    private List<Script> scripts;
    private LocalDate currentDate;
    private ScriptRunner scriptRunner;
    private PluginLoader pluginLoader;

    private boolean isRunning = true;

    private Locale locale = Locale.getDefault();
    private UIStrings uiStrings = new UIStrings(locale);
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(locale);

    public MainMenu(CalendarData c) {
        calendar = c;
        scriptRunner = new ScriptRunner(calendar);
        pluginLoader = new PluginLoader(calendar);
        scripts = new ArrayList<>(calendar.getScripts()); // Copy scripts from calendar
        currentDate = LocalDate.now(); // Set current date to today
    }

    public void start() {
        System.out.println(uiStrings.welcomeStr);
        // calendar.printData(); // TODO: DEBUG ONLY
        initScripts();
        initPlugins();
        waitForUser(); // TODO: DEBUG Makes it easier to see the output
        while (isRunning) {
            DrawCalendar.draw(calendar, currentDate, uiStrings);
            changeDate();
        }
    }

    /* Initialise all plugins */
    private void initPlugins() {
        System.out.println(uiStrings.initPluStr);
        for (Plugin plugin : calendar.getPlugins()) {
            pluginLoader.loadPlugin(plugin);
        }
    }

    /* Runs all scripts */
    private void initScripts() {
        System.out.println(uiStrings.runningScriptStr);
        for (Script script : scripts) {
            scriptRunner.executeScript(script);
        }
        // Close the interpreter
        scriptRunner.close();
    }

    private void searchEvents() {
        String searchStr = Keyboard.nextLine();
        /* search for event with matching description */
        if (calendar.searchFutureEvents(searchStr, currentDate).isPresent()) {
            Event match = calendar.searchFutureEvents(searchStr, currentDate).get();

            System.out.println(uiStrings.eventFoundStr + prettyPrint(match));
            // update current date to event date
            currentDate = match.getDateTime().toLocalDate();
        } else {
            System.out.println(uiStrings.noEventsFoundStr);
        }
    }

    /* Returns localised event information */
    public String prettyPrint(Event event) {
        if (event.isAllDay()) {
            return "\n" + uiStrings.descriptionStr + event.getDescription() +
                    "\n" + uiStrings.datetimeStr + event.getDateTime().format(formatter) +
                    "\n" + uiStrings.allDayStr;
        }
        return "\n" + uiStrings.descriptionStr + event.getDescription() +
                "\n" + uiStrings.datetimeStr + event.getDateTime().format(formatter) +
                "\n" + uiStrings.durationStr + event.getDuration();

    }

    /* Changes date based on menu choice */
    private void changeDate() {
        StringJoiner output = new StringJoiner("\n");

        output.add(uiStrings.currentDateStr + currentDate.format(formatter));
        /* Prints menu options */
        for (String option : uiStrings.menuOptionsArray()) {
            output.add(option);
        }

        output.add(uiStrings.enterChoiceStr);
        System.out.print(output.toString());

        // Input validation loop
        while (true) {
            String choice = Keyboard.nextLine();
            if (processChoice(choice, formatter)) {
                break;
            }
        }
    }

    // Returns true if the choice was valid and processed, otherwise false
    private boolean processChoice(String choice, DateTimeFormatter formatter) {
        switch (choice) {
            case "+d":
                currentDate = currentDate.plusDays(1);
                break;
            case "+w":
                currentDate = currentDate.plusWeeks(1);
                break;
            case "+m":
                currentDate = currentDate.plusMonths(1);
                break;
            case "+y":
                currentDate = currentDate.plusYears(1);
                break;
            case "-d":
                currentDate = currentDate.minusDays(1);
                break;
            case "-w":
                currentDate = currentDate.minusWeeks(1);
                break;
            case "-m":
                currentDate = currentDate.minusMonths(1);
                break;
            case "-y":
                currentDate = currentDate.minusYears(1);
                break;
            case "t":
                currentDate = LocalDate.now();
                break;
            case "s":
                System.out.println(uiStrings.enterSearchTermStr);
                searchEvents();
                waitForUser(); // Lets user see the output
                break;
            case "q":
                System.out.println(uiStrings.closingAppStr);
                isRunning = false;
                break;
            default:
                System.out.println(uiStrings.invalidChoiceStr);
                return false;
        }
        return true;
    }

    /* Ask user to press ENTER to proceed */
    private void waitForUser() {
        System.out.println(uiStrings.enterToContinueStr);

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}