import java.lang.reflect.Method;
import java.util.Scanner;

public class AnnotationExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the name of a Java class: ");
        String className = scanner.next();
        
        try {
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getMethods();
            
            for (Method method : methods) {
                if (method.isAnnotationPresent(CalendarDate.class)) {
                    CalendarDate calendarDate = method.getAnnotation(CalendarDate.class);
                    System.out.println("Method name: " + method.getName());
                    System.out.println("Day: " + calendarDate.day());
                    System.out.println("Month: " + calendarDate.month());
                    System.out.println("Name: " + calendarDate.name());
                    System.out.println("---------------------------------");
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }
    }
}
