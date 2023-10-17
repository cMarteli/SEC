import java.lang.reflect.Method;

public class CalendarDateFinder {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java CalendarDateFinder <fully_qualified_class_name>");
            return;
        }

        String className = args[0];

        try {
            Class<?> targetClass = Class.forName(className);

            Method[] methods = targetClass.getDeclaredMethods();

            for (Method method : methods) {
                CalendarDate annotation = method.getAnnotation(CalendarDate.class);
                if (annotation != null) {
                    System.out.println("Method Name: " + method.getName());
                    System.out.println("Day: " + annotation.day());
                    System.out.println("Month: " + annotation.month());
                    System.out.println("Name: " + annotation.name());
                    System.out.println();
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        }
    }
}
