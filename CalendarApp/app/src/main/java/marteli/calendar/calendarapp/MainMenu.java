package marteli.calendar.calendarapp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import marteli.calendar.calendarapp.graphics.DrawCalendar;
import marteli.calendar.calendarapp.models.*;
import marteli.calendar.calendarapp.scripting.ScriptRunner;
import marteli.calendar.calendarapp.strings.OutStrings;
import marteli.calendar.calendarapp.userInput.Keyboard;

public class MainMenu {
    private CalendarData calendar;
    ArrayList<Script> scripts;
    LocalDate currentDate;

    public MainMenu(CalendarData c) {
        calendar = c;
        scripts = (ArrayList<Script>) calendar.getScripts();
        currentDate = LocalDate.now();
    }

    public void Start() {
        System.out.println(OutStrings.welcomeString);
        // TODO: DEBUG ONLY
        System.out.println(OutStrings.runningFirstScriptString);
        ScriptRunner.runScript(scripts.get(0));
        // GfxTest.gfxTest();
        while (true) { // TODO: Change while (true) when done debugging
            DrawCalendar.draw(calendar, currentDate);
            changeDate();
        }
    }

    private void changeDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // TODO: Change this so it uses locale
        System.out.println(OutStrings.currentDateString + currentDate.format(formatter));
        System.out.println(OutStrings.optionsString);
        System.out.println(OutStrings.forwardOneDayString);
        System.out.println(OutStrings.forwardOneWeekString);
        System.out.println(OutStrings.forwardOneMonthString);
        System.out.println(OutStrings.forwardOneYearString);
        System.out.println(OutStrings.backwardOneDayString);
        System.out.println(OutStrings.backwardOneWeekString);
        System.out.println(OutStrings.backwardOneMonthString);
        System.out.println(OutStrings.backwardOneYearString);
        System.out.println(OutStrings.returnToTodayString);
        System.out.println(OutStrings.quitString);
        System.out.print(OutStrings.enterChoiceString);
        String choice = Keyboard.nextLine();
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
                System.out.println("Quitting...");
                System.exit(0); // TODO: might need to change this
                break;

            default:
                System.out.println("Invalid option");
        }
        System.out.println("New date: " + currentDate.format(formatter));
    }
}
