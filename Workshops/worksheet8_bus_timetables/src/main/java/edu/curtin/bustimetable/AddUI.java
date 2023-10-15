package edu.curtin.bustimetable;

import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Manages the dialog box for adding new timetable entries. The user enters five
 * details: the
 * route ID, the 'from' location, the destination, the departure time, and the
 * duration of the
 * trip. All of these are dynamically validated.
 */
public class AddUI {
    private static final int SPACING = 8;
    private static final String ERROR_STYLE = "-fx-background-color: khaki;";

    private ObservableList<TimetableEntry> entries;
    private Dialog<TimetableEntry> dialog = null;
    private TextField routeIdField = new TextField();
    private TextField fromField = new TextField();
    private TextField destinationField = new TextField();
    private TextField departureTimeField = new TextField();
    private TextField durationField = new TextField();
    private List<TextField> fields = List.of(routeIdField, fromField, destinationField, departureTimeField,
            durationField);

    public AddUI(ObservableList<TimetableEntry> entries) {
        this.entries = entries;
    }

    /**
     * Shows the dialog, and (if the user clicks 'ok') adds a new TimetableEntry
     * object to the list.
     */
    public void addEntry() {
        if (dialog == null) {
            var content = new GridPane();
            dialog = new Dialog<>();
            dialog.setTitle(Labels.timetableEntryTxt);
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.setResultConverter(this::buildEntry);
            content.setHgap(SPACING);
            content.setVgap(SPACING);
            content.add(new Label(Labels.routeLabel), 0, 0);
            content.add(new Label(Labels.fromLabel), 0, 1);
            content.add(new Label(Labels.destinationLabel), 0, 2);
            content.add(new Label(Labels.departLabel), 0, 3);
            content.add(new Label(Labels.durationLabel), 0, 4);
            content.add(routeIdField, 1, 0);
            content.add(fromField, 1, 1);
            content.add(destinationField, 1, 2);
            content.add(departureTimeField, 1, 3);
            content.add(durationField, 1, 4);
            fields.forEach(f -> f.textProperty().addListener((o, v1, v2) -> validateEntry()));
        }

        validateEntry();
        var entry = dialog.showAndWait().orElse(null);
        if (entry != null) {
            entries.add(entry);
        }
    }

    /**
     * Checks whether the current field values represent a valid timetable entry. If
     * not, the 'ok'
     * button is disabled, and the field(s) with invalid values are highlighted.
     */
    private void validateEntry() {
        boolean disable = false;

        for (TextField tf : fields) {
            if (tf.getLength() == 0) {
                tf.setStyle(ERROR_STYLE);
                disable = true;
            } else {
                tf.setStyle("");
            }
        }

        try {
            LocalTime.parse(departureTimeField.getText());
        } catch (DateTimeParseException e) {
            departureTimeField.setStyle(ERROR_STYLE);
            disable = true;
        }

        int d = -1;
        try {
            d = Integer.parseInt(durationField.getText());
        } catch (NumberFormatException e) {
        } // Unparseable

        if (d < 0) // Either unparseable, or parseable but negative
        {
            durationField.setStyle(ERROR_STYLE);
            disable = true;
        }

        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(disable);
    }

    /**
     * Creates a TimetableEntry object from the field values in the dialog box.
     */
    private TimetableEntry buildEntry(ButtonType buttonType) {
        if (buttonType != ButtonType.OK) {
            return null;
        }

        return new TimetableEntry(
                routeIdField.getText(),
                fromField.getText(),
                destinationField.getText(),
                LocalTime.parse(departureTimeField.getText()),
                Duration.ofMinutes(Integer.parseInt(durationField.getText())));
    }
}
