package marteli.calendar.calendarapp;

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
import marteli.calendar.calendarapp.userInput.Keyboard;

public class MainMenu {
    private CalendarData calendar;
    private List<Script> scripts;
    private LocalDate currentDate;
    private ScriptRunner scriptRunner;
    private PluginLoader pluginLoader;
    private boolean isRunning = true;

    public MainMenu(CalendarData c) {
        calendar = c;
        scriptRunner = new ScriptRunner(calendar);
        pluginLoader = new PluginLoader(calendar);
        scripts = new ArrayList<>(calendar.getScripts()); // Copy scripts from calendar
        currentDate = LocalDate.now(); // Set current date to today
    }

    public void start() {
        System.out.println(UIStrings.welcomeStr);

        initScripts();
        initPlugins();
        while (isRunning) {
            DrawCalendar.draw(calendar, currentDate);
            changeDate();
        }
    }

    private void initPlugins() {
        System.out.println("Initializing plugins...");// TODO: Translate
        calendar.printData();
        for (Plugin plugin : calendar.getPlugins()) {
            pluginLoader.loadPlugin(plugin);
        }
    }

    /* Runs all scripts */
    private void initScripts() {
        System.out.println(UIStrings.runningScriptStr);
        for (Script script : scripts) {
            scriptRunner.executeScript(script);
        }
        // Close the interpreter
        scriptRunner.close();
    }

    private void changeDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                .withLocale(Locale.getDefault());
        StringJoiner output = new StringJoiner("\n");

        output.add(UIStrings.currentDateStr + currentDate.format(formatter));

        for (String option : UIStrings.menuOptionsArray()) {
            output.add(option);
        }

        output.add(UIStrings.enterChoiceStr);
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
            case "q":
                System.out.println(UIStrings.closingAppStr);
                isRunning = false;
                break;
            default:
                System.out.println(UIStrings.invalidChoiceStr);
                return false;
        }
        System.out.println(UIStrings.newDateStr + currentDate.format(formatter));
        return true;
    }
}