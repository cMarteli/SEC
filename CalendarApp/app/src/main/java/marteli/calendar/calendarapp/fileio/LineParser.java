package marteli.calendar.calendarapp.fileio;

import java.util.List;

public interface LineParser<T> {
    List<T> parseLines(List<String> lines);
}
