/**
 * ArenaListener.java
 * 2023/SEC Assignment 1
 * Code adapted from: Software Engineering Concepts JavaFXDemo by David Cooper
 * Modified by Victor Marteli in September, 2023.
 */
package edu.curtin.saed.assignment1.jfx;

/**
 * Represents an event handler for when the arena is clicked.
 */
public interface ArenaListener {
    void squareClicked(int x, int y);
}
