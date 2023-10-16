package scriptrunner;

import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the filename of the Python script: ");
        String filename = scanner.nextLine();

        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(filename)), "UTF-8");
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            return;
        }

        PythonInterpreter interpreter = new PythonInterpreter();

        // Initialize the Java objects and APIs you'll use in the Python script
        Canvas canvas = new Canvas();
        interpreter.set("canvas", canvas);

        try {
            interpreter.exec(content);
        } catch (PyException e) {
            e.printStackTrace();
        }
    }
}
