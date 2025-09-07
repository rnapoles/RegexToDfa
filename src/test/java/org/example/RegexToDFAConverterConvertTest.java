package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the main convert() method of RegexToDFAConverter.
 * 
 * Single Responsibility: Tests only the convert() method functionality
 * Open/Closed: Extensible for new conversion scenarios
 * Liskov Substitution: Maintains consistent testing interface
 * Interface Segregation: Focused on conversion testing only
 * Dependency Inversion: Depends on abstractions (DFA interface)
 */
public class RegexToDFAConverterConvertTest extends RegexToDFAConverterBaseTest {
    
    @Test
    public void testConvertNullRegex() {
        // Test null input
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            RegexToDFAConverter.convert(null);
        });
        assertEquals("Regex cannot be null", exception.getMessage());
    }
    
    @Test
    public void testConvertEmptyRegex() {
        // Test empty regex - should accept only empty strings
        DFA dfa = RegexToDFAConverter.convert("");
        assertNotNull(dfa, "DFA should not be null");
        assertTrue(dfa.accepts(""), "Empty DFA should accept empty string");
        assertFalse(dfa.accepts("a"), "Empty DFA should not accept non-empty string");
        assertFalse(dfa.accepts("ab"), "Empty DFA should not accept non-empty string");
    }
    
    @Test
    public void testConvertSimpleCharacter() {
        // Test simple character regex
        DFA dfa = RegexToDFAConverter.convert("a");
        assertNotNull(dfa, "DFA should not be null");
        assertTrue(dfa.accepts("a"), "DFA should accept 'a'");
        assertFalse(dfa.accepts(""), "DFA should not accept empty string");
        assertFalse(dfa.accepts("b"), "DFA should not accept 'b'");
        assertFalse(dfa.accepts("aa"), "DFA should not accept 'aa'");
    }
    
    @Test
    public void testConvertConcatenation() {
        // Test concatenation
        DFA dfa = RegexToDFAConverter.convert("ab");
        assertNotNull(dfa, "DFA should not be null");
        assertTrue(dfa.accepts("ab"), "DFA should accept 'ab'");
        assertFalse(dfa.accepts(""), "DFA should not accept empty string");
        assertFalse(dfa.accepts("a"), "DFA should not accept 'a'");
        assertFalse(dfa.accepts("b"), "DFA should not accept 'b'");
        assertFalse(dfa.accepts("ba"), "DFA should not accept 'ba'");
        assertFalse(dfa.accepts("abc"), "DFA should not accept 'abc'");
    }
    
    @Test
    public void testConvertUnion() {
        // Test union (alternation)
        DFA dfa = RegexToDFAConverter.convert("a|b");
        assertNotNull(dfa, "DFA should not be null");
        assertTrue(dfa.accepts("a"), "DFA should accept 'a'");
        assertTrue(dfa.accepts("b"), "DFA should accept 'b'");
        assertFalse(dfa.accepts(""), "DFA should not accept empty string");
        assertFalse(dfa.accepts("ab"), "DFA should not accept 'ab'");
        assertFalse(dfa.accepts("ba"), "DFA should not accept 'ba'");
    }
    
    @Test
    public void testConvertStar() {
        // Test Kleene star (zero or more)
        DFA dfa = RegexToDFAConverter.convert("a*");
        assertNotNull(dfa, "DFA should not be null");
        assertTrue(dfa.accepts(""), "DFA should accept empty string");
        assertTrue(dfa.accepts("a"), "DFA should accept 'a'");
        assertTrue(dfa.accepts("aa"), "DFA should accept 'aa'");
        assertTrue(dfa.accepts("aaa"), "DFA should accept 'aaa'");
        assertFalse(dfa.accepts("b"), "DFA should not accept 'b'");
        assertFalse(dfa.accepts("ab"), "DFA should not accept 'ab'");
    }
    
    @Test
    public void testConvertPlus() {
        // Test plus (one or more)
        DFA dfa = RegexToDFAConverter.convert("a+");
        assertNotNull(dfa, "DFA should not be null");
        assertFalse(dfa.accepts(""), "DFA should not accept empty string");
        assertTrue(dfa.accepts("a"), "DFA should accept 'a'");
        assertTrue(dfa.accepts("aa"), "DFA should accept 'aa'");
        assertTrue(dfa.accepts("aaa"), "DFA should accept 'aaa'");
        assertFalse(dfa.accepts("b"), "DFA should not accept 'b'");
        assertFalse(dfa.accepts("ab"), "DFA should not accept 'ab'");
    }
    
    @Test
    public void testConvertQuestion() {
        // Test question mark (zero or one)
        DFA dfa = RegexToDFAConverter.convert("a?");
        assertNotNull(dfa, "DFA should not be null");
        assertTrue(dfa.accepts(""), "DFA should accept empty string");
        assertTrue(dfa.accepts("a"), "DFA should accept 'a'");
        assertFalse(dfa.accepts("aa"), "DFA should not accept 'aa'");
        assertFalse(dfa.accepts("b"), "DFA should not accept 'b'");
        assertFalse(dfa.accepts("ab"), "DFA should not accept 'ab'");
    }
    
    @Test
    public void testConvertParentheses() {
        // Test parentheses for grouping
        DFA dfa = RegexToDFAConverter.convert("(a|b)*");
        assertNotNull(dfa, "DFA should not be null");
        assertTrue(dfa.accepts(""), "DFA should accept empty string");
        assertTrue(dfa.accepts("a"), "DFA should accept 'a'");
        assertTrue(dfa.accepts("b"), "DFA should accept 'b'");
        assertTrue(dfa.accepts("ab"), "DFA should accept 'ab'");
        assertTrue(dfa.accepts("ba"), "DFA should accept 'ba'");
        assertTrue(dfa.accepts("aab"), "DFA should accept 'aab'");
        assertFalse(dfa.accepts("c"), "DFA should not accept 'c'");
    }
    
    @Test
    public void testConvertCharacterClass() {
        // Test character class
        DFA dfa = RegexToDFAConverter.convert("[abc]");
        assertNotNull(dfa, "DFA should not be null");
        assertTrue(dfa.accepts("a"), "DFA should accept 'a'");
        assertTrue(dfa.accepts("b"), "DFA should accept 'b'");
        assertTrue(dfa.accepts("c"), "DFA should accept 'c'");
        assertFalse(dfa.accepts(""), "DFA should not accept empty string");
        assertFalse(dfa.accepts("d"), "DFA should not accept 'd'");
        assertFalse(dfa.accepts("ab"), "DFA should not accept 'ab'");
    }
    
    @Test
    public void testConvertEscapeSequence() {
        // Test escape sequence
        DFA dfa = RegexToDFAConverter.convert("\\.");
        assertNotNull(dfa, "DFA should not be null");
        assertTrue(dfa.accepts("."), "DFA should accept '.'");
        assertFalse(dfa.accepts(""), "DFA should not accept empty string");
        assertFalse(dfa.accepts("a"), "DFA should not accept 'a'");
    }
    
    @Test
    public void testConvertComplexPattern() {
        // Test complex pattern: (a|b)*c
        DFA dfa = RegexToDFAConverter.convert("(a|b)*c");
        assertNotNull(dfa, "DFA should not be null");
        assertTrue(dfa.accepts("c"), "DFA should accept 'c'");
        assertTrue(dfa.accepts("ac"), "DFA should accept 'ac'");
        assertTrue(dfa.accepts("bc"), "DFA should accept 'bc'");
        assertTrue(dfa.accepts("abc"), "DFA should accept 'abc'");
        assertTrue(dfa.accepts("aabbc"), "DFA should accept 'aabbc'");
        assertFalse(dfa.accepts(""), "DFA should not accept empty string");
        assertFalse(dfa.accepts("a"), "DFA should not accept 'a'");
        assertFalse(dfa.accepts("b"), "DFA should not accept 'b'");
        assertFalse(dfa.accepts("ab"), "DFA should not accept 'ab'");
        assertFalse(dfa.accepts("d"), "DFA should not accept 'd'");
    }
    
    @Test
    public void testConvertInvalidRegex() {
        // Test invalid regex patterns
        String[] invalidRegexes = {
            "*a", "+a", "?a", "|a", "a|", "(a", "a)", "[]", "[a", "\\"
        };
        
        for (String invalidRegex : invalidRegexes) {
            assertThrows(IllegalArgumentException.class, () -> {
                RegexToDFAConverter.convert(invalidRegex);
            }, "Should throw exception for invalid regex: " + invalidRegex);
        }
    }
    
    @Test
    public void testConvertWithQuantifiers() {
        // Test quantifiers
        DFA dfa1 = RegexToDFAConverter.convert("a{2}");
        assertNotNull(dfa1, "DFA should not be null");
        assertTrue(dfa1.accepts("aa"), "DFA should accept 'aa'");
        assertFalse(dfa1.accepts(""), "DFA should not accept empty string");
        assertFalse(dfa1.accepts("a"), "DFA should not accept 'a'");
        assertFalse(dfa1.accepts("aaa"), "DFA should not accept 'aaa'");
        
        DFA dfa2 = RegexToDFAConverter.convert("a{2,4}");
        assertNotNull(dfa2, "DFA should not be null");
        assertTrue(dfa2.accepts("aa"), "DFA should accept 'aa'");
        assertTrue(dfa2.accepts("aaa"), "DFA should accept 'aaa'");
        assertTrue(dfa2.accepts("aaaa"), "DFA should accept 'aaaa'");
        assertFalse(dfa2.accepts(""), "DFA should not accept empty string");
        assertFalse(dfa2.accepts("a"), "DFA should not accept 'a'");
        assertFalse(dfa2.accepts("aaaaa"), "DFA should not accept 'aaaaa'");
    }
    
    @Test
    public void testConvertWithWildcard() {
        // Test wildcard
        DFA dfa = RegexToDFAConverter.convert(".");
        assertNotNull(dfa, "DFA should not be null");
        assertTrue(dfa.accepts("a"), "DFA should accept 'a'");
        assertTrue(dfa.accepts("b"), "DFA should accept 'b'");
        assertTrue(dfa.accepts("1"), "DFA should accept '1'");
        assertTrue(dfa.accepts("@"), "DFA should accept '@'");
        assertFalse(dfa.accepts(""), "DFA should not accept empty string");
        assertFalse(dfa.accepts("ab"), "DFA should not accept 'ab'");
    }
    
    @Test
    public void testConvertResultIsValidDFA() {
        // Test that the result is a valid DFA
        DFA dfa = RegexToDFAConverter.convert("a*b+");
        assertNotNull(dfa, "DFA should not be null");
        
        // Check that DFA has proper structure
        assertNotNull(dfa.getStartState(), "DFA should have a start state");
        assertFalse(dfa.getStates().isEmpty(), "DFA should have states");
        assertFalse(dfa.getAcceptingStates().isEmpty(), "DFA should have accepting states");
        
        // Test deterministic behavior
        String[] testStrings = {"", "a", "b", "ab", "aa", "bb", "aab", "abb"};
        for (String testString : testStrings) {
            // DFA should give consistent results
            boolean result1 = dfa.accepts(testString);
            boolean result2 = dfa.accepts(testString);
            assertEquals(result1, result2, "DFA should be deterministic for: " + testString);
        }
    }
}
