public class TestClass {

    public TestClass() {
    }

    public TestClass(String str) {
        System.out.println("Constructor with String called: " + str);
    }

    public int testMethod(int x) {
        return x * 2;
    }

    public static void staticMethod(int x) {
        // This method should not appear in the list
    }

    public double anotherMethod(int x) {
        return x / 2.0;
    }

    public static void main(String[] args) {
        // This main method is just for demonstration; you'd normally run the ReflectionExample to test.
    }
}
