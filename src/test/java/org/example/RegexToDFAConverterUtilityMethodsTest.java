package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

/**
 * Tests for utility methods of RegexToDFAConverter.
 * 
 * Single Responsibility: Tests only utility method functionality
 * Open/Closed: Extensible for new utility method scenarios
 * Liskov Substitution: Maintains consistent testing interface
 * Interface Segregation: Focused on utility method testing only
 * Dependency Inversion: Depends on abstractions (utility interface)
 */
public class RegexToDFAConverterUtilityMethodsTest extends RegexToDFAConverterBaseTest {
    
    @Test
    public void testIsOperand() throws Exception {
        // Test operands
        for (char operand : OPERANDS) {
            boolean result = (Boolean) invokePrivateMethod("isOperand", operand);
            assertTrue(result, "Character should be identified as operand: " + operand);
        }
        
        // Test special characters that are operands
        char[] specialOperands = {'.'};
        for (char operand : specialOperands) {
            boolean result = (Boolean) invokePrivateMethod("isOperand", operand);
            assertTrue(result, "Special character should be identified as operand: " + operand);
        }
        
        // Test backslash (part of escape sequence)
        boolean result = (Boolean) invokePrivateMethod("isOperand", '\\');
        assertTrue(result, "Backslash should be identified as operand");
    }
    
    @Test
    public void testIsOperandFalse() throws Exception {
        // Test operators (should not be operands)
        for (char operator : OPERATORS) {
            boolean result = (Boolean) invokePrivateMethod("isOperand", operator);
            assertFalse(result, "Character should not be identified as operand: " + operator);
        }
        
        // Test special characters that are not operands
        char[] specialNonOperands = {'(', ')', '[', ']', '^', '$', '&'};
        for (char nonOperand : specialNonOperands) {
            boolean result = (Boolean) invokePrivateMethod("isOperand", nonOperand);
            assertFalse(result, "Special character should not be identified as operand: " + nonOperand);
        }
    }
    
    @Test
    public void testIsOperator() throws Exception {
        // Test operators
        for (char operator : OPERATORS) {
            boolean result = (Boolean) invokePrivateMethod("isOperator", operator);
            assertTrue(result, "Character should be identified as operator: " + operator);
        }
    }
    
    @Test
    public void testIsOperatorFalse() throws Exception {
        // Test operands (should not be operators)
        for (char operand : OPERANDS) {
            boolean result = (Boolean) invokePrivateMethod("isOperator", operand);
            assertFalse(result, "Character should not be identified as operator: " + operand);
        }
        
        // Test special characters that are not operators
        for (char special : SPECIAL_CHARS) {
            boolean result = (Boolean) invokePrivateMethod("isOperator", special);
            assertFalse(result, "Special character should not be identified as operator: " + special);
        }
        
        // Test backslash
        boolean result = (Boolean) invokePrivateMethod("isOperator", '\\');
        assertFalse(result, "Backslash should not be identified as operator");
    }
    
    @Test
    public void testGetPrecedence() throws Exception {
        // Test operator precedence
        int unionPrecedence = (Integer) invokePrivateMethod("getPrecedence", '|');
        assertEquals(1, unionPrecedence, "Union should have precedence 1");
        
        int concatPrecedence = (Integer) invokePrivateMethod("getPrecedence", '&');
        assertEquals(2, concatPrecedence, "Concatenation should have precedence 2");
        
        int quantifierPrecedence = (Integer) invokePrivateMethod("getPrecedence", '*');
        assertEquals(3, quantifierPrecedence, "Star should have precedence 3");
        
        int plusPrecedence = (Integer) invokePrivateMethod("getPrecedence", '+');
        assertEquals(3, plusPrecedence, "Plus should have precedence 3");
        
        int questionPrecedence = (Integer) invokePrivateMethod("getPrecedence", '?');
        assertEquals(3, questionPrecedence, "Question should have precedence 3");
        
        int anchorPrecedence = (Integer) invokePrivateMethod("getPrecedence", '^');
        assertEquals(4, anchorPrecedence, "Anchor should have precedence 4");
        
        int dollarPrecedence = (Integer) invokePrivateMethod("getPrecedence", '$');
        assertEquals(4, dollarPrecedence, "Dollar should have precedence 4");
        
        int parenPrecedence = (Integer) invokePrivateMethod("getPrecedence", '(');
        assertEquals(0, parenPrecedence, "Parenthesis should have precedence 0");
        
        int closeParenPrecedence = (Integer) invokePrivateMethod("getPrecedence", ')');
        assertEquals(0, closeParenPrecedence, "Close parenthesis should have precedence 0");
    }
    
    @Test
    public void testGetPrecedenceInvalidOperator() throws Exception {
        // Test invalid operator
        assertThrowsIllegalArgumentException("getPrecedence", 'a', "Not an operator: a");
        assertThrowsIllegalArgumentException("getPrecedence", '1', "Not an operator: 1");
        assertThrowsIllegalArgumentException("getPrecedence", '@', "Not an operator: @");
        assertThrowsIllegalArgumentException("getPrecedence", '\\', "Not an operator: \\");
    }
    
    @Test
    public void testGetEscapedCharacter() throws Exception {
        // Test escape sequences
        char result1 = (Character) invokePrivateMethod("getEscapedCharacter", 't');
        assertEquals('\t', result1, "\\t should be converted to tab");
        
        char result2 = (Character) invokePrivateMethod("getEscapedCharacter", 'n');
        assertEquals('\n', result2, "\\n should be converted to newline");
        
        char result3 = (Character) invokePrivateMethod("getEscapedCharacter", 'r');
        assertEquals('\r', result3, "\\r should be converted to carriage return");
        
        char result4 = (Character) invokePrivateMethod("getEscapedCharacter", '\\');
        assertEquals('\\', result4, "\\\\ should be converted to backslash");
    }
    
    @Test
    public void testGetEscapedCharacterSpecialChars() throws Exception {
        // Test special characters
        char[] specialChars = {'[', ']', '(', ')', '*', '+', '?', '|', '.', '^', '$'};
        for (char c : specialChars) {
            char result = (Character) invokePrivateMethod("getEscapedCharacter", c);
            assertEquals(c, result, "Escaped special character should return itself: " + c);
        }
    }
    
    @Test
    public void testGetEscapedCharacterUnrecognized() throws Exception {
        // Test unrecognized escape sequences
        char result1 = (Character) invokePrivateMethod("getEscapedCharacter", 'z');
        assertEquals('z', result1, "Unrecognized escape sequence should return character as-is");
        
        char result2 = (Character) invokePrivateMethod("getEscapedCharacter", '@');
        assertEquals('@', result2, "Unrecognized escape sequence should return character as-is");
        
        char result3 = (Character) invokePrivateMethod("getEscapedCharacter", '1');
        assertEquals('1', result3, "Unrecognized escape sequence should return character as-is");
    }
    
    @Test
    public void testCreateCharacterNFA() throws Exception {
        // Test character NFA creation
        NFA nfa = (NFA) invokePrivateMethod("createCharacterNFA", 'a');
        assertNotNull(nfa, "Character NFA should not be null");
        assertTrue(nfa.accepts("a"), "Character NFA should accept the character");
        assertFalse(nfa.accepts(""), "Character NFA should not accept empty string");
        assertFalse(nfa.accepts("b"), "Character NFA should not accept different character");
        assertFalse(nfa.accepts("aa"), "Character NFA should not accept multiple characters");
    }
    
    @Test
    public void testCreateCharacterNFADifferentCharacters() throws Exception {
        // Test different characters
        char[] testChars = {'a', 'b', 'c', '1', '2', '3', '@', '#', '$'};
        for (char c : testChars) {
            NFA nfa = (NFA) invokePrivateMethod("createCharacterNFA", c);
            assertNotNull(nfa, "Character NFA should not be null for: " + c);
            assertTrue(nfa.accepts(String.valueOf(c)), "Character NFA should accept: " + c);
            assertFalse(nfa.accepts(""), "Character NFA should not accept empty string for: " + c);
        }
    }
    
    @Test
    public void testCreateEmptyStringNFA() throws Exception {
        // Test empty string NFA creation
        NFA nfa = (NFA) invokePrivateMethod("createEmptyStringNFA");
        assertNotNull(nfa, "Empty string NFA should not be null");
        assertTrue(nfa.accepts(""), "Empty string NFA should accept empty string");
        assertFalse(nfa.accepts("a"), "Empty string NFA should not accept non-empty string");
        assertFalse(nfa.accepts("ab"), "Empty string NFA should not accept non-empty string");
    }
    
    @Test
    public void testCreateEmptyStringDFA() throws Exception {
        // Test empty string DFA creation
        DFA dfa = (DFA) invokePrivateMethod("createEmptyStringDFA");
        assertNotNull(dfa, "Empty string DFA should not be null");
        assertTrue(dfa.accepts(""), "Empty string DFA should accept empty string");
        assertFalse(dfa.accepts("a"), "Empty string DFA should not accept non-empty string");
        assertFalse(dfa.accepts("ab"), "Empty string DFA should not accept non-empty string");
    }
    
    @Test
    public void testCreateWildcardNFA() throws Exception {
        // Test wildcard NFA creation
        NFA nfa = (NFA) invokePrivateMethod("createWildcardNFA");
        assertNotNull(nfa, "Wildcard NFA should not be null");
        assertTrue(nfa.accepts("a"), "Wildcard NFA should accept 'a'");
        assertTrue(nfa.accepts("b"), "Wildcard NFA should accept 'b'");
        assertTrue(nfa.accepts("1"), "Wildcard NFA should accept '1'");
        assertTrue(nfa.accepts("@"), "Wildcard NFA should accept '@'");
        assertTrue(nfa.accepts(" "), "Wildcard NFA should accept space");
        assertTrue(nfa.accepts("\t"), "Wildcard NFA should accept tab");
        assertTrue(nfa.accepts("\n"), "Wildcard NFA should accept newline");
        assertTrue(nfa.accepts("\r"), "Wildcard NFA should accept carriage return");
        assertFalse(nfa.accepts(""), "Wildcard NFA should not accept empty string");
        assertFalse(nfa.accepts("ab"), "Wildcard NFA should not accept multiple characters");
    }
    
    @Test
    public void testCreateCharacterClassNFA() throws Exception {
        // Test character class NFA creation
        NFA nfa = (NFA) invokePrivateMethod("createCharacterClassNFA", "abc");
        assertNotNull(nfa, "Character class NFA should not be null");
        assertTrue(nfa.accepts("a"), "Character class NFA should accept 'a'");
        assertTrue(nfa.accepts("b"), "Character class NFA should accept 'b'");
        assertTrue(nfa.accepts("c"), "Character class NFA should accept 'c'");
        assertFalse(nfa.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(nfa.accepts("d"), "Character class NFA should not accept 'd'");
        assertFalse(nfa.accepts("ab"), "Character class NFA should not accept multiple characters");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithRange() throws Exception {
        // Test character class NFA with range
        NFA nfa = (NFA) invokePrivateMethod("createCharacterClassNFA", "a-z");
        assertNotNull(nfa, "Character class NFA with range should not be null");
        assertTrue(nfa.accepts("a"), "Character class NFA should accept 'a'");
        assertTrue(nfa.accepts("m"), "Character class NFA should accept 'm'");
        assertTrue(nfa.accepts("z"), "Character class NFA should accept 'z'");
        assertFalse(nfa.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(nfa.accepts("A"), "Character class NFA should not accept 'A'");
        assertFalse(nfa.accepts("1"), "Character class NFA should not accept '1'");
    }
    
    @Test
    public void testCreateCharacterClassNFAWithNegation() throws Exception {
        // Test character class NFA with negation
        NFA nfa = (NFA) invokePrivateMethod("createCharacterClassNFA", "^abc");
        assertNotNull(nfa, "Negated character class NFA should not be null");
        assertFalse(nfa.accepts("a"), "Negated character class NFA should not accept 'a'");
        assertFalse(nfa.accepts("b"), "Negated character class NFA should not accept 'b'");
        assertFalse(nfa.accepts("c"), "Negated character class NFA should not accept 'c'");
        assertTrue(nfa.accepts("d"), "Negated character class NFA should accept 'd'");
        assertTrue(nfa.accepts("1"), "Negated character class NFA should accept '1'");
        assertTrue(nfa.accepts("@"), "Negated character class NFA should accept '@'");
        assertFalse(nfa.accepts(""), "Negated character class NFA should not accept empty string");
    }
    
    @Test
    public void testParseCharacterClass() throws Exception {
        // Test character class parsing
        @SuppressWarnings("unchecked")
        Set<Character> result1 = (Set<Character>) invokePrivateMethod("parseCharacterClass", "abc");
        assertNotNull(result1, "Parsed character class should not be null");
        assertEquals(3, result1.size(), "Character class should have 3 characters");
        assertTrue(result1.contains('a'), "Character class should contain 'a'");
        assertTrue(result1.contains('b'), "Character class should contain 'b'");
        assertTrue(result1.contains('c'), "Character class should contain 'c'");
    }
    
    @Test
    public void testParseCharacterClassWithRange() throws Exception {
        // Test character class parsing with range
        @SuppressWarnings("unchecked")
        Set<Character> result = (Set<Character>) invokePrivateMethod("parseCharacterClass", "a-z");
        assertNotNull(result, "Parsed character class with range should not be null");
        assertTrue(result.size() > 20, "Character class with range should have many characters");
        assertTrue(result.contains('a'), "Character class should contain 'a'");
        assertTrue(result.contains('m'), "Character class should contain 'm'");
        assertTrue(result.contains('z'), "Character class should contain 'z'");
        assertFalse(result.contains('A'), "Character class should not contain 'A'");
    }
    
    @Test
    public void testParseCharacterClassWithNegation() throws Exception {
        // Test character class parsing with negation
        @SuppressWarnings("unchecked")
        Set<Character> result = (Set<Character>) invokePrivateMethod("parseCharacterClass", "^abc");
        assertNotNull(result, "Parsed negated character class should not be null");
        assertTrue(result.size() > 90, "Negated character class should have many characters");
        assertFalse(result.contains('a'), "Negated character class should not contain 'a'");
        assertFalse(result.contains('b'), "Negated character class should not contain 'b'");
        assertFalse(result.contains('c'), "Negated character class should not contain 'c'");
        assertTrue(result.contains('d'), "Negated character class should contain 'd'");
        assertTrue(result.contains('1'), "Negated character class should contain '1'");
    }
    
    @Test
    public void testParseCharacterClassWithEscapeSequences() throws Exception {
        // Test character class parsing with escape sequences
        @SuppressWarnings("unchecked")
        Set<Character> result = (Set<Character>) invokePrivateMethod("parseCharacterClass", "\\t\\n\\r");
        assertNotNull(result, "Parsed character class with escape sequences should not be null");
        assertEquals(3, result.size(), "Character class should have 3 escape sequence characters");
        assertTrue(result.contains('\t'), "Character class should contain tab");
        assertTrue(result.contains('\n'), "Character class should contain newline");
        assertTrue(result.contains('\r'), "Character class should contain carriage return");
    }
    
    @Test
    public void testParseCharacterClassWithHexSequences() throws Exception {
        // Test character class parsing with hex sequences
        @SuppressWarnings("unchecked")
        Set<Character> result = (Set<Character>) invokePrivateMethod("parseCharacterClass", "\\x41\\x42\\x43");
        assertNotNull(result, "Parsed character class with hex sequences should not be null");
        assertEquals(3, result.size(), "Character class should have 3 hex sequence characters");
        assertTrue(result.contains('A'), "Character class should contain 'A' (\\x41)");
        assertTrue(result.contains('B'), "Character class should contain 'B' (\\x42)");
        assertTrue(result.contains('C'), "Character class should contain 'C' (\\x43)");
    }
    
    @Test
    public void testParseCharacterClassInvalidRange() throws Exception {
        // Test invalid character range
        assertThrowsIllegalArgumentException("parseCharacterClass", "z-a", "Invalid character range: z-a");
        assertThrowsIllegalArgumentException("parseCharacterClass", "9-0", "Invalid character range: 9-0");
    }
    
    @Test
    public void testParseCharacterClassInvalidHexRange() throws Exception {
        // Test invalid hex range
        assertThrowsIllegalArgumentException("parseCharacterClass", "\\x7E-\\x20", "Invalid regex: invalid hex range \\x7E-\\x20");
        assertThrowsIllegalArgumentException("parseCharacterClass", "\\x5A-\\x41", "Invalid regex: invalid hex range \\x5A-\\x41");
    }
    
    @Test
    public void testParseCharacterClassIncompleteEscapeSequence() throws Exception {
        // Test incomplete escape sequence
        assertThrowsIllegalArgumentException("parseCharacterClass", "\\", "Incomplete escape sequence");
    }
    
    @Test
    public void testParseCharacterClassInvalidHexCharacter() throws Exception {
        // Test invalid hex character
        assertThrowsIllegalArgumentException("parseCharacterClass", "\\xG", "Invalid regex: invalid hex character \\xG");
        assertThrowsIllegalArgumentException("parseCharacterClass", "\\xGG", "Invalid regex: invalid hex character \\xGG");
    }
}
