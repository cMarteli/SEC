package marteli.calendar.calendarapp.scripting;

import org.python.core.PyObject;
import marteli.calendar.calendarapp.models.Script;

public interface ScriptAPI {

    public void runScript(Script script);

    public PyObject getVariable(String variableName);

    public void setVariable(String variableName, PyObject value);

    public void deleteVariable(String variableName);

}
