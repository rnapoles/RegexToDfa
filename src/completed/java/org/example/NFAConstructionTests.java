package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

/**
 * Test class for NFA construction and basic properties.
 * Tests NFA creation, state management, and basic functionality.
 */
public class NFAConstructionTests extends BaseAutomatonTest {
    
    @Test
    public void testDefaultConstructor() {
        NFA nfa = new NFA();
        
        assertNotNull(nfa, "NFA should not be null");
        // Test that empty NFA doesn't accept any string
        assertFalse(nfa.accepts(""), "Empty NFA should not accept empty string");
        assertFalse(nfa.accepts("a"), "Empty NFA should not accept any string");
    }
    
    @Test
    public void testConstructorWithStartAndAcceptingStates() {
        Set<String> acceptingStates = stateSet("q1", "q2");
        NFA nfa = new NFA("q0", acceptingStates);
        
        assertNotNull(nfa, "NFA should not be null");
        // Test that NFA with start state but no transitions doesn't accept any string
        assertFalse(nfa.accepts(""), "NFA with no transitions should not accept empty string");
        assertFalse(nfa.accepts("a"), "NFA with no transitions should not accept any string");
    }
    
    @Test
    public void testConstructorWithNullStartState() {
        Set<String> acceptingStates = stateSet("q1");
        NFA nfa = new NFA(null, acceptingStates);
        
        assertNotNull(nfa, "NFA should not be null");
        assertFalse(nfa.accepts(""), "NFA with null start state should not accept any string");
    }
    
    @Test
    public void testConstructorWithEmptyAcceptingStates() {
        Set<String> emptyAcceptingStates = Set.of();
        NFA nfa = new NFA("q0", emptyAcceptingStates);
        
        assertNotNull(nfa, "NFA should not be null");
        assertFalse(nfa.accepts(""), "NFA with no accepting states should not accept any string");
    }
    
    @Test
    public void testAddTransitionWithValidStates() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'a', "q1");
        
        assertNFAAccepts(nfa, "a", true);
    }
    
    @Test
    public void testAddTransitionCreatesStates() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        
        // Add transition with non-existent states
        nfa.addTransition("q0", 'a', "q1");
        
        assertNFAAccepts(nfa, "a", true);
    }
    
    @Test
    public void testAddTransitionAddsToAlphabet() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1", "q2"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q0", 'b', "q2");
        
        // Both symbols should be recognized
        assertNFAAccepts(nfa, "a", true);
        assertNFAAccepts(nfa, "b", true);
    }
    
    @Test
    public void testAddEpsilonTransition() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addEpsilonTransition("q0", "q1");
        
        assertNFAAccepts(nfa, "", true);
    }
    
    @Test
    public void testAddEpsilonTransitionCreatesStates() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        
        // Add epsilon transition with non-existent states
        nfa.addEpsilonTransition("q0", "q1");
        
        assertNFAAccepts(nfa, "", true);
    }
    
    @Test
    public void testAddMultipleTransitionsFromSameState() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1", "q2", "q3"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q0", 'b', "q2");
        nfa.addTransition("q0", 'c', "q3");
        
        assertNFAAccepts(nfa, "a", true);
        assertNFAAccepts(nfa, "b", true);
        assertNFAAccepts(nfa, "c", true);
        assertNFAAccepts(nfa, "d", false);
    }
    
    @Test
    public void testAddMultipleTransitionsToSameState() {
        NFA nfa1 = createSimpleNFA("q0", stateSet("q1"));
        nfa1.addTransition("q0", 'a', "q1");
        
        NFA nfa2 = createSimpleNFA("q2", stateSet("q1"));
        nfa2.addTransition("q2", 'b', "q1");
        
        NFA nfa3 = createSimpleNFA("q3", stateSet("q1"));
        nfa3.addTransition("q3", 'c', "q1");
        
        assertNFAAccepts(nfa1, "a", true);
        assertNFAAccepts(nfa2, "b", true);
        assertNFAAccepts(nfa3, "c", true);
    }
    
    @Test
    public void testAddSelfTransition() {
        NFA nfa = createSimpleNFA("q0", stateSet("q0"));
        nfa.addTransition("q0", 'a', "q0");
        
        assertNFAAccepts(nfa, "", true);
        assertNFAAccepts(nfa, "a", true);
        assertNFAAccepts(nfa, "aa", true);
        assertNFAAccepts(nfa, "aaa", true);
    }
    
    @Test
    public void testAddTransitionChain() {
        NFA nfa = createSimpleNFA("q0", stateSet("q3"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q1", 'b', "q2");
        nfa.addTransition("q2", 'c', "q3");
        
        assertNFAAccepts(nfa, "abc", true);
        assertNFAAccepts(nfa, "ab", false);
        assertNFAAccepts(nfa, "a", false);
        assertNFAAccepts(nfa, "abcd", false);
    }
    
    @Test
    public void testAddMultipleTransitionsOnSameSymbol() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1", "q2"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q0", 'a', "q2"); // Multiple transitions on same symbol
        
        // Both paths should lead to acceptance
        assertNFAAccepts(nfa, "a", true);
    }
    
    @Test
    public void testAddEpsilonTransitionChain() {
        NFA nfa = createSimpleNFA("q0", stateSet("q2"));
        nfa.addEpsilonTransition("q0", "q1");
        nfa.addEpsilonTransition("q1", "q2");
        
        assertNFAAccepts(nfa, "", true);
    }
    
    @Test
    public void testAddMixedTransitions() {
        NFA nfa = createSimpleNFA("q0", stateSet("q3"));
        nfa.addEpsilonTransition("q0", "q1");
        nfa.addTransition("q1", 'a', "q2");
        nfa.addEpsilonTransition("q2", "q3");
        
        assertNFAAccepts(nfa, "a", true);
        assertNFAAccepts(nfa, "", false);
    }
    
    @Test
    public void testAddTransitionWithSpecialCharacters() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1", "q2", "q3"));
        nfa.addTransition("q0", '!', "q1");
        nfa.addTransition("q0", '@', "q2");
        nfa.addTransition("q0", '#', "q3");
        
        assertNFAAccepts(nfa, "!", true);
        assertNFAAccepts(nfa, "@", true);
        assertNFAAccepts(nfa, "#", true);
        assertNFAAccepts(nfa, "a", false);
    }
    
    @Test
    public void testAddTransitionWithNumericSymbols() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1", "q2"));
        nfa.addTransition("q0", '0', "q1");
        nfa.addTransition("q0", '1', "q2");
        
        assertNFAAccepts(nfa, "0", true);
        assertNFAAccepts(nfa, "1", true);
        assertNFAAccepts(nfa, "a", false);
    }
    
    @Test
    public void testAddTransitionWithWhitespace() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1", "q2", "q3"));
        nfa.addTransition("q0", ' ', "q1");
        nfa.addTransition("q0", '\t', "q2");
        nfa.addTransition("q0", '\n', "q3");
        
        assertNFAAccepts(nfa, " ", true);
        assertNFAAccepts(nfa, "\t", true);
        assertNFAAccepts(nfa, "\n", true);
        assertNFAAccepts(nfa, "a", false);
    }
    
    @Test
    public void testAddTransitionWithUnicodeCharacters() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1", "q2", "q3"));
        nfa.addTransition("q0", 'ñ', "q1");
        nfa.addTransition("q0", '中', "q2");
        nfa.addTransition("q0", 'α', "q3"); // Using Greek alpha instead of emoji
        
        assertNFAAccepts(nfa, "ñ", true);
        assertNFAAccepts(nfa, "中", true);
        assertNFAAccepts(nfa, "α", true);
        assertNFAAccepts(nfa, "a", false);
    }
    
    @Test
    public void testComplexNFAConstruction() {
        NFA nfa = createSimpleNFA("q0", stateSet("q3"));
        
        // Create a complex NFA that accepts strings containing "ab" or "ba"
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q0", 'b', "q2");
        nfa.addTransition("q1", 'a', "q1");
        nfa.addTransition("q1", 'b', "q3");
        nfa.addTransition("q2", 'a', "q3");
        nfa.addTransition("q2", 'b', "q2");
        nfa.addTransition("q3", 'a', "q3");
        nfa.addTransition("q3", 'b', "q3");
        
        // Test strings containing "ab"
        assertNFAAccepts(nfa, "ab", true);
        assertNFAAccepts(nfa, "aab", true);
        assertNFAAccepts(nfa, "bab", true);
        assertNFAAccepts(nfa, "abab", true);
        
        // Test strings containing "ba"
        assertNFAAccepts(nfa, "ba", true);
        assertNFAAccepts(nfa, "aba", true);
        assertNFAAccepts(nfa, "bba", true);
        assertNFAAccepts(nfa, "baba", true);
        
        // Test strings that don't contain "ab" or "ba"
        assertNFAAccepts(nfa, "a", false);
        assertNFAAccepts(nfa, "b", false);
        assertNFAAccepts(nfa, "aa", false);
        assertNFAAccepts(nfa, "bb", false);
    }
}
