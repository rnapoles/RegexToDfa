package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DFA string acceptance behavior.
 * Tests various acceptance scenarios and edge cases.
 */
public class DFAAcceptanceTests extends BaseAutomatonTest {
    
    @Test
    public void testAcceptsEmptyString() {
        DFA dfa = createSimpleDFA("q0", stateSet("q0"));
        
        assertDFAAccepts(dfa, "", true);
    }
    
    @Test
    public void testRejectsEmptyStringWhenStartStateNotAccepting() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'a', "q1");
        
        assertDFAAccepts(dfa, "", false);
    }
    
    @Test
    public void testAcceptsSingleCharacter() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'a', "q1");
        
        assertDFAAccepts(dfa, "a", true);
    }
    
    @Test
    public void testRejectsSingleCharacter() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'a', "q2");
        
        assertDFAAccepts(dfa, "a", false);
    }
    
    @Test
    public void testAcceptsMultipleCharacters() {
        DFA dfa = createSimpleDFA("q0", stateSet("q2"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q1", 'b', "q2");
        
        assertDFAAccepts(dfa, "ab", true);
    }
    
    @Test
    public void testRejectsMultipleCharacters() {
        DFA dfa = createSimpleDFA("q0", stateSet("q2"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q1", 'b', "q2");
        
        assertDFAAccepts(dfa, "ac", false);
        assertDFAAccepts(dfa, "a", false);
        assertDFAAccepts(dfa, "abc", false);
    }
    
    @Test
    public void testAcceptsWithSelfLoops() {
        DFA dfa = createSimpleDFA("q0", stateSet("q0"));
        dfa.addTransition("q0", 'a', "q0");
        
        assertDFAAccepts(dfa, "", true);
        assertDFAAccepts(dfa, "a", true);
        assertDFAAccepts(dfa, "aa", true);
        assertDFAAccepts(dfa, "aaa", true);
    }
    
    @Test
    public void testAcceptsWithMultipleAcceptingStates() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1", "q2"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q0", 'b', "q2");
        
        assertDFAAccepts(dfa, "a", true);
        assertDFAAccepts(dfa, "b", true);
        assertDFAAccepts(dfa, "c", false);
    }
    
    @Test
    public void testRejectsWithNoTransitions() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        
        assertDFAAccepts(dfa, "a", false);
        assertDFAAccepts(dfa, "b", false);
    }
    
    @Test
    public void testRejectsWithIncompleteTransitions() {
        DFA dfa = createSimpleDFA("q0", stateSet("q2"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q1", 'b', "q2");
        
        assertDFAAccepts(dfa, "a", false);
        assertDFAAccepts(dfa, "ab", true);
        assertDFAAccepts(dfa, "abc", false);
    }
    
    @Test
    public void testAcceptsWithComplexPath() {
        DFA dfa = createSimpleDFA("q0", stateSet("q3"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q1", 'b', "q2");
        dfa.addTransition("q2", 'c', "q3");
        dfa.addTransition("q3", 'd', "q3"); // Self loop on accepting state
        
        assertDFAAccepts(dfa, "abc", true);
        assertDFAAccepts(dfa, "abcd", true);
        assertDFAAccepts(dfa, "abcdd", true);
        assertDFAAccepts(dfa, "ab", false);
        assertDFAAccepts(dfa, "abcdc", false);
    }
    
    @Test
    public void testAcceptsWithBranchingPaths() {
        DFA dfa = createSimpleDFA("q0", stateSet("q2", "q3"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q1", 'b', "q2");
        dfa.addTransition("q1", 'c', "q3");
        
        assertDFAAccepts(dfa, "ab", true);
        assertDFAAccepts(dfa, "ac", true);
        assertDFAAccepts(dfa, "ad", false);
        assertDFAAccepts(dfa, "a", false);
    }
    
    @Test
    public void testRejectsWithNullStartState() {
        DFA dfa = new DFA();
        dfa.addState("q1", true);
        
        assertDFAAccepts(dfa, "", false);
        assertDFAAccepts(dfa, "a", false);
    }
    
    @Test
    public void testAcceptsWithSpecialCharacters() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", '0', "q1");
        dfa.addTransition("q0", '1', "q1");
        dfa.addTransition("q0", '!', "q1");
        dfa.addTransition("q0", '@', "q1");
        
        assertDFAAccepts(dfa, "0", true);
        assertDFAAccepts(dfa, "1", true);
        assertDFAAccepts(dfa, "!", true);
        assertDFAAccepts(dfa, "@", true);
        assertDFAAccepts(dfa, "a", false);
    }
    
    @Test
    public void testAcceptsWithUnicodeCharacters() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'ñ', "q1");
        dfa.addTransition("q0", '中', "q1");
        dfa.addTransition("q0", 'α', "q1"); // Using Greek alpha instead of emoji
        
        assertDFAAccepts(dfa, "ñ", true);
        assertDFAAccepts(dfa, "中", true);
        assertDFAAccepts(dfa, "α", true);
        assertDFAAccepts(dfa, "a", false);
    }
    
    @Test
    public void testMultipleTestCases() {
        DFA dfa = createSimpleDFA("q0", stateSet("q1"));
        dfa.addTransition("q0", 'a', "q1");
        dfa.addTransition("q1", 'b', "q1");
        
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
        
        assertDFAMultipleTestCases(dfa, testCases);
    }
}
