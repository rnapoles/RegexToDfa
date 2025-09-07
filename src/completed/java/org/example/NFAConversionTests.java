package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for NFA to DFA conversion functionality.
 * Tests the toDFA method and its behavior.
 */
public class NFAConversionTests extends BaseAutomatonTest {
    
    @Test
    public void testConvertSimpleNFA() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'a', "q1");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that the DFA accepts the same strings as the NFA
        assertDFAAccepts(dfa, "a", true);
        assertDFAAccepts(dfa, "b", false);
        assertDFAAccepts(dfa, "aa", false);
    }
    
    @Test
    public void testConvertEmptyNFA() {
        NFA nfa = new NFA();
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Empty NFA should convert to DFA that accepts nothing
        assertDFAAccepts(dfa, "", false);
        assertDFAAccepts(dfa, "a", false);
    }
    
    @Test
    public void testConvertNFAWithEpsilonTransitions() {
        NFA nfa = createSimpleNFA("q0", stateSet("q2"));
        nfa.addEpsilonTransition("q0", "q1");
        nfa.addTransition("q1", 'a', "q2");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that epsilon transitions are handled correctly
        assertDFAAccepts(dfa, "a", true);
        assertDFAAccepts(dfa, "b", false);
    }
    
    @Test
    public void testConvertNFAWithMultipleTransitions() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1", "q2"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q0", 'b', "q2");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that multiple transitions are handled correctly
        assertDFAAccepts(dfa, "a", true);
        assertDFAAccepts(dfa, "b", true);
        assertDFAAccepts(dfa, "c", false);
    }
    
    @Test
    public void testConvertNFAWithSelfLoops() {
        NFA nfa = createSimpleNFA("q0", stateSet("q0"));
        nfa.addTransition("q0", 'a', "q0");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that self loops are handled correctly
        assertDFAAccepts(dfa, "", true);
        assertDFAAccepts(dfa, "a", true);
        assertDFAAccepts(dfa, "aa", true);
        assertDFAAccepts(dfa, "aaa", true);
    }
    
    @Test
    public void testConvertNFAWithComplexEpsilonStructure() {
        NFA nfa = createSimpleNFA("q0", stateSet("q3"));
        nfa.addEpsilonTransition("q0", "q1");
        nfa.addTransition("q1", 'a', "q2");
        nfa.addEpsilonTransition("q2", "q3");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that complex epsilon structure is handled correctly
        assertDFAAccepts(dfa, "a", true);
        assertDFAAccepts(dfa, "b", false);
    }
    
    @Test
    public void testConvertNFAWithMultipleAcceptingStates() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1", "q2"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q0", 'b', "q2");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that multiple accepting states are handled correctly
        assertDFAAccepts(dfa, "a", true);
        assertDFAAccepts(dfa, "b", true);
        assertDFAAccepts(dfa, "c", false);
    }
    
    @Test
    public void testConvertNFAWithNoAcceptingStates() {
        NFA nfa = createSimpleNFA("q0", stateSet());
        nfa.addTransition("q0", 'a', "q1");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // NFA with no accepting states should convert to DFA that accepts nothing
        assertDFAAccepts(dfa, "", false);
        assertDFAAccepts(dfa, "a", false);
    }
    
    @Test
    public void testConvertNFAWithNullStartState() {
        NFA nfa = new NFA();
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // NFA with null start state should convert to DFA that accepts nothing
        assertDFAAccepts(dfa, "", false);
        assertDFAAccepts(dfa, "a", false);
    }
    
    @Test
    public void testConvertNFAWithComplexLanguage() {
        // Create NFA that accepts strings containing "ab" or "ba"
        NFA nfa = createSimpleNFA("q0", stateSet("q3"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q0", 'b', "q2");
        nfa.addTransition("q1", 'a', "q1");
        nfa.addTransition("q1", 'b', "q3");
        nfa.addTransition("q2", 'a', "q3");
        nfa.addTransition("q2", 'b', "q2");
        nfa.addTransition("q3", 'a', "q3");
        nfa.addTransition("q3", 'b', "q3");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that the converted DFA accepts the same strings
        assertDFAAccepts(dfa, "ab", true);
        assertDFAAccepts(dfa, "ba", true);
        assertDFAAccepts(dfa, "aab", true);
        assertDFAAccepts(dfa, "bba", true);
        assertDFAAccepts(dfa, "abab", true);
        assertDFAAccepts(dfa, "baba", true);
        assertDFAAccepts(dfa, "a", false);
        assertDFAAccepts(dfa, "b", false);
        assertDFAAccepts(dfa, "aa", false);
        assertDFAAccepts(dfa, "bb", false);
    }
    
    @Test
    public void testConvertNFAWithEpsilonLoops() {
        NFA nfa = createSimpleNFA("q0", stateSet("q2"));
        nfa.addEpsilonTransition("q0", "q1");
        nfa.addEpsilonTransition("q1", "q0"); // Epsilon loop
        nfa.addTransition("q1", 'a', "q2");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that epsilon loops are handled correctly
        assertDFAAccepts(dfa, "a", true);
        assertDFAAccepts(dfa, "b", false);
    }
    
    @Test
    public void testConvertNFAWithMultiplePaths() {
        NFA nfa = createSimpleNFA("q0", stateSet("q2"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q0", 'a', "q2"); // Multiple transitions on same symbol
        nfa.addTransition("q1", 'b', "q2");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that multiple paths are handled correctly
        assertDFAAccepts(dfa, "a", true);
        assertDFAAccepts(dfa, "ab", true);
        assertDFAAccepts(dfa, "b", false);
    }
    
    @Test
    public void testConvertNFAWithSpecialCharacters() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", '!', "q1");
        nfa.addTransition("q0", '@', "q1");
        nfa.addTransition("q0", '#', "q1");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that special characters are handled correctly
        assertDFAAccepts(dfa, "!", true);
        assertDFAAccepts(dfa, "@", true);
        assertDFAAccepts(dfa, "#", true);
        assertDFAAccepts(dfa, "a", false);
    }
    
    @Test
    public void testConvertNFAWithNumericSymbols() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", '0', "q1");
        nfa.addTransition("q0", '1', "q1");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that numeric symbols are handled correctly
        assertDFAAccepts(dfa, "0", true);
        assertDFAAccepts(dfa, "1", true);
        assertDFAAccepts(dfa, "a", false);
    }
    
    @Test
    public void testConvertNFAWithUnicodeCharacters() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'ñ', "q1");
        nfa.addTransition("q0", '中', "q1");
        nfa.addTransition("q0", 'α', "q1"); // Using Greek alpha instead of emoji
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that Unicode characters are handled correctly
        assertDFAAccepts(dfa, "ñ", true);
        assertDFAAccepts(dfa, "中", true);
        assertDFAAccepts(dfa, "α", true);
        assertDFAAccepts(dfa, "a", false);
    }
    
    @Test
    public void testConvertNFAWithWhitespace() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", ' ', "q1");
        nfa.addTransition("q0", '\t', "q1");
        nfa.addTransition("q0", '\n', "q1");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that whitespace characters are handled correctly
        assertDFAAccepts(dfa, " ", true);
        assertDFAAccepts(dfa, "\t", true);
        assertDFAAccepts(dfa, "\n", true);
        assertDFAAccepts(dfa, "a", false);
    }
    
    @Test
    public void testConvertNFAWithEmptyStringAcceptance() {
        NFA nfa = createSimpleNFA("q0", stateSet("q0"));
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
        // Test that empty string acceptance is handled correctly
        assertDFAAccepts(dfa, "", true);
        assertDFAAccepts(dfa, "a", false);
    }
    
    
    @Test
    public void testConvertNFAWithMultipleTestCases() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q1", 'b', "q1");
        
        DFA dfa = nfa.toDFA();
        
        assertNotNull(dfa, "Converted DFA should not be null");
        
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
