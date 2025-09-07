package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DFA transition management.
 * Tests transition addition, modification, and behavior.
 */
public class DFATransitionTests extends BaseAutomatonTest {
    
    @Test
    public void testAddSingleTransition() {
        DFA dfa = new DFA();
        dfa.addTransition("q0", 'a', "q1");
        dfa.addState("q1", true);
        
        DFA dfaWithStart = dfa.setStartState("q0");
        
        assertDFAAccepts(dfaWithStart, "a", true);
        assertDFAAccepts(dfaWithStart, "b", false);
    }
    
    @Test
    public void testAddMultipleTransitionsFromSameState() {
        DFA dfa = new DFA();
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        dfa.addTransition("q0", 'c', "q3");
        dfa.addState("q1", true);
        dfa.addState("q2", true);
        dfa.addState("q3", true);
        
        DFA dfaWithStart = dfa.setStartState("q0");
        
        assertDFAAccepts(dfaWithStart, "a", true);
        assertDFAAccepts(dfaWithStart, "b", true);
        assertDFAAccepts(dfaWithStart, "c", true);
        assertDFAAccepts(dfaWithStart, "d", false);
    }
    
    @Test
    public void testAddMultipleTransitionsToSameState() {
        DFA dfa = new DFA();
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q2", 'b', "q1");
        dfa.addTransition("q3", 'c', "q1");
        dfa.addState("q1", true);
        
        DFA dfaWithStart = dfa.setStartState("q0");
        
        assertDFAAccepts(dfaWithStart, "a", true);
        
        // Test from q2
        DFA dfaFromQ2 = dfa.setStartState("q2");
        assertDFAAccepts(dfaFromQ2, "b", true);
        
        // Test from q3
        DFA dfaFromQ3 = dfa.setStartState("q3");
        assertDFAAccepts(dfaFromQ3, "c", true);
    }
    
    @Test
    public void testAddSelfTransition() {
        DFA dfa = new DFA();
        dfa.addTransition("q0", 'a', "q0");
        dfa.addState("q0", true);
        
        DFA dfaWithStart = dfa.setStartState("q0");
        
        assertDFAAccepts(dfaWithStart, "", true);
        assertDFAAccepts(dfaWithStart, "a", true);
        assertDFAAccepts(dfaWithStart, "aa", true);
        assertDFAAccepts(dfaWithStart, "aaa", true);
    }
    
    @Test
    public void testAddTransitionChain() {
        DFA dfa = new DFA();
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q1", 'b', "q2");
        dfa.addTransition("q2", 'c', "q3");
        dfa.addState("q3", true);
        
        DFA dfaWithStart = dfa.setStartState("q0");
        
        assertDFAAccepts(dfaWithStart, "abc", true);
        assertDFAAccepts(dfaWithStart, "ab", false);
        assertDFAAccepts(dfaWithStart, "a", false);
        assertDFAAccepts(dfaWithStart, "abcd", false);
    }
    
    @Test
    public void testAddTransitionWithOverwrite() {
        DFA dfa = new DFA();
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'a', "q2"); // Overwrites previous transition
        dfa.addState("q2", true);
        
        DFA dfaWithStart = dfa.setStartState("q0");
        
        assertDFAAccepts(dfaWithStart, "a", true);
        // q1 is not accepting, so this should fail
        assertDFAAccepts(dfaWithStart, "b", false);
    }
    
    @Test
    public void testAddTransitionWithNumericSymbols() {
        DFA dfa = new DFA();
        dfa.addTransition("q0", '0', "q1");
        dfa.addTransition("q0", '1', "q2");
        dfa.addTransition("q1", '0', "q3");
        dfa.addTransition("q1", '1', "q3");
        dfa.addTransition("q2", '0', "q3");
        dfa.addTransition("q2", '1', "q3");
        dfa.addState("q3", true);
        
        DFA dfaWithStart = dfa.setStartState("q0");
        
        assertDFAAccepts(dfaWithStart, "00", true);
        assertDFAAccepts(dfaWithStart, "01", true);
        assertDFAAccepts(dfaWithStart, "10", true);
        assertDFAAccepts(dfaWithStart, "11", true);
        assertDFAAccepts(dfaWithStart, "0", false);
        assertDFAAccepts(dfaWithStart, "1", false);
    }
    
    @Test
    public void testAddTransitionWithSpecialCharacters() {
        DFA dfa = new DFA();
        dfa.addTransition("q0", '!', "q1");
        dfa.addTransition("q0", '@', "q2");
        dfa.addTransition("q0", '#', "q3");
        dfa.addState("q1", true);
        dfa.addState("q2", true);
        dfa.addState("q3", true);
        
        DFA dfaWithStart = dfa.setStartState("q0");
        
        assertDFAAccepts(dfaWithStart, "!", true);
        assertDFAAccepts(dfaWithStart, "@", true);
        assertDFAAccepts(dfaWithStart, "#", true);
        assertDFAAccepts(dfaWithStart, "a", false);
    }
    
    @Test
    public void testAddTransitionWithWhitespace() {
        DFA dfa = new DFA();
        dfa.addTransition("q0", ' ', "q1");
        dfa.addTransition("q0", '\t', "q2");
        dfa.addTransition("q0", '\n', "q3");
        dfa.addState("q1", true);
        dfa.addState("q2", true);
        dfa.addState("q3", true);
        
        DFA dfaWithStart = dfa.setStartState("q0");
        
        assertDFAAccepts(dfaWithStart, " ", true);
        assertDFAAccepts(dfaWithStart, "\t", true);
        assertDFAAccepts(dfaWithStart, "\n", true);
        assertDFAAccepts(dfaWithStart, "a", false);
    }
    
    @Test
    public void testAddTransitionCreatesStates() {
        DFA dfa = new DFA();
        
        // Add transition with non-existent states
        dfa.addTransition("q0", 'a', "q1");
        
        // States should be created automatically
        DFA dfaWithStart = dfa.setStartState("q0");
        dfaWithStart.addState("q1", true);
        
        assertDFAAccepts(dfaWithStart, "a", true);
    }
    
    @Test
    public void testAddTransitionAddsToAlphabet() {
        DFA dfa = new DFA();
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        dfa.addState("q1", true);
        dfa.addState("q2", true);
        
        DFA dfaWithStart = dfa.setStartState("q0");
        
        // Both symbols should be recognized
        assertDFAAccepts(dfaWithStart, "a", true);
        assertDFAAccepts(dfaWithStart, "b", true);
    }
    
    @Test
    public void testComplexTransitionNetwork() {
        DFA dfa = createSimpleDFA("q0", stateSet("q3", "q4"));
        
        // Create a simpler DFA that accepts strings ending with "ab" or "ba"
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
        
        // Test strings ending with "ab"
        assertDFAAccepts(dfa, "ab", true);
        assertDFAAccepts(dfa, "aab", true);
        assertDFAAccepts(dfa, "abab", true);
        
        // Test strings ending with "ba"
        assertDFAAccepts(dfa, "ba", true);
        assertDFAAccepts(dfa, "baba", true);
        
        // Test strings that don't end with "ab" or "ba"
        assertDFAAccepts(dfa, "a", false);
        assertDFAAccepts(dfa, "b", false);
        assertDFAAccepts(dfa, "aa", false);
        assertDFAAccepts(dfa, "bb", false);
        assertDFAAccepts(dfa, "abc", false);
    }
}
