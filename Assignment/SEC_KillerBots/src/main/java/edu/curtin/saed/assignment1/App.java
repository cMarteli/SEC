/**
 * App.java
 * Main class of KillerBots Game
 * Some java code adapted from:
 * Software Engineering Concepts JavaFXDemo by David Cooper
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1;

import edu.curtin.saed.assignment1.JFX.*;
import edu.curtin.saed.assignment1.gameLogic.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    /* Class constants */
    private static final String APP_TITLE = "Revenge of the Killer Bots";
    private static final int GRID_SIZE_X = 9;
    private static final int GRID_SIZE_Y = 9;
    private static final long UPDATE_INTERVAL = 40; // In milliseconds
    private static final long SCORE_UPDATE_INTERVAL = 1000; // In milliseconds

    /* Class variables */
    protected Game game;
    protected Grid grid;
    private TextArea logger;
    private Label scoreLbl;
    private Label queue;

    /* Animation Timer for Game Loop */
    AnimationTimer gameLoop = new AnimationTimer() {
        private long lastUpdateTime = 0;
        private long lastScoreUpdateTime = 0;

        @Override
        public void handle(long currentTimeInNano) {
            long currentTimeInMilli = currentTimeInNano / 1_000_000; // Convert to milliseconds

            /* Screen Update */
            if (game.isRunning() && (currentTimeInMilli - lastUpdateTime >= UPDATE_INTERVAL)) {
                game.updateJFX();
                lastUpdateTime = currentTimeInMilli;
            }

            /* Score Update */
            if (game.isRunning() && (currentTimeInMilli - lastScoreUpdateTime >= SCORE_UPDATE_INTERVAL)) {
                game.updateScore(10);
                scoreLbl.setText("Score: " + game.getScore());
                lastScoreUpdateTime = currentTimeInMilli;
            }
        }
    };

    public static void main(String[] args) {
        launch();
    }

    /* Stops the game and frees resources */
    @Override
    public void stop() {
        if (game.isRunning()) {
            logger.appendText("\nEnd of game\n Final Score:" + game.getScore() + "\n");
            game.stopGame();
        }
    }

    /* Application entry point */
    @Override
    public void start(Stage stage) {
        setupUI(stage);
        game.initGame();
        gameLoop.start();
    }

    /* Initializes the user interface */
    private void setupUI(Stage stage) {
        /* Window Configuration */
        Image icon = new Image(Graphics.ICON_IMAGE);
        stage.getIcons().add(icon);
        stage.setTitle(APP_TITLE);

        logger = new TextArea();
        JFXArena arena = new JFXArena(logger);
        grid = new Grid(GRID_SIZE_X, GRID_SIZE_Y);
        game = new Game(arena, grid);

        /* Toolbar */
        ToolBar toolbar = setupToolbar(stage);

        /* Window Layout */
        SplitPane splitPane = new SplitPane(arena, logger);
        BorderPane contentPane = new BorderPane(splitPane, toolbar, null, null, null);

        Scene scene = new Scene(contentPane, (GRID_SIZE_X - 1) * 100, (GRID_SIZE_Y - 1) * 100);
        stage.setScene(scene);
        stage.show();
    }

    /* Creates and returns a configured toolbar */
    private ToolBar setupToolbar(Stage stage) { // Add Stage stage parameter
        scoreLbl = new Label("Score: 0");
        queue = new Label("Walls Queued: 0");
        Button fullScreenBtn = new Button("Fullscreen");
        Button stopGameBtn = new Button("Stop Game");

        fullScreenBtn.setOnAction(event -> toggleFullscreen(stage)); // Pass stage here
        stopGameBtn.setOnAction(event -> stop());

        return new ToolBar(scoreLbl, new Separator(), queue, new Separator(), fullScreenBtn, new Separator(),
                stopGameBtn);
    }

    /* Toggles the fullscreen mode */
    private void toggleFullscreen(Stage stage) {
        stage.setFullScreen(!stage.isFullScreen());
    }
}
