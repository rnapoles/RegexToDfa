package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the preprocessRegex() method of RegexToDFAConverter.
 * 
 * Single Responsibility: Tests only regex preprocessing functionality
 * Open/Closed: Extensible for new preprocessing scenarios
 * Liskov Substitution: Maintains consistent testing interface
 * Interface Segregation: Focused on preprocessing testing only
 * Dependency Inversion: Depends on abstractions (preprocessing interface)
 */
public class RegexToDFAConverterPreprocessingTest extends RegexToDFAConverterBaseTest {
    
    @Test
    public void testPreprocessSimpleRegex() throws Exception {
        // Test simple regex without special characters
        String result = (String) invokePrivateMethod("preprocessRegex", "abc");
        assertEquals("abc", result, "Simple regex should remain unchanged");
    }
    
    @Test
    public void testPreprocessEscapeSequences() throws Exception {
        // Test escape sequences
        String result1 = (String) invokePrivateMethod("preprocessRegex", "\\.");
        assertEquals("\\.", result1, "Escaped dot should remain escaped");
        
        String result2 = (String) invokePrivateMethod("preprocessRegex", "\\*");
        assertEquals("\\*", result2, "Escaped star should remain escaped");
        
        String result3 = (String) invokePrivateMethod("preprocessRegex", "\\+");
        assertEquals("\\+", result3, "Escaped plus should remain escaped");
        
        String result4 = (String) invokePrivateMethod("preprocessRegex", "\\?");
        assertEquals("\\?", result4, "Escaped question should remain escaped");
        
        String result5 = (String) invokePrivateMethod("preprocessRegex", "\\|");
        assertEquals("\\|", result5, "Escaped pipe should remain escaped");
        
        String result6 = (String) invokePrivateMethod("preprocessRegex", "\\(");
        assertEquals("\\(", result6, "Escaped opening paren should remain escaped");
        
        String result7 = (String) invokePrivateMethod("preprocessRegex", "\\)");
        assertEquals("\\)", result7, "Escaped closing paren should remain escaped");
        
        String result8 = (String) invokePrivateMethod("preprocessRegex", "\\[");
        assertEquals("\\[", result8, "Escaped opening bracket should remain escaped");
        
        String result9 = (String) invokePrivateMethod("preprocessRegex", "\\]");
        assertEquals("\\]", result9, "Escaped closing bracket should remain escaped");
        
        String result10 = (String) invokePrivateMethod("preprocessRegex", "\\\\");
        assertEquals("\\\\", result10, "Escaped backslash should remain escaped");
    }
    
    @Test
    public void testPreprocessWhitespaceEscapeSequences() throws Exception {
        // Test whitespace escape sequences
        String result1 = (String) invokePrivateMethod("preprocessRegex", "\\s");
        assertEquals("[\\s]", result1, "\\s should be converted to character class");
        
        String result2 = (String) invokePrivateMethod("preprocessRegex", "\\t");
        assertEquals("\\t", result2, "\\t should remain as escape sequence");
        
        String result3 = (String) invokePrivateMethod("preprocessRegex", "\\n");
        assertEquals("\\n", result3, "\\n should remain as escape sequence");
        
        String result4 = (String) invokePrivateMethod("preprocessRegex", "\\r");
        assertEquals("\\r", result4, "\\r should remain as escape sequence");
    }
    
    @Test
    public void testPreprocessHexEscapeSequences() throws Exception {
        // Test hex escape sequences
        String result1 = (String) invokePrivateMethod("preprocessRegex", "\\x20");
        assertEquals(" ", result1, "\\x20 should be converted to space character");
        
        String result2 = (String) invokePrivateMethod("preprocessRegex", "\\x41");
        assertEquals("A", result2, "\\x41 should be converted to 'A'");
        
        String result3 = (String) invokePrivateMethod("preprocessRegex", "\\x61");
        assertEquals("a", result3, "\\x61 should be converted to 'a'");
        
        String result4 = (String) invokePrivateMethod("preprocessRegex", "\\x7E");
        assertEquals("~", result4, "\\x7E should be converted to '~'");
    }
    
    @Test
    public void testPreprocessInvalidHexEscapeSequences() throws Exception {
        // Test invalid hex escape sequences
        assertThrowsIllegalArgumentException("preprocessRegex", "\\x", "Invalid hex character");
        assertThrowsIllegalArgumentException("preprocessRegex", "\\xG", "Invalid hex character");
        assertThrowsIllegalArgumentException("preprocessRegex", "\\xGG", "Invalid hex character");
        assertThrowsIllegalArgumentException("preprocessRegex", "\\x1", "Invalid hex character");
    }
    
    @Test
    public void testPreprocessCharacterClasses() throws Exception {
        // Test character classes
        String result1 = (String) invokePrivateMethod("preprocessRegex", "[abc]");
        assertEquals("[abc]", result1, "Simple character class should remain unchanged");
        
        String result2 = (String) invokePrivateMethod("preprocessRegex", "[a-z]");
        assertEquals("[a-z]", result2, "Character range should remain unchanged");
        
        String result3 = (String) invokePrivateMethod("preprocessRegex", "[^abc]");
        assertEquals("[^abc]", result3, "Negated character class should remain unchanged");
        
        String result4 = (String) invokePrivateMethod("preprocessRegex", "[a-zA-Z0-9]");
        assertEquals("[a-zA-Z0-9]", result4, "Complex character class should remain unchanged");
    }
    
    @Test
    public void testPreprocessInvalidCharacterClasses() throws Exception {
        // Test invalid character classes
        assertThrowsIllegalArgumentException("preprocessRegex", "[]", "Invalid regex: empty character class");
        assertThrowsIllegalArgumentException("preprocessRegex", "[", "Unclosed character class");
        assertThrowsIllegalArgumentException("preprocessRegex", "[[", "Nested character classes are not supported");
        assertThrowsIllegalArgumentException("preprocessRegex", "\\]", "Invalid regex");
    }
    
    @Test
    public void testPreprocessCharacterRanges() throws Exception {
        // Test character ranges
        String result1 = (String) invokePrivateMethod("preprocessRegex", "[a-z]");
        assertEquals("[a-z]", result1, "Valid character range should remain unchanged");
        
        String result2 = (String) invokePrivateMethod("preprocessRegex", "[0-9]");
        assertEquals("[0-9]", result2, "Valid digit range should remain unchanged");
        
        String result3 = (String) invokePrivateMethod("preprocessRegex", "[A-Za-z]");
        assertEquals("[A-Za-z]", result3, "Valid letter range should remain unchanged");
    }
    
    @Test
    public void testPreprocessInvalidCharacterRanges() throws Exception {
        // Test invalid character ranges
        assertThrowsIllegalArgumentException("preprocessRegex", "[z-a]", "Invalid character range: z-a");
        assertThrowsIllegalArgumentException("preprocessRegex", "[9-0]", "Invalid character range: 9-0");
        assertThrowsIllegalArgumentException("preprocessRegex", "[-a]", "Invalid regex");
        assertThrowsIllegalArgumentException("preprocessRegex", "[a-]", "Invalid regex");
    }
    
    @Test
    public void testPreprocessHexCharacterRanges() throws Exception {
        // Test hex character ranges
        String result1 = (String) invokePrivateMethod("preprocessRegex", "[\\x20-\\x7E]");
        assertEquals("[\\x20-\\x7E]", result1, "Valid hex range should remain unchanged");
        
        String result2 = (String) invokePrivateMethod("preprocessRegex", "[\\x41-\\x5A]");
        assertEquals("[\\x41-\\x5A]", result2, "Valid hex range should remain unchanged");
    }
    
    @Test
    public void testPreprocessInvalidHexCharacterRanges() throws Exception {
        // Test invalid hex character ranges
        assertThrowsIllegalArgumentException("preprocessRegex", "[\\x7E-\\x20]", "Invalid hex range");
        assertThrowsIllegalArgumentException("preprocessRegex", "[\\x5A-\\x41]", "Invalid hex range");
    }
    
    @Test
    public void testPreprocessLiteralHyphen() throws Exception {
        // Test literal hyphen in character class
        String result1 = (String) invokePrivateMethod("preprocessRegex", "[-abc]");
        assertEquals("[-abc]", result1, "Hyphen at start should be literal");
        
        String result2 = (String) invokePrivateMethod("preprocessRegex", "[abc-]");
        assertEquals("[abc-]", result2, "Hyphen at end should be literal");
        
        String result3 = (String) invokePrivateMethod("preprocessRegex", "[a\\-c]");
        assertEquals("[a\\-c]", result3, "Escaped hyphen should be literal");
    }
    
    @Test
    public void testPreprocessWildcard() throws Exception {
        // Test wildcard
        String result = (String) invokePrivateMethod("preprocessRegex", ".");
        assertEquals(".", result, "Wildcard should remain unchanged");
    }
    
    @Test
    public void testPreprocessQuantifiers() throws Exception {
        // Test quantifiers
        String result1 = (String) invokePrivateMethod("preprocessRegex", "a{2}");
        assertEquals("a{2}", result1, "Exact quantifier should remain unchanged");
        
        String result2 = (String) invokePrivateMethod("preprocessRegex", "a{2,4}");
        assertEquals("a{2,4}", result2, "Range quantifier should remain unchanged");
        
        String result3 = (String) invokePrivateMethod("preprocessRegex", "a{2,}");
        assertEquals("a{2,}", result3, "At-least quantifier should remain unchanged");
        
        String result4 = (String) invokePrivateMethod("preprocessRegex", "a{,4}");
        assertEquals("a{,4}", result4, "At-most quantifier should remain unchanged");
    }
    
    @Test
    public void testPreprocessComplexRegex() throws Exception {
        // Test complex regex with multiple features
        String result = (String) invokePrivateMethod("preprocessRegex", "(a|b)*[0-9]+\\.");
        assertEquals("(a|b)*[0-9]+\\.", result, "Complex regex should be processed correctly");
    }
    
    @Test
    public void testPreprocessEmptyString() throws Exception {
        // Test empty string
        String result = (String) invokePrivateMethod("preprocessRegex", "");
        assertEquals("", result, "Empty string should remain empty");
    }
    
    @Test
    public void testPreprocessIncompleteEscapeSequence() throws Exception {
        // Test incomplete escape sequence
        assertThrowsIllegalArgumentException("preprocessRegex", "\\", "Incomplete escape sequence");
    }
    
    @Test
    public void testPreprocessUnclosedCharacterClass() throws Exception {
        // Test unclosed character class
        assertThrowsIllegalArgumentException("preprocessRegex", "[abc", "Unclosed character class");
    }
    
    @Test
    public void testPreprocessNestedCharacterClasses() throws Exception {
        // Test nested character classes
        assertThrowsIllegalArgumentException("preprocessRegex", "[[abc]]", "Nested character classes are not supported");
    }
    
    @Test
    public void testPreprocessEscapeSequencesInCharacterClass() throws Exception {
        // Test escape sequences in character class
        String result1 = (String) invokePrivateMethod("preprocessRegex", "[\\t\\n\\r]");
        assertEquals("[\\t\\n\\r]", result1, "Escape sequences in character class should remain unchanged");
        
        String result2 = (String) invokePrivateMethod("preprocessRegex", "[\\x20-\\x7E]");
        assertEquals("[\\x20-\\x7E]", result2, "Hex ranges in character class should remain unchanged");
    }
    
    @Test
    public void testPreprocessInvalidEscapeInCharacterClass() throws Exception {
        // Test invalid escape sequences in character class
        assertThrowsIllegalArgumentException("preprocessRegex", "[\\]", "Invalid regex");
        assertThrowsIllegalArgumentException("preprocessRegex", "[\\x]", "Invalid regex");
        assertThrowsIllegalArgumentException("preprocessRegex", "[\\xG]", "Invalid regex");
    }
    
    @Test
    public void testPreprocessMixedContent() throws Exception {
        // Test mixed content with various features
        String result = (String) invokePrivateMethod("preprocessRegex", "a\\.[0-9]+\\s\\t");
        assertEquals("a\\.[0-9]+[\\s]\\t", result, "Mixed content should be processed correctly");
    }
    
    @Test
    public void testPreprocessSpecialCharacters() throws Exception {
        // Test special characters that should be escaped
        String result1 = (String) invokePrivateMethod("preprocessRegex", "\\^");
        assertEquals("\\^", result1, "Escaped caret should remain escaped");
        
        String result2 = (String) invokePrivateMethod("preprocessRegex", "\\$");
        assertEquals("\\$", result2, "Escaped dollar should remain escaped");
    }
    
    @Test
    public void testPreprocessUnrecognizedEscapeSequence() throws Exception {
        // Test unrecognized escape sequences
        String result1 = (String) invokePrivateMethod("preprocessRegex", "\\z");
        assertEquals("z", result1, "Unrecognized escape sequence should be treated as literal");
        
        String result2 = (String) invokePrivateMethod("preprocessRegex", "\\@");
        assertEquals("@", result2, "Unrecognized escape sequence should be treated as literal");
    }
}
