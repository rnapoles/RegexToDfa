package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for quantifier operators: *, +, ?
 * Tests Kleene star, plus, and question mark operations.
 */
public class QuantifierTests extends BaseRegexToDFATest {
    
    @Test
    public void testKleeneStar() {
        convertRegex("a*");
        
        assertMultipleTestCases(
            testCase("", true),      // zero occurrences
            testCase("a", true),     // one occurrence
            testCase("aa", true),    // two occurrences
            testCase("aaa", true),   // three occurrences
            testCase("aaaa", true),  // four occurrences
            testCase("b", false),    // different character
            testCase("ab", false)    // mixed characters
        );
    }
    
    @Test
    public void testKleenePlus() {
        convertRegex("a+");
        
        assertMultipleTestCases(
            testCase("", false),     // zero occurrences - should reject
            testCase("a", true),     // one occurrence
            testCase("aa", true),    // two occurrences
            testCase("aaa", true),   // three occurrences
            testCase("aaaa", true),  // four occurrences
            testCase("b", false),    // different character
            testCase("ab", false)    // mixed characters
        );
    }
    
    @Test
    public void testQuestionMark() {
        convertRegex("a?");
        
        assertMultipleTestCases(
            testCase("", true),      // zero occurrences
            testCase("a", true),     // one occurrence
            testCase("aa", false),   // two occurrences - should reject
            testCase("aaa", false), // three occurrences - should reject
            testCase("b", false),    // different character
            testCase("ab", false)    // mixed characters
        );
    }
    
    @Test
    public void testStarWithConcatenation() {
        convertRegex("ab*");
        
        assertMultipleTestCases(
            testCase("a", true),     // a followed by zero b's
            testCase("ab", true),    // a followed by one b
            testCase("abb", true),   // a followed by two b's
            testCase("abbb", true),  // a followed by three b's
            testCase("", false),     // empty string
            testCase("b", false),    // just b
            testCase("aa", false),   // two a's
            testCase("abc", false)   // mixed characters
        );
    }
    
    @Test
    public void testPlusWithConcatenation() {
        convertRegex("ab+");
        
        assertMultipleTestCases(
            testCase("a", false),    // a followed by zero b's - should reject
            testCase("ab", true),    // a followed by one b
            testCase("abb", true),   // a followed by two b's
            testCase("abbb", true),  // a followed by three b's
            testCase("", false),     // empty string
            testCase("b", false),    // just b
            testCase("aa", false),   // two a's
            testCase("abc", false)   // mixed characters
        );
    }
    
    @Test
    public void testQuestionWithConcatenation() {
        convertRegex("ab?");
        
        assertMultipleTestCases(
            testCase("a", true),     // a followed by zero b's
            testCase("ab", true),     // a followed by one b
            testCase("abb", false),  // a followed by two b's - should reject
            testCase("abbb", false), // a followed by three b's - should reject
            testCase("", false),     // empty string
            testCase("b", false),    // just b
            testCase("aa", false),   // two a's
            testCase("abc", false)   // mixed characters
        );
    }
    
    @Test
    public void testStarWithUnion() {
        convertRegex("(a|b)*");
        
        assertMultipleTestCases(
            testCase("", true),      // zero occurrences
            testCase("a", true),     // one a
            testCase("b", true),     // one b
            testCase("aa", true),    // two a's
            testCase("bb", true),    // two b's
            testCase("ab", true),    // a then b
            testCase("ba", true),    // b then a
            testCase("aba", true),   // mixed sequence
            testCase("bab", true),   // mixed sequence
            testCase("abab", true),  // longer mixed sequence
            testCase("c", false),    // different character
            testCase("ac", false)    // mixed with different character
        );
    }
    
    @Test
    public void testPlusWithUnion() {
        convertRegex("(a|b)+");
        
        assertMultipleTestCases(
            testCase("", false),     // zero occurrences - should reject
            testCase("a", true),     // one a
            testCase("b", true),     // one b
            testCase("aa", true),    // two a's
            testCase("bb", true),    // two b's
            testCase("ab", true),    // a then b
            testCase("ba", true),    // b then a
            testCase("aba", true),   // mixed sequence
            testCase("bab", true),   // mixed sequence
            testCase("abab", true),  // longer mixed sequence
            testCase("c", false),    // different character
            testCase("ac", false)    // mixed with different character
        );
    }
    
    @Test
    public void testQuestionWithUnion() {
        convertRegex("(a|b)?");
        
        assertMultipleTestCases(
            testCase("", true),      // zero occurrences
            testCase("a", true),     // one a
            testCase("b", true),     // one b
            testCase("aa", false),   // two a's - should reject
            testCase("bb", false),   // two b's - should reject
            testCase("ab", false),   // a then b - should reject
            testCase("ba", false),   // b then a - should reject
            testCase("c", false),    // different character
            testCase("ac", false)    // mixed with different character
        );
    }
    
    @Test
    public void testNestedQuantifiers() {
        convertRegex("(a*)*");
        
        assertMultipleTestCases(
            testCase("", true),      // zero occurrences
            testCase("a", true),     // one a
            testCase("aa", true),    // two a's
            testCase("aaa", true),   // three a's
            testCase("b", false),    // different character
            testCase("ab", false)    // mixed characters
        );
    }
    
    @Test
    public void testComplexQuantifierCombination() {
        convertRegex("a*b+c?");
        
        assertMultipleTestCases(
            testCase("b", true),     // zero a's, one b, zero c's
            testCase("bb", true),    // zero a's, two b's, zero c's
            testCase("bc", true),    // zero a's, one b, one c
            testCase("bbc", true),   // zero a's, two b's, one c
            testCase("ab", true),    // one a, one b, zero c's
            testCase("abb", true),   // one a, two b's, zero c's
            testCase("abc", true),   // one a, one b, one c
            testCase("abbb", true),  // one a, three b's, zero c's
            testCase("abbbc", true), // one a, three b's, one c
            testCase("", false),     // empty string - no b's
            testCase("a", false),    // one a, no b's
            testCase("c", false),    // no a's, no b's, one c
            testCase("ac", false),   // one a, no b's, one c
            testCase("bbcc", false), // no a's, two b's, two c's
            testCase("aabb", false)  // two a's, two b's, no c's
        );
    }
}
