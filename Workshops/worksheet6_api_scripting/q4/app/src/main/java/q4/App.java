package q4;

import org.python.util.PythonInterpreter;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the filename of the Python script to run:");
        String filename = scanner.nextLine();

        // Initialize the Canvas and PythonInterpreter
        Canvas canvas = new Canvas();
        try (PythonInterpreter pyInterp = new PythonInterpreter()) {
            // Make the Canvas object accessible from Python
            pyInterp.set("canvas", canvas);

            // Execute the script
            pyInterp.execfile(filename);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while running the script.");
        }
    }
}
