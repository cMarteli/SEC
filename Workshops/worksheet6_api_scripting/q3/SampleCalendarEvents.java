public class SampleCalendarEvents {
    @CalendarDate(day = 4, month = 5, name = "May the Fourth")
    public void mayTheFourth() {
        System.out.println("May the Fourth event!");
    }

    @CalendarDate(day = 25, month = 5, name = "International Towel Day")
    public void towelDay() {
        System.out.println("Towel Day event!");
    }

    public void noAnnotationMethod() {
        System.out.println("This method has no annotation.");
    }

    @CalendarDate(day = 19, month = 9, name = "International Talk Like a Pirate Day")
    public void pirates() {
        System.out.println("Talk Like a Pirate Day event!");
    }

    public static void main(String[] args) {
        SampleCalendarEvents calendarEvents = new SampleCalendarEvents();
        calendarEvents.mayTheFourth();
        calendarEvents.towelDay();
        calendarEvents.noAnnotationMethod();
        calendarEvents.pirates();
    }
}
