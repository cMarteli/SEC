package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/*
 * KillerBots Game
 * @Author Victor Marteli - 19598552
 * Some java code was taken from the following sources:
 * Software Engineering Concepts JavaFXDemo - David Cooper
 *
 */

public class App extends Application
{
    public static final String APP_TITLE = "Revenge of the Killer Bots";
    public static final int SCENE_WIDTH = 800;
    public static final int SCENE_HEIGHT = 800;
    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void start(Stage stage)
    {
        stage.setTitle(APP_TITLE); //sets title of window
        JFXArena arena = new JFXArena(); //creates arena object
        arena.addListener((x, y) ->
        {
            System.out.println("Arena click at (" + x + "," + y + ")");
        });

        ToolBar toolbar = new ToolBar();
//         Button btn1 = new Button("My Button 1");
//         Button btn2 = new Button("My Button 2");
        Label label = new Label("Score: 999");
//         toolbar.getItems().addAll(btn1, btn2, label);
        toolbar.getItems().addAll(label);

//         btn1.setOnAction((event) ->
//         {
//             System.out.println("Button 1 pressed");
//         });

        TextArea logger = new TextArea();
        logger.appendText("Hello\n");
        logger.appendText("World\n");

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);

        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);

        Scene scene = new Scene(contentPane, SCENE_WIDTH, SCENE_HEIGHT, Color.BLACK);
        stage.setScene(scene);
        stage.show();
    }
}
