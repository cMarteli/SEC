
/**
 * ApiPractice.java
 *
 */
import java.lang.reflect.*;

public class ShowMethods {
    public static void main(String args[]) {
        try {
            Class c = Class.forName(args[0]);
            // Method m[] = c.getDeclaredMethods();
            // for (int i = 0; i < m.length; i++)
            // System.out.println(m[i].toString());
            Method m = c.getMethod("getId");
        } catch (Throwable e) {
            System.err.println(e);
        }
    }
}
