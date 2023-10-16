import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ReflectionExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of a Java class: ");
        String className = scanner.next();

        try {
            Class<?> clazz = Class.forName(className);

            // (b) Check for the existence of a constructor that takes a single String parameter.
            Constructor<?> stringConstructor = null;
            try {
                stringConstructor = clazz.getConstructor(String.class);
                System.out.print("Enter a string to construct an object: ");
                String inputString = scanner.next();
                Object obj = stringConstructor.newInstance(inputString);
            } catch (NoSuchMethodException e) {
                System.out.println("No constructor with a single String parameter found.");
            }

            // (c) Output a list of all non-static methods within the class that take a single int as a parameter.
            Method[] methods = clazz.getMethods();
            ArrayList<Method> intMethods = new ArrayList<>();

            for (Method method : methods) {
                int modifiers = method.getModifiers();
                if (!Modifier.isStatic(modifiers) && method.getParameterCount() == 1
                        && method.getParameterTypes()[0].equals(int.class)) {
                    intMethods.add(method);
                }
            }

            for (int i = 0; i < intMethods.size(); i++) {
                System.out.println((i + 1) + ". " + intMethods.get(i).getName());
            }

            // (d) Ask the user to select a method from the list
            System.out.print("Select a method by number: ");
            int selectedMethodIndex = scanner.nextInt() - 1;

            if (selectedMethodIndex >= 0 && selectedMethodIndex < intMethods.size()) {
                Method selectedMethod = intMethods.get(selectedMethodIndex);
                System.out.print("Enter an integer value for the parameter: ");
                int intValue = scanner.nextInt();
                Object obj = stringConstructor != null ? stringConstructor.newInstance("Test") : clazz.getConstructor().newInstance();
                Object returnValue = selectedMethod.invoke(obj, intValue);

                // (e) Output the return value
                System.out.println("Return value: " + returnValue);
            } else {
                System.out.println("Invalid selection.");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
