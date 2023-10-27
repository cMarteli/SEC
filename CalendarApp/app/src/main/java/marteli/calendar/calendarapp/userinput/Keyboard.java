/**
 * Utility class for scanner methods
 * Used to so scanner doesn't have to be constantly be instantiated
 * and closed
 * @author Victor Marteli (19598552)
 */
package marteli.calendar.calendarapp.userinput;

import java.util.Scanner;

public class Keyboard {

    private static final Scanner KBD = new Scanner(System.in);

    private Keyboard() {
    } // empty constructor to prevent instantiation

    public static String next() {
        return KBD.next();
    }

    public static double nextDouble() {
        return KBD.nextDouble();
    }

    public static int nextInt() {
        return KBD.nextInt();
    }

    public static String nextLine() {
        return KBD.nextLine();
    }

    public static long nextLong() {
        return KBD.nextLong();
    }

    public static void close() {
        KBD.close();
    }

}
