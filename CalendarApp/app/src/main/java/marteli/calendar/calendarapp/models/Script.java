/**
 * Script.java
 * Holds script content
 */

package marteli.calendar.calendarapp.models;

public class Script {
    private String content;

    public Script(String c) {
        content = c;
    }

    // Getters and setters
    public String getContent() {
        return content;
    }

    public void setContent(String c) {
        content = c;
    }

    @Override
    public String toString() {
        return "Script{" +
                "content='" + content + '\'' +
                '}';
    }
}
