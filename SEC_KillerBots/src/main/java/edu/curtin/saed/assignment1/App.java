/**
 * App.java
 * Main class of KillerBots Game
 * Some java code adapted from:
 * Software Engineering Concepts JavaFXDemo by David Cooper
 * 2023/SEC Assignment 1
 * @author Victor Marteli (19598552)
 */

package edu.curtin.saed.assignment1;

import edu.curtin.saed.assignment1.gamelogic.*;
import edu.curtin.saed.assignment1.jfx.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    /* Class constants */
    private static final String APP_TITLE = "Revenge of the Killer Bots"; // Application title
    /* Can be changed to dinamically resize the grid */
    private static final int GRID_SIZE_X = 9;
    private static final int GRID_SIZE_Y = 9;
    /* Time related values */
    private static final long UPDATE_INTERVAL = 40; // In milliseconds
    private static final long SCORE_UPDATE_INTERVAL = 1000; // In milliseconds
    private static final int POINTS_PER_SEC = 10; // Points added to score per second

    /* Class variables */
    private Game game;
    private Grid grid;
    private JFXArena arena;
    private TextArea logger;
    private Label scoreLbl;
    private Label queueLbl;

    /* Animation Timer for Game Loop */
    private AnimationTimer gameLoop = new AnimationTimer() {
        private long lastUpdateTime = 0;
        private long lastScoreUpdateTime = 0;

        @Override
        public void handle(long currentTimeInNano) {
            long currentTimeInMilli = currentTimeInNano / 1_000_000; // Convert to milliseconds

            /* Screen Updates every 40ms (25fps) */
            if (game.isRunning() && (currentTimeInMilli - lastUpdateTime >= UPDATE_INTERVAL)) {
                game.updateJFX();
                scoreLbl.setText("Score: " + game.getScore());
                queueLbl.setText("Walls Queued: " + game.getQueueCount());
                lastUpdateTime = currentTimeInMilli;
            }

            /* Add to score every second */
            if (game.isRunning() && (currentTimeInMilli - lastScoreUpdateTime >= SCORE_UPDATE_INTERVAL)) {
                game.updateScore(POINTS_PER_SEC);
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
        System.out.println("Stopping game."); // Debug message

        if (game.isRunning()) {
            game.stopGame();
            gameLoop.stop();
            // arena.clearAndResetArena();
            grid.clearGrid();
        }
    }

    /* Application entry point */
    @Override
    public void start(Stage stage) {
        newGame();
        setupUI(stage);
        toggleFullscreen(stage); // Uncomment to start game in fullscreen
    }

    public void newGame() {
        System.out.println("Starting new game."); // Debug message

        logger = new TextArea();
        grid = new Grid(GRID_SIZE_X, GRID_SIZE_Y);
        arena = new JFXArena(grid, logger);
        game = new Game(arena, grid);
        game.initGame();
        gameLoop.start();
    }

    // public void newGameBtnClick() {
    // stop();
    // newGame();
    // }

    /* Initializes the user interface */
    private void setupUI(Stage stage) {
        /* Window Configuration */
        Image icon = new Image(Graphics.ICON_IMAGE);
        stage.getIcons().add(icon);
        stage.setTitle(APP_TITLE);

        arena.addListener(game); // adds a listener to the arena

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
        queueLbl = new Label("Walls Queued: 0");
        Button fullScreenBtn = new Button("Fullscreen");
        // Button newGameBtn = new Button("New Game");

        fullScreenBtn.setOnAction(event -> toggleFullscreen(stage)); // Pass stage here
        // newGameBtn.setOnAction(event -> newGameBtnClick());

        // return new ToolBar(scoreLbl, new Separator(), queueLbl, new Separator(),
        // fullScreenBtn, new Separator(),
        // newGameBtn);

        return new ToolBar(scoreLbl, new Separator(), queueLbl, new Separator(),
                fullScreenBtn, new Separator());
    }

    /* Toggles the fullscreen mode */
    private void toggleFullscreen(Stage stage) {
        stage.setFullScreen(!stage.isFullScreen());
    }
}
