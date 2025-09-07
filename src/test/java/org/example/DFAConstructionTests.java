package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.HashSet;

/**
 * Test class for DFA construction and basic properties.
 * Tests DFA creation, state management, and basic functionality.
 */
public class DFAConstructionTests extends BaseAutomatonTest {
    
    @Test
    public void testDefaultConstructor() {
        DFA dfa = new DFA();
        
        assertNotNull(dfa, "DFA should not be null");
        // Test that empty DFA doesn't accept any string
        assertFalse(dfa.accepts(""), "Empty DFA should not accept empty string");
        assertFalse(dfa.accepts("a"), "Empty DFA should not accept any string");
    }
    
    @Test
    public void testConstructorWithStartAndAcceptingStates() {
        Set<String> acceptingStates = stateSet("q1", "q2");
        DFA dfa = new DFA("q0", acceptingStates);
        
        assertNotNull(dfa, "DFA should not be null");
        // Test that DFA with start state but no transitions doesn't accept any string
        assertFalse(dfa.accepts(""), "DFA with no transitions should not accept empty string");
        assertFalse(dfa.accepts("a"), "DFA with no transitions should not accept any string");
    }
    
    @Test
    public void testConstructorWithNullStartState() {
        Set<String> acceptingStates = stateSet("q1");
        DFA dfa = new DFA(null, acceptingStates);
        
        assertNotNull(dfa, "DFA should not be null");
        assertFalse(dfa.accepts(""), "DFA with null start state should not accept any string");
    }
    
    @Test
    public void testConstructorWithEmptyAcceptingStates() {
        Set<String> emptyAcceptingStates = new HashSet<>();
        DFA dfa = new DFA("q0", emptyAcceptingStates);
        
        assertNotNull(dfa, "DFA should not be null");
        assertFalse(dfa.accepts(""), "DFA with no accepting states should not accept any string");
    }
    
    @Test
    public void testAddStateWithValidName() {
        DFA dfa = new DFA();
        
        dfa.addState("q0", false);
        dfa.addState("q1", true);
        
        // Test that states were added by trying to use them in transitions
        dfa.addTransition("q0", 'a', "q1");
        
        // Set the start state
        DFA dfaWithStart = dfa.setStartState("q0");
        assertTrue(dfaWithStart.accepts("a"), "DFA should accept 'a' after adding states and transition");
    }
    
    @Test
    public void testAddStateWithNullName() {
        DFA dfa = new DFA();
        
        assertThrowsIllegalArgumentException(
            () -> dfa.addState(null, false),
            "State name cannot be null or empty"
        );
    }
    
    @Test
    public void testAddStateWithEmptyName() {
        DFA dfa = new DFA();
        
        assertThrowsIllegalArgumentException(
            () -> dfa.addState("", false),
            "State name cannot be null or empty"
        );
    }
    
    @Test
    public void testAddStateWithWhitespaceName() {
        DFA dfa = new DFA();
        
        assertThrowsIllegalArgumentException(
            () -> dfa.addState("   ", false),
            "State name cannot be null or empty"
        );
    }
    
    @Test
    public void testAddStateWithAcceptingFlag() {
        DFA dfa = new DFA();
        
        dfa.addState("q0", false);
        dfa.addState("q1", true);
        
        // Add transitions to test acceptance
        dfa.addTransition("q0", 'a', "q1");
        
        // Set start state
        DFA dfaWithStart = dfa.setStartState("q0");
        
        assertTrue(dfaWithStart.accepts("a"), "DFA should accept string ending in accepting state");
        assertFalse(dfaWithStart.accepts(""), "DFA should not accept empty string");
    }
    
    @Test
    public void testSetStartStateWithValidState() {
        DFA dfa = new DFA();
        dfa.addState("q0", false);
        dfa.addState("q1", true);
        dfa.addTransition("q0", 'a', "q1");
        
        DFA dfaWithStart = dfa.setStartState("q0");
        
        assertNotNull(dfaWithStart, "DFA with start state should not be null");
        assertTrue(dfaWithStart.accepts("a"), "DFA should accept 'a' with proper start state");
    }
    
    @Test
    public void testSetStartStateWithNullState() {
        DFA dfa = new DFA();
        dfa.addState("q0", false);
        
        assertThrowsIllegalArgumentException(
            () -> dfa.setStartState(null),
            "Start state must be a valid state in the DFA"
        );
    }
    
    @Test
    public void testSetStartStateWithInvalidState() {
        DFA dfa = new DFA();
        dfa.addState("q0", false);
        
        assertThrowsIllegalArgumentException(
            () -> dfa.setStartState("q1"),
            "Start state must be a valid state in the DFA"
        );
    }
    
    @Test
    public void testSetStartStateReturnsNewInstance() {
        DFA originalDFA = new DFA();
        originalDFA.addState("q0", false);
        originalDFA.addState("q1", true);
        originalDFA.addTransition("q0", 'a', "q1");
        
        DFA newDFA = originalDFA.setStartState("q0");
        
        assertNotSame(originalDFA, newDFA, "setStartState should return a new DFA instance");
        assertTrue(newDFA.accepts("a"), "New DFA should work correctly");
    }
    
    @Test
    public void testAddTransitionAddsStatesAndSymbols() {
        DFA dfa = new DFA();
        
        dfa.addTransition("q0", 'a', "q1");
        
        // Test that the transition works
        DFA dfaWithStart = dfa.setStartState("q0");
        dfaWithStart.addState("q1", true);
        
        assertTrue(dfaWithStart.accepts("a"), "DFA should accept 'a' after adding transition");
    }
    
    @Test
    public void testMultipleTransitionsFromSameState() {
        DFA dfa = new DFA();
        
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        dfa.addState("q1", true);
        dfa.addState("q2", true);
        
        DFA dfaWithStart = dfa.setStartState("q0");
        
        assertTrue(dfaWithStart.accepts("a"), "DFA should accept 'a'");
        assertTrue(dfaWithStart.accepts("b"), "DFA should accept 'b'");
        assertFalse(dfaWithStart.accepts("c"), "DFA should not accept 'c'");
    }
    
    @Test
    public void testTransitionToSelf() {
        DFA dfa = new DFA();
        
        dfa.addTransition("q0", 'a', "q0");
        dfa.addState("q0", true);
        
        DFA dfaWithStart = dfa.setStartState("q0");
        
        assertTrue(dfaWithStart.accepts("a"), "DFA should accept 'a' with self-transition");
        assertTrue(dfaWithStart.accepts("aa"), "DFA should accept 'aa' with self-transition");
    }
}
