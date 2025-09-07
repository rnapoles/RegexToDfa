package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for NFA operations (union, concatenation, star, plus, question).
 * Tests various NFA operations and their behavior.
 */
public class NFAOperationsTests extends BaseAutomatonTest {
    
    @Test
    public void testStarOperation() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'a', "q1");
        
        NFA starNFA = nfa.star();
        
        assertNotNull(starNFA, "Star NFA should not be null");
        
        // Test that star accepts zero or more occurrences
        assertNFAAccepts(starNFA, "", true);
        assertNFAAccepts(starNFA, "a", true);
        assertNFAAccepts(starNFA, "aa", true);
        assertNFAAccepts(starNFA, "aaa", true);
        assertNFAAccepts(starNFA, "b", false);
    }
    
    @Test
    public void testStarOperationWithEmptyNFA() {
        NFA nfa = new NFA();
        
        NFA starNFA = nfa.star();
        
        assertNotNull(starNFA, "Star NFA should not be null");
        
        // Empty NFA star should accept only empty string
        assertNFAAccepts(starNFA, "", true);
        assertNFAAccepts(starNFA, "a", false);
    }
    
    @Test
    public void testPlusOperation() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'a', "q1");
        
        NFA plusNFA = nfa.plus();
        
        assertNotNull(plusNFA, "Plus NFA should not be null");
        
        // Test that plus accepts one or more occurrences
        assertNFAAccepts(plusNFA, "", false);
        assertNFAAccepts(plusNFA, "a", true);
        assertNFAAccepts(plusNFA, "aa", true);
        assertNFAAccepts(plusNFA, "aaa", true);
        assertNFAAccepts(plusNFA, "b", false);
    }
    
    @Test
    public void testQuestionOperation() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'a', "q1");
        
        NFA questionNFA = nfa.question();
        
        assertNotNull(questionNFA, "Question NFA should not be null");
        
        // Test that question accepts zero or one occurrence
        assertNFAAccepts(questionNFA, "", true);
        assertNFAAccepts(questionNFA, "a", true);
        assertNFAAccepts(questionNFA, "aa", false);
        assertNFAAccepts(questionNFA, "b", false);
    }
    
    @Test
    public void testQuestionOperationWithEmptyNFA() {
        NFA nfa = new NFA();
        
        NFA questionNFA = nfa.question();
        
        assertNotNull(questionNFA, "Question NFA should not be null");
        
        // Empty NFA question should accept only empty string
        assertNFAAccepts(questionNFA, "", true);
        assertNFAAccepts(questionNFA, "a", false);
    }
    
    @Test
    public void testUnionOperation() {
        NFA nfa1 = createSimpleNFA("q0", stateSet("q1"));
        nfa1.addTransition("q0", 'a', "q1");
        
        NFA nfa2 = createSimpleNFA("q2", stateSet("q3"));
        nfa2.addTransition("q2", 'b', "q3");
        
        NFA unionNFA = nfa1.union(nfa2);
        
        assertNotNull(unionNFA, "Union NFA should not be null");
        
        // Test that union accepts strings from either NFA
        assertNFAAccepts(unionNFA, "a", true);
        assertNFAAccepts(unionNFA, "b", true);
        assertNFAAccepts(unionNFA, "ab", false);
        assertNFAAccepts(unionNFA, "c", false);
    }
    
    @Test
    public void testUnionOperationWithEmptyNFAs() {
        NFA nfa1 = new NFA();
        NFA nfa2 = new NFA();
        
        NFA unionNFA = nfa1.union(nfa2);
        
        assertNotNull(unionNFA, "Union NFA should not be null");
        
        // Union of empty NFAs should accept nothing
        assertNFAAccepts(unionNFA, "", false);
        assertNFAAccepts(unionNFA, "a", false);
    }
    
    @Test
    public void testUnionOperationWithOneEmptyNFA() {
        NFA nfa1 = createSimpleNFA("q0", stateSet("q1"));
        nfa1.addTransition("q0", 'a', "q1");
        
        NFA nfa2 = new NFA();
        
        NFA unionNFA = nfa1.union(nfa2);
        
        assertNotNull(unionNFA, "Union NFA should not be null");
        
        // Union should accept strings from the non-empty NFA
        assertNFAAccepts(unionNFA, "a", true);
        assertNFAAccepts(unionNFA, "b", false);
    }
    
    @Test
    public void testConcatenationOperation() {
        NFA nfa1 = createSimpleNFA("q0", stateSet("q1"));
        nfa1.addTransition("q0", 'a', "q1");
        
        NFA nfa2 = createSimpleNFA("q2", stateSet("q3"));
        nfa2.addTransition("q2", 'b', "q3");
        
        NFA concatNFA = nfa1.concatenate(nfa2);
        
        assertNotNull(concatNFA, "Concatenation NFA should not be null");
        
        // Test that concatenation accepts strings from first NFA followed by second NFA
        assertNFAAccepts(concatNFA, "ab", true);
        assertNFAAccepts(concatNFA, "a", false);
        assertNFAAccepts(concatNFA, "b", false);
        assertNFAAccepts(concatNFA, "ba", false);
    }
    
    @Test
    public void testConcatenationOperationWithEmptyNFAs() {
        NFA nfa1 = new NFA();
        NFA nfa2 = new NFA();
        
        NFA concatNFA = nfa1.concatenate(nfa2);
        
        assertNotNull(concatNFA, "Concatenation NFA should not be null");
        
        // Concatenation of empty NFAs should accept nothing
        assertNFAAccepts(concatNFA, "", false);
        assertNFAAccepts(concatNFA, "a", false);
    }
    
    @Test
    public void testConcatenationOperationWithOneEmptyNFA() {
        NFA nfa1 = createSimpleNFA("q0", stateSet("q1"));
        nfa1.addTransition("q0", 'a', "q1");
        
        NFA nfa2 = new NFA();
        
        NFA concatNFA = nfa1.concatenate(nfa2);
        
        assertNotNull(concatNFA, "Concatenation NFA should not be null");
        
        // Concatenation with empty NFA should accept strings from the non-empty NFA
        assertNFAAccepts(concatNFA, "a", true);
        assertNFAAccepts(concatNFA, "b", false);
    }
    
    @Test
    public void testComplexStarOperation() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q1", 'b', "q1");
        
        NFA starNFA = nfa.star();
        
        assertNotNull(starNFA, "Star NFA should not be null");
        
        // Test that star accepts zero or more occurrences of "ab*"
        assertNFAAccepts(starNFA, "", true);
        assertNFAAccepts(starNFA, "a", true);
        assertNFAAccepts(starNFA, "ab", true);
        assertNFAAccepts(starNFA, "abb", true);
        assertNFAAccepts(starNFA, "aab", true);
        assertNFAAccepts(starNFA, "aabb", true);
        assertNFAAccepts(starNFA, "b", false);
    }
    
    @Test
    public void testComplexPlusOperation() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q1", 'b', "q1");
        
        NFA plusNFA = nfa.plus();
        
        assertNotNull(plusNFA, "Plus NFA should not be null");
        
        // Test that plus accepts one or more occurrences of "ab*"
        assertNFAAccepts(plusNFA, "", false);
        assertNFAAccepts(plusNFA, "a", true);
        assertNFAAccepts(plusNFA, "ab", true);
        assertNFAAccepts(plusNFA, "abb", true);
        assertNFAAccepts(plusNFA, "aab", true);
        assertNFAAccepts(plusNFA, "aabb", true);
        assertNFAAccepts(plusNFA, "b", false);
    }
    
    @Test
    public void testComplexQuestionOperation() {
        NFA nfa = createSimpleNFA("q0", stateSet("q1"));
        nfa.addTransition("q0", 'a', "q1");
        nfa.addTransition("q1", 'b', "q1");
        
        NFA questionNFA = nfa.question();
        
        assertNotNull(questionNFA, "Question NFA should not be null");
        
        // Test that question accepts zero or one occurrence of "ab*"
        assertNFAAccepts(questionNFA, "", true);
        assertNFAAccepts(questionNFA, "a", true);
        assertNFAAccepts(questionNFA, "ab", true);
        assertNFAAccepts(questionNFA, "abb", true);
        assertNFAAccepts(questionNFA, "aab", false);
        assertNFAAccepts(questionNFA, "b", false);
    }
    
    @Test
    public void testComplexUnionOperation() {
        NFA nfa1 = createSimpleNFA("q0", stateSet("q1"));
        nfa1.addTransition("q0", 'a', "q1");
        nfa1.addTransition("q1", 'a', "q1");
        
        NFA nfa2 = createSimpleNFA("q2", stateSet("q3"));
        nfa2.addTransition("q2", 'b', "q3");
        nfa2.addTransition("q3", 'b', "q3");
        
        NFA unionNFA = nfa1.union(nfa2);
        
        assertNotNull(unionNFA, "Union NFA should not be null");
        
        // Test that union accepts strings from either NFA
        assertNFAAccepts(unionNFA, "a", true);
        assertNFAAccepts(unionNFA, "aa", true);
        assertNFAAccepts(unionNFA, "aaa", true);
        assertNFAAccepts(unionNFA, "b", true);
        assertNFAAccepts(unionNFA, "bb", true);
        assertNFAAccepts(unionNFA, "bbb", true);
        assertNFAAccepts(unionNFA, "ab", false);
        assertNFAAccepts(unionNFA, "ba", false);
    }
    
    @Test
    public void testComplexConcatenationOperation() {
        NFA nfa1 = createSimpleNFA("q0", stateSet("q1"));
        nfa1.addTransition("q0", 'a', "q1");
        nfa1.addTransition("q1", 'a', "q1");
        
        NFA nfa2 = createSimpleNFA("q2", stateSet("q3"));
        nfa2.addTransition("q2", 'b', "q3");
        nfa2.addTransition("q3", 'b', "q3");
        
        NFA concatNFA = nfa1.concatenate(nfa2);
        
        assertNotNull(concatNFA, "Concatenation NFA should not be null");
        
        // Test that concatenation accepts strings from first NFA followed by second NFA
        assertNFAAccepts(concatNFA, "ab", true);
        assertNFAAccepts(concatNFA, "aab", true);
        assertNFAAccepts(concatNFA, "aabb", true);
        assertNFAAccepts(concatNFA, "a", false);
        assertNFAAccepts(concatNFA, "b", false);
        assertNFAAccepts(concatNFA, "ba", false);
    }
    
    @Test
    public void testChainedOperations() {
        NFA nfa1 = createSimpleNFA("q0", stateSet("q1"));
        nfa1.addTransition("q0", 'a', "q1");
        
        NFA nfa2 = createSimpleNFA("q2", stateSet("q3"));
        nfa2.addTransition("q2", 'b', "q3");
        
        // Test chained operations: (a* | b*)
        NFA star1 = nfa1.star();
        NFA star2 = nfa2.star();
        NFA unionNFA = star1.union(star2);
        
        assertNotNull(unionNFA, "Chained operations NFA should not be null");
        
        // Test that chained operations work correctly
        assertNFAAccepts(unionNFA, "", true);
        assertNFAAccepts(unionNFA, "a", true);
        assertNFAAccepts(unionNFA, "aa", true);
        assertNFAAccepts(unionNFA, "b", true);
        assertNFAAccepts(unionNFA, "bb", true);
        assertNFAAccepts(unionNFA, "ab", false);
        assertNFAAccepts(unionNFA, "ba", false);
    }
    
    @Test
    public void testNestedOperations() {
        NFA nfa1 = createSimpleNFA("q0", stateSet("q1"));
        nfa1.addTransition("q0", 'a', "q1");
        
        NFA nfa2 = createSimpleNFA("q2", stateSet("q3"));
        nfa2.addTransition("q2", 'b', "q3");
        
        // Test nested operations: (a | b)*
        NFA unionNFA = nfa1.union(nfa2);
        NFA starNFA = unionNFA.star();
        
        assertNotNull(starNFA, "Nested operations NFA should not be null");
        
        // Test that nested operations work correctly
        assertNFAAccepts(starNFA, "", true);
        assertNFAAccepts(starNFA, "a", true);
        assertNFAAccepts(starNFA, "b", true);
        assertNFAAccepts(starNFA, "ab", true);
        assertNFAAccepts(starNFA, "ba", true);
        assertNFAAccepts(starNFA, "aab", true);
        assertNFAAccepts(starNFA, "abb", true);
        assertNFAAccepts(starNFA, "c", false);
    }
    
    @Test
    public void testOperationsWithEpsilonTransitions() {
        NFA nfa1 = createSimpleNFA("q0", stateSet("q1"));
        nfa1.addEpsilonTransition("q0", "q1");
        
        NFA nfa2 = createSimpleNFA("q2", stateSet("q3"));
        nfa2.addTransition("q2", 'a', "q3");
        
        NFA unionNFA = nfa1.union(nfa2);
        
        assertNotNull(unionNFA, "Union NFA with epsilon transitions should not be null");
        
        // Test that union with epsilon transitions works correctly
        assertNFAAccepts(unionNFA, "", true);
        assertNFAAccepts(unionNFA, "a", true);
        assertNFAAccepts(unionNFA, "b", false);
    }
}
