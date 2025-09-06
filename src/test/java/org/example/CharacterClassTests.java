package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for character class functionality: [abc], [a-z], [^abc]
 * Tests character classes, ranges, and negation.
 */
public class CharacterClassTests extends BaseRegexToDFATest {
    
    @Test
    public void testSimpleCharacterClass() {
        convertRegex("[abc]");
        
        assertMultipleTestCases(
            testCase("a", true),
            testCase("b", true),
            testCase("c", true),
            testCase("", false),
            testCase("d", false),
            testCase("ab", false),
            testCase("aa", false)
        );
    }
    
    @Test
    public void testCharacterRange() {
        convertRegex("[a-z]");
        
        assertMultipleTestCases(
            testCase("a", true),
            testCase("m", true),
            testCase("z", true),
            testCase("", false),
            testCase("A", false),    // uppercase
            testCase("0", false),    // digit
            testCase("aa", false),   // multiple characters
            testCase("ab", false)    // multiple characters
        );
    }
    
    @Test
    public void testDigitRange() {
        convertRegex("[0-9]");
        
        assertMultipleTestCases(
            testCase("0", true),
            testCase("5", true),
            testCase("9", true),
            testCase("", false),
            testCase("a", false),     // letter
            testCase("00", false),    // multiple digits
            testCase("12", false)    // multiple digits
        );
    }
    
    @Test
    public void testUppercaseRange() {
        convertRegex("[A-Z]");
        
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
    public void testNegatedCharacterClass() {
        convertRegex("[^abc]");
        
        assertMultipleTestCases(
            testCase("d", true),
            testCase("e", true),
            testCase("z", true),
            testCase("0", true),
            testCase("A", true),
            testCase("", false),
            testCase("a", false),     // should reject
            testCase("b", false),     // should reject
            testCase("c", false),     // should reject
            testCase("ab", false),    // multiple characters
            testCase("ad", false)     // mixed (contains rejected char)
        );
    }
    
    @Test
    public void testNegatedRange() {
        convertRegex("[^a-z]");
        
        assertMultipleTestCases(
            testCase("A", true),     // uppercase
            testCase("0", true),     // digit
            testCase("!", true),     // punctuation
            testCase("", false),
            testCase("a", false),    // lowercase - should reject
            testCase("m", false),    // lowercase - should reject
            testCase("z", false),    // lowercase - should reject
            testCase("Aa", false),   // mixed
            testCase("a0", false)    // mixed
        );
    }
    
    @Test
    public void testMixedCharacterClass() {
        convertRegex("[a-zA-Z0-9]");
        
        assertMultipleTestCases(
            testCase("a", true),     // lowercase
            testCase("Z", true),     // uppercase
            testCase("0", true),     // digit
            testCase("9", true),     // digit
            testCase("", false),
            testCase("!", false),    // punctuation
            testCase("@", false),    // punctuation
            testCase("aa", false),   // multiple characters
            testCase("a0", false)    // multiple characters
        );
    }
    
    @Test
    public void testCharacterClassWithQuantifier() {
        convertRegex("[abc]*");
        
        assertMultipleTestCases(
            testCase("", true),      // zero occurrences
            testCase("a", true),     // one occurrence
            testCase("b", true),      // one occurrence
            testCase("c", true),      // one occurrence
            testCase("ab", true),     // two occurrences
            testCase("abc", true),    // three occurrences
            testCase("aabbcc", true), // six occurrences
            testCase("d", false),     // different character
            testCase("ad", false),    // mixed
            testCase("abcd", false)   // mixed
        );
    }
    
    @Test
    public void testCharacterClassWithPlus() {
        convertRegex("[abc]+");
        
        assertMultipleTestCases(
            testCase("", false),     // zero occurrences - should reject
            testCase("a", true),     // one occurrence
            testCase("b", true),      // one occurrence
            testCase("c", true),      // one occurrence
            testCase("ab", true),     // two occurrences
            testCase("abc", true),    // three occurrences
            testCase("aabbcc", true), // six occurrences
            testCase("d", false),     // different character
            testCase("ad", false),    // mixed
            testCase("abcd", false)   // mixed
        );
    }
    
    @Test
    public void testCharacterClassWithQuestion() {
        convertRegex("[abc]?");
        
        assertMultipleTestCases(
            testCase("", true),      // zero occurrences
            testCase("a", true),     // one occurrence
            testCase("b", true),      // one occurrence
            testCase("c", true),      // one occurrence
            testCase("ab", false),    // two occurrences - should reject
            testCase("abc", false),   // three occurrences - should reject
            testCase("d", false),     // different character
            testCase("ad", false),    // mixed
            testCase("abcd", false)   // mixed
        );
    }
    
    @Test
    public void testCharacterClassWithConcatenation() {
        convertRegex("[abc][def]");
        
        assertMultipleTestCases(
            testCase("ad", true),
            testCase("be", true),
            testCase("cf", true),
            testCase("", false),
            testCase("a", false),
            testCase("d", false),
            testCase("ab", false),
            testCase("de", false),
            testCase("adf", false),
            testCase("ade", false)
        );
    }
    
    @Test
    public void testCharacterClassWithUnion() {
        convertRegex("[abc]|[def]");
        
        assertMultipleTestCases(
            testCase("a", true),
            testCase("b", true),
            testCase("c", true),
            testCase("d", true),
            testCase("e", true),
            testCase("f", true),
            testCase("", false),
            testCase("g", false),
            testCase("ab", false),
            testCase("de", false),
            testCase("ad", false)
        );
    }
    
    @Test
    public void testComplexCharacterClassPattern() {
        convertRegex("[a-z]+[0-9]*");
        
        assertMultipleTestCases(
            testCase("a", true),      // one letter, zero digits
            testCase("abc", true),    // three letters, zero digits
            testCase("a0", true),     // one letter, one digit
            testCase("abc123", true), // three letters, three digits
            testCase("", false),      // empty string
            testCase("0", false),     // digit only
            testCase("123", false),   // digits only
            testCase("A", false),     // uppercase letter
            testCase("a0b", false),   // mixed pattern
            testCase("0a", false)     // digit then letter
        );
    }
    
    @Test
    public void testInvalidCharacterRange() {
        // Test invalid range where start > end
        assertThrowsIllegalArgumentException("[z-a]", "Invalid character range");
    }
    
    @Test
    public void testUnclosedCharacterClass() {
        assertThrowsIllegalArgumentException("[abc", "Unclosed character class");
    }
    
    @Test
    public void testEmptyCharacterClass() {
        // Empty character class should be invalid
        assertThrowsIllegalArgumentException("[]", "Invalid regex");
    }
}
