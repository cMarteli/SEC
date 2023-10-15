package edu.curtin.terminalgrid;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.*;

class TerminalGridTest
{
    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private TerminalGrid terminalGrid = new TerminalGrid(new PrintStream(output), 80);

    @Test
    void test1x1Empty()
    {
        terminalGrid.print(new String[][] {{""}});
        assertEquals(
            "┌──┐\n" + 
            "│  │\n" + 
            "└──┘\n", 
            output.toString());
    }
    
    @Test
    void test2x2()
    {
        terminalGrid.print(new String[][] {{"one", "two"}, {"three", "four"}});
        assertEquals(
            "┌───────┬──────┐\n" + 
            "│ one   │ two  │\n" + 
            "├───────┼──────┤\n" + 
            "│ three │ four │\n" + 
            "└───────┴──────┘\n", 
            output.toString());
    }
    
    @Test
    void test2x1Multiline()
    {
        terminalGrid.print(new String[][] {{"one\ntwo"}, {"three\nfour"}});
        assertEquals(
            "┌───────┐\n" + 
            "│ one   │\n" + 
            "│ two   │\n" + 
            "├───────┤\n" + 
            "│ three │\n" + 
            "│ four  │\n" + 
            "└───────┘\n", 
            output.toString());
    }
    
    @Test
    void test1x2Multiline()
    {
        terminalGrid.print(new String[][] {{"one\ntwo", "three\nfour"}});
        assertEquals(
            "┌─────┬───────┐\n" + 
            "│ one │ three │\n" + 
            "│ two │ four  │\n" + 
            "└─────┴───────┘\n", 
            output.toString());
    }
    
    @Test
    void test2x1UnevenMultiline()
    {
        terminalGrid.print(new String[][] {{"one"}, {"two\nthree\nfour"}});
        assertEquals(
            "┌───────┐\n" + 
            "│ one   │\n" + 
            "├───────┤\n" + 
            "│ two   │\n" + 
            "│ three │\n" + 
            "│ four  │\n" + 
            "└───────┘\n", 
            output.toString());
    }
    
    @Test
    void test1x2UnevenMultiline()
    {
        terminalGrid.print(new String[][] {{"one", "two\nthree\nfour"}});
        assertEquals(
            "┌─────┬───────┐\n" + 
            "│ one │ two   │\n" + 
            "│     │ three │\n" + 
            "│     │ four  │\n" + 
            "└─────┴───────┘\n", 
            output.toString());
    }
    
    @Test
    void test1x1Wrapped()
    {
        terminalGrid.setTerminalWidth(7);
        terminalGrid.print(new String[][] {{"two three"}});
        assertEquals(
            "┌─────┐\n" + 
            "│ two │\n" + 
            "│ thr │\n" + 
            "│ ee  │\n" + 
            "└─────┘\n", 
            output.toString());
    }
    
    @Test
    void test1x2Wrapped2ndCol()
    {
        // It should be the second column that gets squashed, because it's wider (and still wider 
        // after being sufficiently narrowed to fit).
        terminalGrid.setTerminalWidth(15);
        terminalGrid.print(new String[][] {{"one", "two three four"}});
        assertEquals(
            "┌─────┬───────┐\n" + 
            "│ one │ two   │\n" + 
            "│     │ three │\n" + 
            "│     │ four  │\n" + 
            "└─────┴───────┘\n", 
            output.toString());
    }

    @Test
    void test1x2Wrapped1stCol()
    {
        // It should be the first column that gets squashed this time.
        terminalGrid.setTerminalWidth(16);
        terminalGrid.print(new String[][] {{"one two three", "four"}});
        assertEquals(
            "┌───────┬──────┐\n" + 
            "│ one   │ four │\n" + 
            "│ two   │      │\n" + 
            "│ three │      │\n" + 
            "└───────┴──────┘\n", 
            output.toString());
    }

    @Test
    void test1x2WrappedAllCols()
    {
        // Both columns should end up squashed to the same width (because squashing only the 
        // initially-wider column would make it narrower than the other column).
        terminalGrid.setTerminalWidth(17);
        terminalGrid.print(new String[][] {{"one two", "three four"}});
        assertEquals(
            "┌───────┬───────┐\n" +
            "│ one   │ three │\n" + 
            "│ two   │ four  │\n" +
            "└───────┴───────┘\n", 
            output.toString());
    }

    @Test
    void test2x3WrappedAllCols()
    {
        terminalGrid.setTerminalWidth(25);
        terminalGrid.print(new String[][] {{"one two", "three four", "five six"}, {"seven eight", "nine ten", "zero one"}});
        
        assertEquals(
            "┌───────┬───────┬───────┐\n" +
            "│ one   │ three │ five  │\n" +
            "│ two   │ four  │ six   │\n" + 
            "├───────┼───────┼───────┤\n" +
            "│ seven │ nine  │ zero  │\n" +
            "│ eight │ ten   │ one   │\n" +
            "└───────┴───────┴───────┘\n", 
            output.toString());
    }
    
    @Test
    void test2x3Headings()
    {
        terminalGrid.print(
            new String[][] {{"one", "two", "three"}, {"four", "five", "six"}},
            new String[] {"row 1", "row\n2"}, 
            new String[] {"column\nA", "col B", "C"});
        
        assertEquals("\n" + 
            "        column   col B   C      \n" +
            "        A                       \n" +
            "      ┌────────┬───────┬───────┐\n" +
            "row 1 │ one    │ two   │ three │\n" +
            "      ├────────┼───────┼───────┤\n" +
            "row   │ four   │ five  │ six   │\n" +
            "2     │        │       │       │\n" +
            "      └────────┴───────┴───────┘\n", 
            "\n" + output.toString());
    }

    @Test
    void test2x2NestedLists()
    {
        terminalGrid.print(List.of(List.of("one", "two"), List.of("three", "four")));
        assertEquals(
            "┌───────┬──────┐\n" + 
            "│ one   │ two  │\n" + 
            "├───────┼──────┤\n" + 
            "│ three │ four │\n" + 
            "└───────┴──────┘\n", 
            output.toString());
    }

    @Test
    void test2x2NestedListsHeadings()
    {
        terminalGrid.print(List.of(List.of("one", "two"), List.of("three", "four")),
                           List.of("row 1", "row 2"),
                           List.of("A", "B"));
        assertEquals(
            "        A       B     \n" +
            "      ┌───────┬──────┐\n" + 
            "row 1 │ one   │ two  │\n" + 
            "      ├───────┼──────┤\n" + 
            "row 2 │ three │ four │\n" + 
            "      └───────┴──────┘\n", 
            output.toString());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 12, 13})
    void test3x2SquashLimit(int terminalWidth)
    {
        // TerminalGrid should refuse to squash columns down below one character wide, even if this 
        // doesn't fit within the terminal width.
        terminalGrid.setTerminalWidth(terminalWidth);
        terminalGrid.print(new String[][] {{"one", "two", "three"}, {"four", "five", "six"}});
        assertEquals(
            "┌───┬───┬───┐\n" + 
            "│ o │ t │ t │\n" + 
            "│ n │ w │ h │\n" + 
            "│ e │ o │ r │\n" + 
            "│   │   │ e │\n" + 
            "│   │   │ e │\n" + 
            "├───┼───┼───┤\n" + 
            "│ f │ f │ s │\n" + 
            "│ o │ i │ i │\n" + 
            "│ u │ v │ x │\n" + 
            "│ r │ e │   │\n" + 
            "└───┴───┴───┘\n", 
            output.toString());
    }
}
