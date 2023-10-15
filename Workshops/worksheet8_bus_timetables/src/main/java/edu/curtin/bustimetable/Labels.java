package edu.curtin.bustimetable;

import java.util.Locale;
import java.util.ResourceBundle;

// Singleton class that uses the given locale to create a ResourceBundle
public final class Labels {

    private static Labels single_instance = null;
    private ResourceBundle bundle;

    // Labels
    public static String routeLabel;
    public static String fromLabel;
    public static String destinationLabel;
    public static String departLabel;
    public static String arrivalLabel;
    public static String durationLabel;
    public static String encodingLabel;

    // buttons
    public static String addButton;
    public static String saveButton;
    public static String loadButton;

    public static String selectEncodingTxt;
    public static String timetableEntryTxt;
    public static String searchButton;

    // Private constructor
    private Labels() {
        bundle = ResourceBundle.getBundle("bundle", Locale.getDefault());

        // Initialize Tab fields after bundle is set
        routeLabel = bundle.getString("route_tab");
        fromLabel = bundle.getString("from_tab");
        destinationLabel = bundle.getString("dest_tab");
        departLabel = bundle.getString("depart_tab");
        arrivalLabel = bundle.getString("arrival_tab");
        durationLabel = bundle.getString("duration_tab");
        encodingLabel = bundle.getString("encoding_tab");

        addButton = bundle.getString("add_button");
        saveButton = bundle.getString("save_button");
        loadButton = bundle.getString("load_button");
        searchButton = bundle.getString("search_button");

        timetableEntryTxt = bundle.getString("timetable_entry_txt");
        selectEncodingTxt = bundle.getString("select_encoding_txt");
    }

    // Static method to create an instance of the Singleton class
    public static synchronized Labels getInstance() {
        if (single_instance == null) {
            single_instance = new Labels();
        }
        return single_instance;
    }
}
