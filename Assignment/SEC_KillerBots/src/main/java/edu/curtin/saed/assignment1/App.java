/**
 * App.java
 * Main class of KillerBots Game
 * Some java code adapted from:
 * Software Engineering Concepts JavaFXDemo by David Cooper
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1;

import java.util.Scanner;

import edu.curtin.saed.assignment1.JFX.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application implements BotSpawnListener {// TODO: add the listener to arena class?

    public static final String APP_TITLE = "Revenge of the Killer Bots";
    public static final int DIMENSIONX = 9; // TODO: Hardcoded
    public static final int DIMENSIONY = 9; // TODO: Hardcoded
    private TextArea logger;
    Game game;
    Grid grid;
    public static boolean isFullScreen = false;

    // Create a new AnimationTimer
    AnimationTimer timer = new AnimationTimer() {
        private long lastUpdate = 0;

        @Override
        public void handle(long now) {
            if (now - lastUpdate >= 40_000_000) { // 25 times per second is every 40 ms, which
                                                  // is 40,000,000 nanoseconds
                game.updateScreen();
                lastUpdate = now;
            }
        }
    };

    public static void main(String[] args) {
        launch();
    }

    /** Frees memory when game is closed */
    @Override
    public void stop() {
        System.out.println("Closing game");// DEBUG
        game.stopGame(); // Stop the game when the application stops
    }

    @Override
    public void start(Stage stage) // stage is passed in as a parameter
    {
        Image icon = new Image(Graphics.ICON_IMAGE);
        stage.getIcons().add(icon); // sets tray icon
        stage.setTitle(APP_TITLE); // sets title of window
        JFXArena arena = new JFXArena(); // creates arena object

        grid = new Grid(DIMENSIONX, DIMENSIONY); // creates grid object
        game = new Game(arena, grid); // Creates a new game instance

        ToolBar toolbar = new ToolBar();
        // Button btn1 = new Button("My Button 1");
        // Button btn2 = new Button("My Button 2");
        Label label = new Label("Score: 999");
        // toolbar.getItems().addAll(btn1, btn2, label);
        toolbar.getItems().addAll(label);

        // btn1.setOnAction((event) ->
        // {
        // System.out.println("Button 1 pressed");
        // });

        logger = new TextArea();
        arena.addListener((x, y) -> {
            logger.appendText("\nArena click at (" + x + "," + y + ")");
        });
        arena.setBotSpawnListener(this);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);

        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);

        // Scene scene = new Scene(contentPane, SCENE_WIDTH, SCENE_HEIGHT);
        Scene scene = new Scene(contentPane, (DIMENSIONX - 1) * 100, (DIMENSIONY - 1) * 100);

        // stage.setFullScreen(true); // sets window to fullscreen
        stage.setScene(scene);
        stage.show();

        game.initGame(); // starts game here

        /* Auto refresh the screen */

        // Start the AnimationTimer
        timer.start();
    }

    @Override
    public void onBotSpawn(int x, int y) {
        logger.appendText("\nNew bot spawned at (" + x + "," + y + ")");
    }

}
