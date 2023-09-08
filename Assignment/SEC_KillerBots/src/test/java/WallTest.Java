package edu.curtin.saed.assignment1.gridobjects;

import edu.curtin.saed.assignment1.gridobjects.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WallTest {

    private Wall wall1;
    private Wall wall2;

    @BeforeEach
    public void setUp() {
        // Initialize two wall instances before each test
        wall1 = new Wall(0.0, 0.0);
        wall2 = new Wall(1.0, 1.0);
    }

    @Test
    public void testInitialWallState() {
        // Test if a new wall is not damaged by default
        assertFalse(wall1.isDamaged());
    }

    @Test
    public void testDamageWall() {
        // Test if wall is damaged after calling damageWall()
        wall1.damageWall();
        assertTrue(wall1.isDamaged());
        system.out.println("Wall 1 is damaged");
    }

    @Test
    public void testAutoIncrementId() {
        // Test if wall IDs are incrementing as expected
        assertEquals(0, wall1.getId());
        assertEquals(1, wall2.getId());
    }

    @Test
    public void testGetType() {
        // Test if getType returns "wall"
        assertEquals("wall", wall1.getType());
    }
}
