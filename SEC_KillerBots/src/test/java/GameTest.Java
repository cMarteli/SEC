import edu.curtin.saed.assignment1.gamelogic.Game;
import edu.curtin.saed.assignment1.Grid;
import edu.curtin.saed.assignment1.jfx.JFXArena;
import edu.curtin.saed.assignment1.gridobjects.Wall;
import java.awt.Point;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GameTest {

    @Mock
    private Grid mockGrid;

    @Mock
    private JFXArena mockArena;

    @InjectMocks
    private Game game;

    @BeforeEach
    void setUp() {
        // Initialize any shared setup steps here
    }

    /**
     * Test if the game initializes correctly
     */
    @Test
    void testGameInitialization() {
        // Run the initGame method to set the running flag to true
        game.initGame();
        assertTrue(game.isRunning());
    }

    /**
     * Test if the game stops correctly
     */
    @Test
    void testGameStop() {
        game.stopGame();
        assertFalse(game.isRunning());
    }

    /**
     * Test squareClicked method to ensure walls are scheduled correctly
     */
    @Test
    void testSquareClicked() {
        when(mockGrid.isCellEmpty(anyInt(), anyInt())).thenReturn(true);

        game.squareClicked(1, 1);
    }

    /**
     * Test wall collision handling
     */
    @Test
    void testWallCollision() {
        Wall wall = mock(Wall.class);
        when(wall.isDamaged()).thenReturn(true);
        when(mockGrid.getGridObj(any(Point.class))).thenReturn(wall);

        game.wallCollision(new Point(1, 1));
    }

    /**
     * Test score updating functionality
     */
    @Test
    void testUpdateScore() {
        AtomicInteger score = new AtomicInteger(0);
        int delta = 10;

        score.addAndGet(delta);
        game.updateScore(delta);

        assertEquals(score.get(), game.getScore());
    }
}
