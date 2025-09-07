package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DFA visualization functionality.
 * Tests the toDot method for generating DOT representation.
 */
public class DFAVisualizationTests extends BaseAutomatonTest {
    
    @Test
    public void testToDotWithEmptyDFA() {
        DFA dfa = new DFA();
        
        String dot = dfa.toDot();
        
        assertNotNull(dot, "DOT representation should not be null");
        assertTrue(dot.contains("digraph DFA"), "DOT should contain digraph declaration");
        assertTrue(dot.contains("rankdir=LR"), "DOT should contain rankdir=LR");
        assertTrue(dot.contains("size=\"8,5\""), "DOT should contain size specification");
        assertTrue(dot.endsWith("}\n"), "DOT should end with closing brace and newline");
    }
    
    @Test
    public void testToDotWithSingleState() {
        DFA dfa = createSimpleDFA("q0", stateSet("q0"));
        
        String dot = dfa.toDot();
        
        assertNotNull(dot, "DOT representation should not be null");
        assertTrue(dot.contains("digraph DFA"), "DOT should contain digraph declaration");
        assertTrue(dot.contains("\"q0\" [shape=doublecircle]"), "DOT should show q0 as accepting state");
        assertTrue(dot.contains("start -> \"q0\""), "DOT should show start arrow to q0");
    }
    
    @Test
    public void testToDotWithMultipleStates() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q1", 'b', "q2");
        dfa.addState("q2", false);
        
        String dot = dfa.toDot();
        
        assertNotNull(dot, "DOT representation should not be null");
        assertTrue(dot.contains("digraph DFA"), "DOT should contain digraph declaration");
        assertTrue(dot.contains("\"q0\" [shape=circle]"), "DOT should show q0 as regular state");
        assertTrue(dot.contains("\"q1\" [shape=doublecircle]"), "DOT should show q1 as accepting state");
        assertTrue(dot.contains("\"q2\" [shape=circle]"), "DOT should show q2 as regular state");
        assertTrue(dot.contains("start -> \"q0\""), "DOT should show start arrow to q0");
    }
    
    @Test
    public void testToDotWithTransitions() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        dfa.addState("q2", false);
        
        String dot = dfa.toDot();
        
        assertNotNull(dot, "DOT representation should not be null");
        assertTrue(dot.contains("\"q0\" -> \"q1\" [label=\"a\"]"), "DOT should show transition from q0 to q1 on 'a'");
        assertTrue(dot.contains("\"q0\" -> \"q2\" [label=\"b\"]"), "DOT should show transition from q0 to q2 on 'b'");
    }
    
    @Test
    public void testToDotWithSelfTransitions() {
        DFA dfa = createSimpleDFA("q0", stateSet("q0"));
        dfa.addTransition("q0", 'a', "q0");
        
        String dot = dfa.toDot();
        
        assertNotNull(dot, "DOT representation should not be null");
        assertTrue(dot.contains("\"q0\" -> \"q0\" [label=\"a\"]"), "DOT should show self-transition on q0");
    }
    
    @Test
    public void testToDotWithMultipleAcceptingStates() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1", "q2"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        
        String dot = dfa.toDot();
        
        assertNotNull(dot, "DOT representation should not be null");
        assertTrue(dot.contains("\"q1\" [shape=doublecircle]"), "DOT should show q1 as accepting state");
        assertTrue(dot.contains("\"q2\" [shape=doublecircle]"), "DOT should show q2 as accepting state");
    }
    
    @Test
    public void testToDotWithSpecialCharacters() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", '!', "q1");
        dfa.addTransition("q0", '@', "q2");
        dfa.addState("q2", false);
        
        String dot = dfa.toDot();
        
        assertNotNull(dot, "DOT representation should not be null");
        assertTrue(dot.contains("\"q0\" -> \"q1\" [label=\"!\"]"), "DOT should show transition with '!'");
        assertTrue(dot.contains("\"q0\" -> \"q2\" [label=\"@\"]"), "DOT should show transition with '@'");
    }
    
    @Test
    public void testToDotWithNumericSymbols() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", '0', "q1");
        dfa.addTransition("q0", '1', "q2");
        dfa.addState("q2", false);
        
        String dot = dfa.toDot();
        
        assertNotNull(dot, "DOT representation should not be null");
        assertTrue(dot.contains("\"q0\" -> \"q1\" [label=\"0\"]"), "DOT should show transition with '0'");
        assertTrue(dot.contains("\"q0\" -> \"q2\" [label=\"1\"]"), "DOT should show transition with '1'");
    }
    
    @Test
    public void testToDotWithWhitespaceSymbols() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", ' ', "q1");
        dfa.addTransition("q0", '\t', "q2");
        dfa.addState("q2", false);
        
        String dot = dfa.toDot();
        
        assertNotNull(dot, "DOT representation should not be null");
        assertTrue(dot.contains("\"q0\" -> \"q1\" [label=\" \"]"), "DOT should show transition with space");
        assertTrue(dot.contains("\"q0\" -> \"q2\" [label=\"\\t\"]") || dot.contains("\"q0\" -> \"q2\" [label=\"\t\"]"), "DOT should show transition with tab");
    }
    
    @Test
    public void testToDotWithComplexDFA() {
        // Create a complex DFA that accepts strings ending with "ab" or "ba"
        DFA dfa = createSimpleDFA("q0", stateSet("q3", "q4"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        dfa.addTransition("q1", 'a', "q1");
        dfa.addTransition("q1", 'b', "q3");
        dfa.addTransition("q2", 'a', "q4");
        dfa.addTransition("q2", 'b', "q2");
        dfa.addTransition("q3", 'a', "q1");
        dfa.addTransition("q3", 'b', "q2");
        dfa.addTransition("q4", 'a', "q1");
        dfa.addTransition("q4", 'b', "q2");
        
        String dot = dfa.toDot();
        
        assertNotNull(dot, "DOT representation should not be null");
        assertTrue(dot.contains("digraph DFA"), "DOT should contain digraph declaration");
        assertTrue(dot.contains("start -> \"q0\""), "DOT should show start arrow to q0");
        
        // Check that all states are present
        assertTrue(dot.contains("\"q0\" [shape=circle]"), "DOT should show q0 as regular state");
        assertTrue(dot.contains("\"q1\" [shape=circle]"), "DOT should show q1 as regular state");
        assertTrue(dot.contains("\"q2\" [shape=circle]"), "DOT should show q2 as regular state");
        assertTrue(dot.contains("\"q3\" [shape=doublecircle]"), "DOT should show q3 as accepting state");
        assertTrue(dot.contains("\"q4\" [shape=doublecircle]"), "DOT should show q4 as accepting state");
        
        // Check that all transitions are present
        assertTrue(dot.contains("\"q0\" -> \"q1\" [label=\"a\"]"), "DOT should show q0->q1 transition");
        assertTrue(dot.contains("\"q0\" -> \"q2\" [label=\"b\"]"), "DOT should show q0->q2 transition");
        assertTrue(dot.contains("\"q1\" -> \"q1\" [label=\"a\"]"), "DOT should show q1->q1 transition");
        assertTrue(dot.contains("\"q1\" -> \"q3\" [label=\"b\"]"), "DOT should show q1->q3 transition");
        assertTrue(dot.contains("\"q2\" -> \"q4\" [label=\"a\"]"), "DOT should show q2->q4 transition");
        assertTrue(dot.contains("\"q2\" -> \"q2\" [label=\"b\"]"), "DOT should show q2->q2 transition");
    }
    
    @Test
    public void testToDotWithNullStartState() {
        DFA dfa = new DFA();
        dfa.addState("q0", true);
        dfa.addState("q1", false);
        dfa.addTransition("q0", 'a', "q1");
        
        String dot = dfa.toDot();
        
        assertNotNull(dot, "DOT representation should not be null");
        assertTrue(dot.contains("digraph DFA"), "DOT should contain digraph declaration");
        assertFalse(dot.contains("start ->"), "DOT should not contain start arrow when start state is null");
        assertTrue(dot.contains("\"q0\" [shape=doublecircle]"), "DOT should show q0 as accepting state");
        assertTrue(dot.contains("\"q1\" [shape=circle]"), "DOT should show q1 as regular state");
    }
    
    @Test
    public void testToDotFormatting() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'a', "q1");
        
        String dot = dfa.toDot();
        
        assertNotNull(dot, "DOT representation should not be null");
        
        // Check that the DOT is properly formatted
        String[] lines = dot.split("\n");
        assertTrue(lines.length > 0, "DOT should have multiple lines");
        
        // Check that each line is properly indented
        for (String line : lines) {
            if (line.trim().startsWith("\"") || line.trim().startsWith("start")) {
                assertTrue(line.startsWith("  "), "State and transition lines should be indented: " + line);
            }
        }
    }
    
    @Test
    public void testToDotWithUnicodeCharacters() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'ñ', "q1");
        dfa.addTransition("q0", '中', "q2");
        dfa.addState("q2", false);
        
        String dot = dfa.toDot();
        
        assertNotNull(dot, "DOT representation should not be null");
        assertTrue(dot.contains("\"q0\" -> \"q1\" [label=\"ñ\"]"), "DOT should show transition with 'ñ'");
        assertTrue(dot.contains("\"q0\" -> \"q2\" [label=\"中\"]"), "DOT should show transition with '中'");
    }
}
