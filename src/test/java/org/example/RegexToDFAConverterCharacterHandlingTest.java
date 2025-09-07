package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for character class and escape sequence handling methods of RegexToDFAConverter.
 * 
 * Single Responsibility: Tests only character handling functionality
 * Open/Closed: Extensible for new character handling scenarios
 * Liskov Substitution: Maintains consistent testing interface
 * Interface Segregation: Focused on character handling testing only
 * Dependency Inversion: Depends on abstractions (character handling interface)
 */
public class RegexToDFAConverterCharacterHandlingTest extends RegexToDFAConverterBaseTest {
    
    @Test
    public void testCreateCharacterClassNFA() throws Exception {
        // Test simple character class
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "abc");
        assertNotNull(result, "Character class NFA should not be null");
        assertTrue(result.accepts("a"), "Character class NFA should accept 'a'");
        assertTrue(result.accepts("b"), "Character class NFA should accept 'b'");
        assertTrue(result.accepts("c"), "Character class NFA should accept 'c'");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("d"), "Character class NFA should not accept 'd'");
        assertFalse(result.accepts("ab"), "Character class NFA should not accept 'ab'");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithRange() throws Exception {
        // Test character class with range
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "a-z");
        assertNotNull(result, "Character class NFA with range should not be null");
        assertTrue(result.accepts("a"), "Character class NFA should accept 'a'");
        assertTrue(result.accepts("m"), "Character class NFA should accept 'm'");
        assertTrue(result.accepts("z"), "Character class NFA should accept 'z'");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("A"), "Character class NFA should not accept 'A'");
        assertFalse(result.accepts("1"), "Character class NFA should not accept '1'");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithNegation() throws Exception {
        // Test character class with negation
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "^abc");
        assertNotNull(result, "Negated character class NFA should not be null");
        assertFalse(result.accepts("a"), "Negated character class NFA should not accept 'a'");
        assertFalse(result.accepts("b"), "Negated character class NFA should not accept 'b'");
        assertFalse(result.accepts("c"), "Negated character class NFA should not accept 'c'");
        assertTrue(result.accepts("d"), "Negated character class NFA should accept 'd'");
        assertTrue(result.accepts("1"), "Negated character class NFA should accept '1'");
        assertTrue(result.accepts("@"), "Negated character class NFA should accept '@'");
        assertFalse(result.accepts(""), "Negated character class NFA should not accept empty string");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithMixedContent() throws Exception {
        // Test character class with mixed content
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "a-zA-Z0-9");
        assertNotNull(result, "Mixed character class NFA should not be null");
        assertTrue(result.accepts("a"), "Mixed character class NFA should accept 'a'");
        assertTrue(result.accepts("z"), "Mixed character class NFA should accept 'z'");
        assertTrue(result.accepts("A"), "Mixed character class NFA should accept 'A'");
        assertTrue(result.accepts("Z"), "Mixed character class NFA should accept 'Z'");
        assertTrue(result.accepts("0"), "Mixed character class NFA should accept '0'");
        assertTrue(result.accepts("9"), "Mixed character class NFA should accept '9'");
        assertFalse(result.accepts(""), "Mixed character class NFA should not accept empty string");
        assertFalse(result.accepts("@"), "Mixed character class NFA should not accept '@'");
        assertFalse(result.accepts("#"), "Mixed character class NFA should not accept '#'");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithEscapeSequences() throws Exception {
        // Test character class with escape sequences
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "\\t\\n\\r");
        assertNotNull(result, "Character class NFA with escape sequences should not be null");
        assertTrue(result.accepts("\t"), "Character class NFA should accept tab");
        assertTrue(result.accepts("\n"), "Character class NFA should accept newline");
        assertTrue(result.accepts("\r"), "Character class NFA should accept carriage return");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("a"), "Character class NFA should not accept 'a'");
        assertFalse(result.accepts(" "), "Character class NFA should not accept space");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithHexSequences() throws Exception {
        // Test character class with hex sequences
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "\\x41\\x42\\x43");
        assertNotNull(result, "Character class NFA with hex sequences should not be null");
        assertTrue(result.accepts("A"), "Character class NFA should accept 'A' (\\x41)");
        assertTrue(result.accepts("B"), "Character class NFA should accept 'B' (\\x42)");
        assertTrue(result.accepts("C"), "Character class NFA should accept 'C' (\\x43)");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("a"), "Character class NFA should not accept 'a'");
        assertFalse(result.accepts("D"), "Character class NFA should not accept 'D'");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithHexRange() throws Exception {
        // Test character class with hex range
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "\\x41-\\x5A");
        assertNotNull(result, "Character class NFA with hex range should not be null");
        assertTrue(result.accepts("A"), "Character class NFA should accept 'A' (\\x41)");
        assertTrue(result.accepts("M"), "Character class NFA should accept 'M' (\\x4D)");
        assertTrue(result.accepts("Z"), "Character class NFA should accept 'Z' (\\x5A)");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("a"), "Character class NFA should not accept 'a' (\\x61)");
        assertFalse(result.accepts("@"), "Character class NFA should not accept '@' (\\x40)");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithLiteralHyphen() throws Exception {
        // Test character class with literal hyphen
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "-abc");
        assertNotNull(result, "Character class NFA with literal hyphen should not be null");
        assertTrue(result.accepts("-"), "Character class NFA should accept '-'");
        assertTrue(result.accepts("a"), "Character class NFA should accept 'a'");
        assertTrue(result.accepts("b"), "Character class NFA should accept 'b'");
        assertTrue(result.accepts("c"), "Character class NFA should accept 'c'");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("d"), "Character class NFA should not accept 'd'");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithHyphenAtEnd() throws Exception {
        // Test character class with hyphen at end
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "abc-");
        assertNotNull(result, "Character class NFA with hyphen at end should not be null");
        assertTrue(result.accepts("a"), "Character class NFA should accept 'a'");
        assertTrue(result.accepts("b"), "Character class NFA should accept 'b'");
        assertTrue(result.accepts("c"), "Character class NFA should accept 'c'");
        assertTrue(result.accepts("-"), "Character class NFA should accept '-'");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("d"), "Character class NFA should not accept 'd'");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithEscapedHyphen() throws Exception {
        // Test character class with escaped hyphen
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "a\\-c");
        assertNotNull(result, "Character class NFA with escaped hyphen should not be null");
        assertTrue(result.accepts("a"), "Character class NFA should accept 'a'");
        assertTrue(result.accepts("-"), "Character class NFA should accept '-'");
        assertTrue(result.accepts("c"), "Character class NFA should accept 'c'");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("b"), "Character class NFA should not accept 'b'");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithWhitespace() throws Exception {
        // Test character class with whitespace
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "\\s");
        assertNotNull(result, "Character class NFA with whitespace should not be null");
        assertTrue(result.accepts(" "), "Character class NFA should accept space");
        assertTrue(result.accepts("\t"), "Character class NFA should accept tab");
        assertTrue(result.accepts("\n"), "Character class NFA should accept newline");
        assertTrue(result.accepts("\r"), "Character class NFA should accept carriage return");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("a"), "Character class NFA should not accept 'a'");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithNegatedWhitespace() throws Exception {
        // Test character class with negated whitespace
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "^\\s");
        assertNotNull(result, "Character class NFA with negated whitespace should not be null");
        assertFalse(result.accepts(" "), "Character class NFA should not accept space");
        assertFalse(result.accepts("\t"), "Character class NFA should not accept tab");
        assertFalse(result.accepts("\n"), "Character class NFA should not accept newline");
        assertFalse(result.accepts("\r"), "Character class NFA should not accept carriage return");
        assertTrue(result.accepts("a"), "Character class NFA should accept 'a'");
        assertTrue(result.accepts("1"), "Character class NFA should accept '1'");
        assertTrue(result.accepts("@"), "Character class NFA should accept '@'");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithComplexRange() throws Exception {
        // Test character class with complex range
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "a-zA-Z0-9_");
        assertNotNull(result, "Character class NFA with complex range should not be null");
        assertTrue(result.accepts("a"), "Character class NFA should accept 'a'");
        assertTrue(result.accepts("z"), "Character class NFA should accept 'z'");
        assertTrue(result.accepts("A"), "Character class NFA should accept 'A'");
        assertTrue(result.accepts("Z"), "Character class NFA should accept 'Z'");
        assertTrue(result.accepts("0"), "Character class NFA should accept '0'");
        assertTrue(result.accepts("9"), "Character class NFA should accept '9'");
        assertTrue(result.accepts("_"), "Character class NFA should accept '_'");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("@"), "Character class NFA should not accept '@'");
        assertFalse(result.accepts("#"), "Character class NFA should not accept '#'");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithSpecialCharacters() throws Exception {
        // Test character class with special characters
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "!@#$%^&*()");
        assertNotNull(result, "Character class NFA with special characters should not be null");
        assertTrue(result.accepts("!"), "Character class NFA should accept '!'");
        assertTrue(result.accepts("@"), "Character class NFA should accept '@'");
        assertTrue(result.accepts("#"), "Character class NFA should accept '#'");
        assertTrue(result.accepts("$"), "Character class NFA should accept '$'");
        assertTrue(result.accepts("%"), "Character class NFA should accept '%'");
        assertTrue(result.accepts("^"), "Character class NFA should accept '^'");
        assertTrue(result.accepts("&"), "Character class NFA should accept '&'");
        assertTrue(result.accepts("*"), "Character class NFA should accept '*'");
        assertTrue(result.accepts("("), "Character class NFA should accept '('");
        assertTrue(result.accepts(")"), "Character class NFA should accept ')'");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("a"), "Character class NFA should not accept 'a'");
        assertFalse(result.accepts("1"), "Character class NFA should not accept '1'");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithUnicodeRange() throws Exception {
        // Test character class with Unicode range
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "\\x20-\\x7E");
        assertNotNull(result, "Character class NFA with Unicode range should not be null");
        assertTrue(result.accepts(" "), "Character class NFA should accept space (\\x20)");
        assertTrue(result.accepts("A"), "Character class NFA should accept 'A' (\\x41)");
        assertTrue(result.accepts("z"), "Character class NFA should accept 'z' (\\x7A)");
        assertTrue(result.accepts("~"), "Character class NFA should accept '~' (\\x7E)");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("\t"), "Character class NFA should not accept tab (\\x09)");
        assertFalse(result.accepts("\n"), "Character class NFA should not accept newline (\\x0A)");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithMixedEscapeSequences() throws Exception {
        // Test character class with mixed escape sequences
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "\\t\\n\\r\\x20-\\x7E");
        assertNotNull(result, "Character class NFA with mixed escape sequences should not be null");
        assertTrue(result.accepts("\t"), "Character class NFA should accept tab");
        assertTrue(result.accepts("\n"), "Character class NFA should accept newline");
        assertTrue(result.accepts("\r"), "Character class NFA should accept carriage return");
        assertTrue(result.accepts(" "), "Character class NFA should accept space");
        assertTrue(result.accepts("A"), "Character class NFA should accept 'A'");
        assertTrue(result.accepts("z"), "Character class NFA should accept 'z'");
        assertTrue(result.accepts("~"), "Character class NFA should accept '~'");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("a"), "Character class NFA should not accept 'a'");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithNegatedRange() throws Exception {
        // Test character class with negated range
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "^a-z");
        assertNotNull(result, "Character class NFA with negated range should not be null");
        assertFalse(result.accepts("a"), "Character class NFA should not accept 'a'");
        assertFalse(result.accepts("m"), "Character class NFA should not accept 'm'");
        assertFalse(result.accepts("z"), "Character class NFA should not accept 'z'");
        assertTrue(result.accepts("A"), "Character class NFA should accept 'A'");
        assertTrue(result.accepts("1"), "Character class NFA should accept '1'");
        assertTrue(result.accepts("@"), "Character class NFA should accept '@'");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithNegatedSpecialCharacters() throws Exception {
        // Test character class with negated special characters
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "^!@#$%^&*()");
        assertNotNull(result, "Character class NFA with negated special characters should not be null");
        assertFalse(result.accepts("!"), "Character class NFA should not accept '!'");
        assertFalse(result.accepts("@"), "Character class NFA should not accept '@'");
        assertFalse(result.accepts("#"), "Character class NFA should not accept '#'");
        assertFalse(result.accepts("$"), "Character class NFA should not accept '$'");
        assertFalse(result.accepts("%"), "Character class NFA should not accept '%'");
        assertFalse(result.accepts("^"), "Character class NFA should not accept '^'");
        assertFalse(result.accepts("&"), "Character class NFA should not accept '&'");
        assertFalse(result.accepts("*"), "Character class NFA should not accept '*'");
        assertFalse(result.accepts("("), "Character class NFA should not accept '('");
        assertFalse(result.accepts(")"), "Character class NFA should not accept ')'");
        assertTrue(result.accepts("a"), "Character class NFA should accept 'a'");
        assertTrue(result.accepts("1"), "Character class NFA should accept '1'");
        assertTrue(result.accepts(" "), "Character class NFA should accept space");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithComplexNegation() throws Exception {
        // Test character class with complex negation
        NFA result = (NFA) invokePrivateMethod("createCharacterClassNFA", "^a-zA-Z0-9_");
        assertNotNull(result, "Character class NFA with complex negation should not be null");
        assertFalse(result.accepts("a"), "Character class NFA should not accept 'a'");
        assertFalse(result.accepts("z"), "Character class NFA should not accept 'z'");
        assertFalse(result.accepts("A"), "Character class NFA should not accept 'A'");
        assertFalse(result.accepts("Z"), "Character class NFA should not accept 'Z'");
        assertFalse(result.accepts("0"), "Character class NFA should not accept '0'");
        assertFalse(result.accepts("9"), "Character class NFA should not accept '9'");
        assertFalse(result.accepts("_"), "Character class NFA should not accept '_'");
        assertTrue(result.accepts("@"), "Character class NFA should accept '@'");
        assertTrue(result.accepts("#"), "Character class NFA should accept '#'");
        assertTrue(result.accepts(" "), "Character class NFA should accept space");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
    }
}
