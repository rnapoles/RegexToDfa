package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for NFA string acceptance behavior.
 * Tests various acceptance scenarios and edge cases.
 */
public class NFAAcceptanceTests extends BaseAutomatonTest {
    
    @Test
    public void testAcceptsEmptyString() {
        NFA nfa = createSimpleNFA("q0", stateSet("q0"));
        
        assertNFAAccepts(nfa, "", true);
    }
    
    @Test
    public void testRejectsEmptyStringWhenStartStateNotAccepting() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'a', "q1");
        
        assertNFAAccepts(nfa, "", false);
    }
    
    @Test
    public void testAcceptsSingleCharacter() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'a', "q1");
        
        assertNFAAccepts(nfa, "a", true);
    }
    
    @Test
    public void testRejectsSingleCharacter() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'a', "q2");
        
        assertNFAAccepts(nfa, "a", false);
    }
    
    @Test
    public void testAcceptsMultipleCharacters() {
        NFA nfa = createSimpleNFA("q0", stateSet("q2"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q1", 'b', "q2");
        
        assertNFAAccepts(nfa, "ab", true);
    }
    
    @Test
    public void testRejectsMultipleCharacters() {
        NFA nfa = createSimpleNFA("q0", stateSet("q2"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q1", 'b', "q2");
        
        assertNFAAccepts(nfa, "ac", false);
        assertNFAAccepts(nfa, "a", false);
        assertNFAAccepts(nfa, "abc", false);
    }
    
    @Test
    public void testAcceptsWithEpsilonTransitions() {
        NFA nfa = createSimpleNFA("q0", stateSet("q2"));
        nfa.addEpsilonTransition("q0", "q1");
        nfa.addTransition("q1", 'a', "q2");
        
        assertNFAAccepts(nfa, "a", true);
    }
    
    @Test
    public void testAcceptsWithEpsilonClosure() {
        NFA nfa = createSimpleNFA("q0", stateSet("q3"));
        nfa.addEpsilonTransition("q0", "q1");
        nfa.addEpsilonTransition("q1", "q2");
        nfa.addTransition("q2", 'a', "q3");
        
        assertNFAAccepts(nfa, "a", true);
    }
    
    @Test
    public void testAcceptsWithMultiplePaths() {
        NFA nfa = createSimpleNFA("q0", stateSet("q2"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q0", 'a', "q2"); // Multiple transitions on same symbol
        nfa.addTransition("q1", 'b', "q2");
        
        assertNFAAccepts(nfa, "a", true);
        assertNFAAccepts(nfa, "ab", true);
    }
    
    @Test
    public void testAcceptsWithSelfLoops() {
        NFA nfa = createSimpleNFA("q0", stateSet("q0"));
        nfa.addTransition("q0", 'a', "q0");
        
        assertNFAAccepts(nfa, "", true);
        assertNFAAccepts(nfa, "a", true);
        assertNFAAccepts(nfa, "aa", true);
        assertNFAAccepts(nfa, "aaa", true);
    }
    
    @Test
    public void testAcceptsWithMultipleAcceptingStates() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1", "q2"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q0", 'b', "q2");
        
        assertNFAAccepts(nfa, "a", true);
        assertNFAAccepts(nfa, "b", true);
        assertNFAAccepts(nfa, "c", false);
    }
    
    @Test
    public void testRejectsWithNoTransitions() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        
        assertNFAAccepts(nfa, "a", false);
        assertNFAAccepts(nfa, "b", false);
    }
    
    @Test
    public void testRejectsWithIncompleteTransitions() {
        NFA nfa = createSimpleNFA("q0", stateSet("q2"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q1", 'b', "q2");
        
        assertNFAAccepts(nfa, "a", false);
        assertNFAAccepts(nfa, "ab", true);
        assertNFAAccepts(nfa, "abc", false);
    }
    
    @Test
    public void testAcceptsWithComplexPath() {
        NFA nfa = createSimpleNFA("q0", stateSet("q3"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q1", 'b', "q2");
        nfa.addTransition("q2", 'c', "q3");
        nfa.addTransition("q3", 'd', "q3"); // Self loop on accepting state
        
        assertNFAAccepts(nfa, "abc", true);
        assertNFAAccepts(nfa, "abcd", true);
        assertNFAAccepts(nfa, "abcdd", true);
        assertNFAAccepts(nfa, "ab", false);
        assertNFAAccepts(nfa, "abcdc", false);
    }
    
    @Test
    public void testAcceptsWithBranchingPaths() {
        NFA nfa = createSimpleNFA("q0", stateSet("q2", "q3"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q1", 'b', "q2");
        nfa.addTransition("q1", 'c', "q3");
        
        assertNFAAccepts(nfa, "ab", true);
        assertNFAAccepts(nfa, "ac", true);
        assertNFAAccepts(nfa, "ad", false);
        assertNFAAccepts(nfa, "a", false);
    }
    
    @Test
    public void testRejectsWithNullStartState() {
        NFA nfa = new NFA();
        
        assertNFAAccepts(nfa, "", false);
        assertNFAAccepts(nfa, "a", false);
    }
    
    @Test
    public void testAcceptsWithSpecialCharacters() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", '0', "q1");
        nfa.addTransition("q0", '1', "q1");
        nfa.addTransition("q0", '!', "q1");
        nfa.addTransition("q0", '@', "q1");
        
        assertNFAAccepts(nfa, "0", true);
        assertNFAAccepts(nfa, "1", true);
        assertNFAAccepts(nfa, "!", true);
        assertNFAAccepts(nfa, "@", true);
        assertNFAAccepts(nfa, "a", false);
    }
    
    @Test
    public void testAcceptsWithUnicodeCharacters() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'ñ', "q1");
        nfa.addTransition("q0", '中', "q1");
        nfa.addTransition("q0", 'α', "q1"); // Using Greek alpha instead of emoji
        
        assertNFAAccepts(nfa, "ñ", true);
        assertNFAAccepts(nfa, "中", true);
        assertNFAAccepts(nfa, "α", true);
        assertNFAAccepts(nfa, "a", false);
    }
    
    @Test
    public void testAcceptsWithEpsilonTransitionsAtEnd() {
        NFA nfa = createSimpleNFA("q0", stateSet("q2"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addEpsilonTransition("q1", "q2");
        
        assertNFAAccepts(nfa, "a", true);
    }
    
    @Test
    public void testAcceptsWithEpsilonTransitionsInMiddle() {
        NFA nfa = createSimpleNFA("q0", stateSet("q3"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addEpsilonTransition("q1", "q2");
        nfa.addTransition("q2", 'b', "q3");
        
        assertNFAAccepts(nfa, "ab", true);
    }
    
    @Test
    public void testAcceptsWithMultipleEpsilonTransitions() {
        NFA nfa = createSimpleNFA("q0", stateSet("q3"));
        nfa.addEpsilonTransition("q0", "q1");
        nfa.addTransition("q1", 'a', "q2");
        nfa.addEpsilonTransition("q2", "q3");
        
        assertNFAAccepts(nfa, "a", true);
    }
    
    @Test
    public void testAcceptsWithEpsilonLoops() {
        NFA nfa = createSimpleNFA("q0", stateSet("q2"));
        nfa.addEpsilonTransition("q0", "q1");
        nfa.addEpsilonTransition("q1", "q0"); // Epsilon loop
        nfa.addTransition("q1", 'a', "q2");
        
        assertNFAAccepts(nfa, "a", true);
    }
    
    @Test
    public void testAcceptsWithComplexEpsilonStructure() {
        NFA nfa = createSimpleNFA("q0", stateSet("q4"));
        nfa.addEpsilonTransition("q0", "q1");
        nfa.addTransition("q1", 'a', "q2");
        nfa.addEpsilonTransition("q2", "q3");
        nfa.addEpsilonTransition("q3", "q4");
        nfa.addTransition("q1", 'b', "q4"); // Add direct transition for 'b'
        
        assertNFAAccepts(nfa, "a", true);
        assertNFAAccepts(nfa, "b", true);
        assertNFAAccepts(nfa, "c", false);
    }
    
    @Test
    public void testMultipleTestCases() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q1", 'b', "q1");
        
        TestCase[] testCases = {
            testCase("a", true),
            testCase("ab", true),
            testCase("abb", true),
            testCase("abbb", true),
            testCase("", false),
            testCase("b", false),
            testCase("ba", false),
            testCase("abc", false)
        };
        
        assertNFAMultipleTestCases(nfa, testCases);
    }
    
    @Test
    public void testAcceptsWithEmptyStringAndEpsilon() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addEpsilonTransition("q0", "q1");
        
        assertNFAAccepts(nfa, "", true);
    }
    
    @Test
    public void testAcceptsWithEpsilonAndSymbolTransitions() {
        NFA nfa = createSimpleNFA("q0", stateSet("q2"));
        nfa.addEpsilonTransition("q0", "q1");
        nfa.addTransition("q0", 'a', "q2");
        nfa.addTransition("q1", 'b', "q2");
        
        assertNFAAccepts(nfa, "a", true);
        assertNFAAccepts(nfa, "b", true);
        assertNFAAccepts(nfa, "c", false);
    }
}
