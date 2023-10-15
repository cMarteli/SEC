package edu.curtin.bustimetable;

import java.util.Locale;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

/**
 * Entry point for the bus timetabling app. It can be invoked with the
 * command-line parameter
 * --locale=[value].
 */
public class BusTimetableApp extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        var localeString = getParameters().getNamed().get("locale");
        if (localeString != null) { // if language is specified
            if (localeString.equals("br")) { // if language is brazilian portuguese
                Locale.setDefault(new Locale("pt", "BR"));
            }
        }
        // Get the labels for the current locale
        Labels.getInstance();
        var entries = FXCollections.<TimetableEntry>observableArrayList();
        FileIO fileIO = new FileIO();
        LoadSaveUI loadSaveUI = new LoadSaveUI(stage, entries, fileIO);
        AddUI addUI = new AddUI(entries);
        new MainUI(stage, entries, loadSaveUI, addUI).display();
    }

}
