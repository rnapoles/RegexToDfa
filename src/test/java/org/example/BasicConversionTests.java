package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for basic regex conversion functionality.
 * Tests simple patterns without complex operators.
 */
public class BasicConversionTests extends BaseRegexToDFATest {
    
    @Test
    public void testSingleCharacter() {
        convertRegex("a");
        
        assertMultipleTestCases(
            testCase("a", true),
            testCase("", false),
            testCase("aa", false),
            testCase("b", false),
            testCase("ab", false)
        );
    }
    
    @Test
    public void testEmptyRegex() {
        convertRegex("");
        
        assertMultipleTestCases(
            testCase("", true),
            testCase("a", false),
            testCase("abc", false)
        );
    }
    
    @Test
    public void testNullRegex() {
        assertThrowsIllegalArgumentException(null, "Regex cannot be null");
    }
    
    @Test
    public void testSimpleConcatenation() {
        convertRegex("ab");
        
        assertMultipleTestCases(
            testCase("ab", true),
            testCase("", false),
            testCase("a", false),
            testCase("b", false),
            testCase("ba", false),
            testCase("abc", false),
            testCase("aab", false)
        );
    }
    
    @Test
    public void testThreeCharacterConcatenation() {
        convertRegex("abc");
        
        assertMultipleTestCases(
            testCase("abc", true),
            testCase("", false),
            testCase("a", false),
            testCase("ab", false),
            testCase("bc", false),
            testCase("abcd", false),
            testCase("ab", false)
        );
    }
    
    @Test
    public void testUnion() {
        convertRegex("a|b");
        
        assertMultipleTestCases(
            testCase("a", true),
            testCase("b", true),
            testCase("", false),
            testCase("ab", false),
            testCase("ba", false),
            testCase("c", false)
        );
    }
    
    @Test
    public void testUnionWithConcatenation() {
        convertRegex("ab|cd");
        
        assertMultipleTestCases(
            testCase("ab", true),
            testCase("cd", true),
            testCase("", false),
            testCase("a", false),
            testCase("b", false),
            testCase("c", false),
            testCase("d", false),
            testCase("ac", false),
            testCase("bd", false),
            testCase("abcd", false)
        );
    }
    
    @Test
    public void testComplexUnion() {
        convertRegex("a|b|c");
        
        assertMultipleTestCases(
            testCase("a", true),
            testCase("b", true),
            testCase("c", true),
            testCase("", false),
            testCase("ab", false),
            testCase("ac", false),
            testCase("bc", false),
            testCase("abc", false)
        );
    }
    
    @Test
    public void testParenthesesGrouping() {
        convertRegex("(ab)");
        
        assertMultipleTestCases(
            testCase("ab", true),
            testCase("", false),
            testCase("a", false),
            testCase("b", false),
            testCase("ba", false)
        );
    }
    
    @Test
    public void testNestedParentheses() {
        convertRegex("((ab))");
        
        assertMultipleTestCases(
            testCase("ab", true),
            testCase("", false),
            testCase("a", false),
            testCase("b", false)
        );
    }
    
    @Test
    public void testParenthesesWithUnion() {
        convertRegex("(a|b)c");
        
        assertMultipleTestCases(
            testCase("ac", true),
            testCase("bc", true),
            testCase("", false),
            testCase("a", false),
            testCase("b", false),
            testCase("c", false),
            testCase("abc", false)
        );
    }
}
