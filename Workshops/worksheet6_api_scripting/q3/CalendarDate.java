import java.lang.annotation.*;

// Note: To work with annotations, you need to import “java.lang.annotation.*”.

// @Retention(RetentionPolicy.RUNTIME) specifies that the annotation is to
// be made available (“retained”) at runtime, so that reflection code can access
// it. This is not the default, and so specifying it will be essential in our case.

// @Target(...) specifies where an annotation may appear. By default, they
// can appear almost anywhere, but we could restrict them to methods and
// fields with this: @Target({ElementType.METHOD, ElementType.FIELD}).

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CalendarDate {
    int day();
    int month();
    String name();
}
