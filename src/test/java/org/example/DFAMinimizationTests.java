package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DFA minimization functionality.
 * Tests the minimizeDFA method and its various components.
 */
public class DFAMinimizationTests extends BaseAutomatonTest {
    
    @Test
    public void testMinimizeSimpleDFA() {
        // Create a simple DFA that accepts strings ending with 'a'
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        dfa.addTransition("q1", 'a', "q1");
        dfa.addTransition("q1", 'b', "q2");
        dfa.addTransition("q2", 'a', "q1");
        dfa.addTransition("q2", 'b', "q2");
        
        DFA minimizedDFA = dfa.minimizeDFA();
        
        assertNotNull(minimizedDFA, "Minimized DFA should not be null");
        
        // Test that the minimized DFA accepts the same strings
        assertDFAAccepts(minimizedDFA, "a", true);
        assertDFAAccepts(minimizedDFA, "aa", true);
        assertDFAAccepts(minimizedDFA, "ba", true);
        assertDFAAccepts(minimizedDFA, "aba", true);
        assertDFAAccepts(minimizedDFA, "b", false);
        assertDFAAccepts(minimizedDFA, "bb", false);
        assertDFAAccepts(minimizedDFA, "ab", false);
    }
    
    @Test
    public void testMinimizeDFAWithEquivalentStates() {
        // Create a DFA with equivalent states that can be merged
        DFA dfa = createSimpleDFA("q0", stateSet("q1", "q2"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        dfa.addTransition("q1", 'a', "q1");
        dfa.addTransition("q1", 'b', "q1");
        dfa.addTransition("q2", 'a', "q2");
        dfa.addTransition("q2", 'b', "q2");
        
        DFA minimizedDFA = dfa.minimizeDFA();
        
        assertNotNull(minimizedDFA, "Minimized DFA should not be null");
        
        // Test that the minimized DFA accepts the same strings
        assertDFAAccepts(minimizedDFA, "a", true);
        assertDFAAccepts(minimizedDFA, "b", true);
        assertDFAAccepts(minimizedDFA, "aa", true);
        assertDFAAccepts(minimizedDFA, "bb", true);
        assertDFAAccepts(minimizedDFA, "ab", true);
        assertDFAAccepts(minimizedDFA, "ba", true);
    }
    
    @Test
    public void testMinimizeDFAWithUnreachableStates() {
        // Create a DFA with unreachable states
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q2", 'b', "q3"); // Unreachable states
        dfa.addState("q2", false);
        dfa.addState("q3", true);
        
        DFA minimizedDFA = dfa.minimizeDFA();
        
        assertNotNull(minimizedDFA, "Minimized DFA should not be null");
        
        // Test that unreachable states are removed
        assertDFAAccepts(minimizedDFA, "a", true);
        assertDFAAccepts(minimizedDFA, "b", false);
    }
    
    @Test
    public void testMinimizeDFAWithDeadStates() {
        // Create a DFA with dead states (states that can't reach accepting states)
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        dfa.addTransition("q2", 'b', "q2"); // Dead state - can't reach accepting state
        
        DFA minimizedDFA = dfa.minimizeDFA();
        
        assertNotNull(minimizedDFA, "Minimized DFA should not be null");
        
        // Test that dead states are removed
        assertDFAAccepts(minimizedDFA, "a", true);
        assertDFAAccepts(minimizedDFA, "b", false);
        assertDFAAccepts(minimizedDFA, "bb", false);
    }
    
    @Test
    public void testMinimizeAlreadyMinimalDFA() {
        // Create a DFA that is already minimal
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        dfa.addTransition("q1", 'a', "q1");
        dfa.addTransition("q1", 'b', "q2");
        dfa.addTransition("q2", 'a', "q1");
        dfa.addTransition("q2", 'b', "q2");
        
        DFA minimizedDFA = dfa.minimizeDFA();
        
        assertNotNull(minimizedDFA, "Minimized DFA should not be null");
        
        // Test that the behavior is preserved
        assertDFAAccepts(minimizedDFA, "a", true);
        assertDFAAccepts(minimizedDFA, "aa", true);
        assertDFAAccepts(minimizedDFA, "ba", true);
        assertDFAAccepts(minimizedDFA, "b", false);
        assertDFAAccepts(minimizedDFA, "bb", false);
    }
    
    @Test
    public void testMinimizeDFAWithEmptyLanguage() {
        // Create a DFA that accepts no strings
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'a', "q2");
        dfa.addTransition("q2", 'a', "q2");
        
        DFA minimizedDFA = dfa.minimizeDFA();
        
        assertNotNull(minimizedDFA, "Minimized DFA should not be null");
        
        // Test that it still accepts no strings
        assertDFAAccepts(minimizedDFA, "", false);
        assertDFAAccepts(minimizedDFA, "a", false);
        assertDFAAccepts(minimizedDFA, "aa", false);
    }
    
    @Test
    public void testMinimizeDFAWithUniversalLanguage() {
        // Create a DFA that accepts all strings
        DFA dfa = createSimpleDFA("q0", stateSet("q0"));
        dfa.addTransition("q0", 'a', "q0");
        dfa.addTransition("q0", 'b', "q0");
        
        DFA minimizedDFA = dfa.minimizeDFA();
        
        assertNotNull(minimizedDFA, "Minimized DFA should not be null");
        
        // Test that it still accepts all strings
        assertDFAAccepts(minimizedDFA, "", true);
        assertDFAAccepts(minimizedDFA, "a", true);
        assertDFAAccepts(minimizedDFA, "b", true);
        assertDFAAccepts(minimizedDFA, "ab", true);
        assertDFAAccepts(minimizedDFA, "ba", true);
    }
    
    @Test
    public void testMinimizeDFAWithComplexEquivalentStates() {
        // Create a DFA with complex equivalent states
        DFA dfa = createSimpleDFA("q0", stateSet("q3", "q4"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        dfa.addTransition("q1", 'a', "q3");
        dfa.addTransition("q1", 'b', "q4");
        dfa.addTransition("q2", 'a', "q3");
        dfa.addTransition("q2", 'b', "q4");
        dfa.addTransition("q3", 'a', "q3");
        dfa.addTransition("q3", 'b', "q4");
        dfa.addTransition("q4", 'a', "q3");
        dfa.addTransition("q4", 'b', "q4");
        
        DFA minimizedDFA = dfa.minimizeDFA();
        
        assertNotNull(minimizedDFA, "Minimized DFA should not be null");
        
        // Test that the minimized DFA accepts the same strings
        assertDFAAccepts(minimizedDFA, "aa", true);
        assertDFAAccepts(minimizedDFA, "ab", true);
        assertDFAAccepts(minimizedDFA, "ba", true);
        assertDFAAccepts(minimizedDFA, "bb", true);
        assertDFAAccepts(minimizedDFA, "a", false);
        assertDFAAccepts(minimizedDFA, "b", false);
    }
    
    @Test
    public void testMinimizeDFAWithSingleState() {
        // Create a DFA with a single state
        DFA dfa = createSimpleDFA("q0", stateSet("q0"));
        
        DFA minimizedDFA = dfa.minimizeDFA();
        
        assertNotNull(minimizedDFA, "Minimized DFA should not be null");
        
        // Test that it still works
        assertDFAAccepts(minimizedDFA, "", true);
        assertDFAAccepts(minimizedDFA, "a", false);
    }
    
    @Test
    public void testMinimizeDFAWithNoTransitions() {
        // Create a DFA with no transitions
        DFA dfa = createSimpleDFA("q0", stateSet("q0"));
        
        DFA minimizedDFA = dfa.minimizeDFA();
        
        assertNotNull(minimizedDFA, "Minimized DFA should not be null");
        
        // Test that it still works
        assertDFAAccepts(minimizedDFA, "", true);
        assertDFAAccepts(minimizedDFA, "a", false);
    }
    
    @Test
    public void testMinimizeDFAWithMultipleAcceptingStates() {
        // Create a DFA with multiple accepting states
        DFA dfa = createSimpleDFA("q0", stateSet("q1", "q2"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        dfa.addTransition("q1", 'a', "q1");
        dfa.addTransition("q1", 'b', "q1");
        dfa.addTransition("q2", 'a', "q2");
        dfa.addTransition("q2", 'b', "q2");
        
        DFA minimizedDFA = dfa.minimizeDFA();
        
        assertNotNull(minimizedDFA, "Minimized DFA should not be null");
        
        // Test that the minimized DFA accepts the same strings
        assertDFAAccepts(minimizedDFA, "a", true);
        assertDFAAccepts(minimizedDFA, "b", true);
        assertDFAAccepts(minimizedDFA, "aa", true);
        assertDFAAccepts(minimizedDFA, "bb", true);
        assertDFAAccepts(minimizedDFA, "ab", true);
        assertDFAAccepts(minimizedDFA, "ba", true);
    }
    
    @Test
    public void testMinimizeDFAWithComplexLanguage() {
        // Create a DFA that accepts strings with even number of 'a's
        DFA dfa = createSimpleDFA("q0", stateSet("q0"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q0");
        dfa.addTransition("q1", 'a', "q0");
        dfa.addTransition("q1", 'b', "q1");
        
        DFA minimizedDFA = dfa.minimizeDFA();
        
        assertNotNull(minimizedDFA, "Minimized DFA should not be null");
        
        // Test that the minimized DFA accepts the same strings
        assertDFAAccepts(minimizedDFA, "", true);
        assertDFAAccepts(minimizedDFA, "b", true);
        assertDFAAccepts(minimizedDFA, "bb", true);
        assertDFAAccepts(minimizedDFA, "aa", true);
        assertDFAAccepts(minimizedDFA, "aabb", true);
        assertDFAAccepts(minimizedDFA, "a", false);
        assertDFAAccepts(minimizedDFA, "aaa", false);
        assertDFAAccepts(minimizedDFA, "ab", false);
    }
}
