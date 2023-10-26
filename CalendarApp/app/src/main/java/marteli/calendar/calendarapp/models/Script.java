/**
 * Script.java
 * Holds script content
 */

package marteli.calendar.calendarapp.models;

import java.util.List;

public class Script {
    private List<String> content;

    public Script(List<String> content) {
        this.content = content;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String line : content) {
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }
}
