package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for edge cases and special scenarios.
 * Tests boundary conditions, special characters, and unusual inputs.
 */
public class EdgeCaseTests extends BaseRegexToDFATest {
    
    @Test
    public void testDotMetacharacter() {
        convertRegex(".");
        
        // Dot should match any printable ASCII character
        assertMultipleTestCases(
            testCase("a", true),
            testCase("Z", true),
            testCase("0", true),
            testCase("!", true),
            testCase("@", true),
            testCase("#", true),
            testCase("$", true),
            testCase("%", true),
            testCase("^", true),
            testCase("&", true),
            testCase("*", true),
            testCase("(", true),
            testCase(")", true),
            testCase("-", true),
            testCase("_", true),
            testCase("+", true),
            testCase("=", true),
            testCase("[", true),
            testCase("]", true),
            testCase("{", true),
            testCase("}", true),
            testCase("|", true),
            testCase("\\", true),
            testCase(";", true),
            testCase(":", true),
            testCase("'", true),
            testCase("\"", true),
            testCase(",", true),
            testCase(".", true),
            testCase("<", true),
            testCase(">", true),
            testCase("/", true),
            testCase("?", true),
            testCase("~", true),
            testCase("`", true),
            testCase("", false),     // empty string
            testCase("aa", false),   // multiple characters
            testCase("ab", false)    // multiple characters
        );
    }
    
    @Test
    public void testDotWithQuantifier() {
        convertRegex(".*");
        
        assertMultipleTestCases(
            testCase("", true),      // zero occurrences
            testCase("a", true),     // one character
            testCase("ab", true),    // two characters
            testCase("abc", true),   // three characters
            testCase("a1!", true),   // mixed characters
            testCase("!@#$%", true), // special characters
            testCase("a\nb", true),  // including newline
            testCase("a\tb", true),  // including tab
            testCase("a b", true)    // including space
        );
    }
    
    @Test
    public void testDotWithPlus() {
        convertRegex(".+");
        
        assertMultipleTestCases(
            testCase("", false),     // zero occurrences - should reject
            testCase("a", true),     // one character
            testCase("ab", true),    // two characters
            testCase("abc", true),   // three characters
            testCase("a1!", true),   // mixed characters
            testCase("!@#$%", true), // special characters
            testCase("a\nb", true),  // including newline
            testCase("a\tb", true),  // including tab
            testCase("a b", true)    // including space
        );
    }
    
    @Test
    public void testDotWithQuestion() {
        convertRegex(".?");
        
        assertMultipleTestCases(
            testCase("", true),      // zero occurrences
            testCase("a", true),     // one character
            testCase("ab", false),   // two characters - should reject
            testCase("abc", false),  // three characters - should reject
            testCase("a1!", false),  // mixed characters - should reject
            testCase("!@#$%", false) // special characters - should reject
        );
    }
    
    @Test
    public void testSpecialCharacters() {
        convertRegex("\\*\\+\\?\\|\\(\\)\\[\\]\\{\\}\\^\\$\\\\");
        
        assertMultipleTestCases(
            testCase("*+?|()[]{}$\\", true),
            testCase("", false),
            testCase("*", false),
            testCase("+", false),
            testCase("?", false),
            testCase("|", false),
            testCase("(", false),
            testCase(")", false),
            testCase("[", false),
            testCase("]", false),
            testCase("{", false),
            testCase("}", false),
            testCase("^", false),
            testCase("$", false),
            testCase("\\", false),
            testCase("a", false),
            testCase("ab", false)
        );
    }
    
    @Test
    public void testSpecialCharactersWithQuantifier() {
        convertRegex("\\*\\+\\?\\|\\(\\)\\[\\]\\{\\}\\^\\$\\\\*");
        
        assertMultipleTestCases(
            testCase("", true),      // zero occurrences
            testCase("*+?|()[]{}$\\", true), // one occurrence
            testCase("*+?|()[]{}$\\*+?|()[]{}$\\", true), // two occurrences
            testCase("a", false),    // different character
            testCase("ab", false)    // different characters
        );
    }
    
    @Test
    public void testUnicodeCharacters() {
        convertRegex("[\u0041-\u005A]"); // ASCII A-Z
        
        assertMultipleTestCases(
            testCase("A", true),
            testCase("M", true),
            testCase("Z", true),
            testCase("", false),
            testCase("a", false),     // lowercase
            testCase("0", false),     // digit
            testCase("AA", false),    // multiple characters
            testCase("AB", false)     // multiple characters
        );
    }
    
    @Test
    public void testVeryLongPattern() {
        StringBuilder longPattern = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longPattern.append("a");
        }
        
        convertRegex(longPattern.toString());
        
        assertMultipleTestCases(
            testCase(longPattern.toString(), true),
            testCase("", false),
            testCase("a", false),
            testCase("aa", false),
            testCase(longPattern.toString() + "a", false)
        );
    }
    
    @Test
    public void testVeryLongInput() {
        convertRegex("a*");
        
        StringBuilder longInput = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longInput.append("a");
        }
        
        assertAccepts(longInput.toString(), true);
    }
    
    @Test
    public void testEmptyStringPattern() {
        convertRegex("");
        
        assertMultipleTestCases(
            testCase("", true),
            testCase("a", false),
            testCase("abc", false)
        );
    }
    
    @Test
    public void testSingleCharacterPattern() {
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
    public void testTwoCharacterPattern() {
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
    public void testThreeCharacterPattern() {
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
    public void testPatternWithOnlyQuantifiers() {
        convertRegex("a*b*c*");
        
        assertMultipleTestCases(
            testCase("", true),      // zero occurrences of each
            testCase("a", true),     // one a, zero b's, zero c's
            testCase("b", true),     // zero a's, one b, zero c's
            testCase("c", true),     // zero a's, zero b's, one c
            testCase("ab", true),    // one a, one b, zero c's
            testCase("ac", true),    // one a, zero b's, one c
            testCase("bc", true),    // zero a's, one b, one c
            testCase("abc", true),   // one a, one b, one c
            testCase("aabbcc", true), // two a's, two b's, two c's
            testCase("d", false),    // different character
            testCase("abd", false),  // mixed with different character
            testCase("bac", false)   // wrong order
        );
    }
    
    @Test
    public void testPatternWithOnlyUnions() {
        convertRegex("a|b|c|d|e");
        
        assertMultipleTestCases(
            testCase("a", true),
            testCase("b", true),
            testCase("c", true),
            testCase("d", true),
            testCase("e", true),
            testCase("", false),
            testCase("ab", false),
            testCase("ac", false),
            testCase("f", false),
            testCase("aa", false)
        );
    }
    
    @Test
    public void testPatternWithOnlyConcatenations() {
        convertRegex("abcdef");
        
        assertMultipleTestCases(
            testCase("abcdef", true),
            testCase("", false),
            testCase("a", false),
            testCase("ab", false),
            testCase("abc", false),
            testCase("abcd", false),
            testCase("abcde", false),
            testCase("abcdefg", false),
            testCase("fedcba", false)
        );
    }
    
    @Test
    public void testPatternWithMixedQuantifiers() {
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
    
    @Test
    public void testPatternWithNestedQuantifiers() {
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
    public void testPatternWithDeepNesting() {
        convertRegex("((a|b)|(c|d))");
        
        assertMultipleTestCases(
            testCase("a", true),
            testCase("b", true),
            testCase("c", true),
            testCase("d", true),
            testCase("", false),
            testCase("ab", false),
            testCase("cd", false),
            testCase("ac", false),
            testCase("bd", false)
        );
    }
    
    @Test
    public void testPatternWithVeryDeepNesting() {
        convertRegex("((((a))))");
        
        assertMultipleTestCases(
            testCase("a", true),
            testCase("", false),
            testCase("b", false),
            testCase("aa", false)
        );
    }
    
    @Test
    public void testPatternWithVeryWideNesting() {
        convertRegex("(a|b|c|d|e|f|g|h|i|j)");
        
        assertMultipleTestCases(
            testCase("a", true),
            testCase("b", true),
            testCase("c", true),
            testCase("d", true),
            testCase("e", true),
            testCase("f", true),
            testCase("g", true),
            testCase("h", true),
            testCase("i", true),
            testCase("j", true),
            testCase("", false),
            testCase("k", false),
            testCase("ab", false)
        );
    }
}
