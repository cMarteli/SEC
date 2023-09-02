package edu.curtin.saed.assignment1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
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
    private static final String ICON_IMAGE = "icon.png";
    public static final String APP_TITLE = "Revenge of the Killer Bots";
    public static final int SCENE_WIDTH = 800; //can be later changed to (w-1)*100
    public static final int SCENE_HEIGHT = 800; ////can be later changed to (h-1)*100
    Game game;

    public static void main(String[] args)
    {
        launch();
    }

    /** Frees memory when game is closed */
    @Override
    public void stop() {
        game.stopGame();  // Stop the game when the application stops
    }

    @Override
    public void start(Stage stage) //stage is passed in as a parameter
    {
        Image icon = new Image(ICON_IMAGE);
        stage.getIcons().add(icon); //sets icon of window
        stage.setTitle(APP_TITLE); //sets title of window
        JFXArena arena = new JFXArena(); //creates arena object
        game = new Game(arena); //Creates a new game instance

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
        arena.addListener((x, y) ->
        {
            logger.appendText("\nArena click at (" + x + "," + y + ")");
            game.stopGame();
            //game.updateScreen();
        });

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);

        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);

        Scene scene = new Scene(contentPane, SCENE_WIDTH, SCENE_HEIGHT);

        stage.setFullScreen(true); //sets window to fullscreen
        stage.setScene(scene);
        stage.show();

        game.initGame();

        /* Auto refresh the screen */
        // Create a new AnimationTimer
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 40_000_000) { // 25 times per second is every 40 ms, which is 40,000,000 nanoseconds
                    game.updateScreen();
                    lastUpdate = now;
                }
            }
        };

        // Start the AnimationTimer
        timer.start();
    }

}
