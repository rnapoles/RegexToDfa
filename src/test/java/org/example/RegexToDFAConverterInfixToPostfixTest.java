package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the infixToPostfix() method of RegexToDFAConverter.
 * 
 * Single Responsibility: Tests only infix to postfix conversion functionality
 * Open/Closed: Extensible for new conversion scenarios
 * Liskov Substitution: Maintains consistent testing interface
 * Interface Segregation: Focused on conversion testing only
 * Dependency Inversion: Depends on abstractions (conversion interface)
 */
public class RegexToDFAConverterInfixToPostfixTest extends RegexToDFAConverterBaseTest {
    
    @Test
    public void testInfixToPostfixNullInput() throws Exception {
        // Test null input
        String result = (String) invokePrivateMethod("infixToPostfix", (String) null);
        assertEquals("", result, "Null input should return empty string");
    }
    
    @Test
    public void testInfixToPostfixEmptyInput() throws Exception {
        // Test empty input
        String result = (String) invokePrivateMethod("infixToPostfix", "");
        assertEquals("", result, "Empty input should return empty string");
    }
    
    @Test
    public void testInfixToPostfixSimpleOperand() throws Exception {
        // Test simple operand
        String result = (String) invokePrivateMethod("infixToPostfix", "a");
        assertEquals("a", result, "Simple operand should remain unchanged");
    }
    
    @Test
    public void testInfixToPostfixConcatenation() throws Exception {
        // Test concatenation (implicit)
        String result = (String) invokePrivateMethod("infixToPostfix", "ab");
        assertEquals("ab&", result, "Concatenation should be converted to postfix");
    }
    
    @Test
    public void testInfixToPostfixUnion() throws Exception {
        // Test union
        String result = (String) invokePrivateMethod("infixToPostfix", "a|b");
        assertEquals("ab|", result, "Union should be converted to postfix");
    }
    
    @Test
    public void testInfixToPostfixStar() throws Exception {
        // Test Kleene star
        String result = (String) invokePrivateMethod("infixToPostfix", "a*");
        assertEquals("a*", result, "Star should be converted to postfix");
    }
    
    @Test
    public void testInfixToPostfixPlus() throws Exception {
        // Test plus
        String result = (String) invokePrivateMethod("infixToPostfix", "a+");
        assertEquals("a+", result, "Plus should be converted to postfix");
    }
    
    @Test
    public void testInfixToPostfixQuestion() throws Exception {
        // Test question mark
        String result = (String) invokePrivateMethod("infixToPostfix", "a?");
        assertEquals("a?", result, "Question should be converted to postfix");
    }
    
    @Test
    public void testInfixToPostfixParentheses() throws Exception {
        // Test parentheses
        String result = (String) invokePrivateMethod("infixToPostfix", "(a|b)");
        assertEquals("ab|", result, "Parentheses should be handled correctly");
    }
    
    @Test
    public void testInfixToPostfixComplexExpression() throws Exception {
        // Test complex expression
        String result = (String) invokePrivateMethod("infixToPostfix", "(a|b)*c");
        assertEquals("ab|*c&", result, "Complex expression should be converted correctly");
    }
    
    @Test
    public void testInfixToPostfixOperatorPrecedence() throws Exception {
        // Test operator precedence
        String result1 = (String) invokePrivateMethod("infixToPostfix", "a|b*c");
        assertEquals("abc*|", result1, "Star should have higher precedence than union");
        
        String result2 = (String) invokePrivateMethod("infixToPostfix", "a*b|c");
        assertEquals("a*bc|", result2, "Star should have higher precedence than union");
        
        String result3 = (String) invokePrivateMethod("infixToPostfix", "a|b&c");
        assertEquals("abc&|", result3, "Concatenation should have higher precedence than union");
    }
    
    @Test
    public void testInfixToPostfixMultipleConcatenations() throws Exception {
        // Test multiple concatenations
        String result = (String) invokePrivateMethod("infixToPostfix", "abc");
        assertEquals("ab&c&", result, "Multiple concatenations should be handled correctly");
    }
    
    @Test
    public void testInfixToPostfixMultipleUnions() throws Exception {
        // Test multiple unions
        String result = (String) invokePrivateMethod("infixToPostfix", "a|b|c");
        assertEquals("ab|c|", result, "Multiple unions should be handled correctly");
    }
    
    @Test
    public void testInfixToPostfixNestedParentheses() throws Exception {
        // Test nested parentheses
        String result = (String) invokePrivateMethod("infixToPostfix", "((a|b)|(c|d))");
        assertEquals("ab|cd||", result, "Nested parentheses should be handled correctly");
    }
    
    @Test
    public void testInfixToPostfixQuantifiersWithParentheses() throws Exception {
        // Test quantifiers with parentheses
        String result1 = (String) invokePrivateMethod("infixToPostfix", "(a|b)*");
        assertEquals("ab|*", result1, "Star with parentheses should be handled correctly");
        
        String result2 = (String) invokePrivateMethod("infixToPostfix", "(a|b)+");
        assertEquals("ab|+", result2, "Plus with parentheses should be handled correctly");
        
        String result3 = (String) invokePrivateMethod("infixToPostfix", "(a|b)?");
        assertEquals("ab|?", result3, "Question with parentheses should be handled correctly");
    }
    
    @Test
    public void testInfixToPostfixCharacterClass() throws Exception {
        // Test character class
        String result = (String) invokePrivateMethod("infixToPostfix", "[abc]");
        assertEquals("[abc]", result, "Character class should remain unchanged");
    }
    
    @Test
    public void testInfixToPostfixEscapeSequence() throws Exception {
        // Test escape sequence
        String result = (String) invokePrivateMethod("infixToPostfix", "\\.");
        assertEquals("\\.", result, "Escape sequence should remain unchanged");
    }
    
    @Test
    public void testInfixToPostfixQuantifiers() throws Exception {
        // Test quantifiers
        String result1 = (String) invokePrivateMethod("infixToPostfix", "a{2}");
        assertEquals("aQE2", result1, "Exact quantifier should be converted correctly");
        
        String result2 = (String) invokePrivateMethod("infixToPostfix", "a{2,4}");
        assertEquals("aQR2,4", result2, "Range quantifier should be converted correctly");
        
        String result3 = (String) invokePrivateMethod("infixToPostfix", "a{2,}");
        assertEquals("aQA2", result3, "At-least quantifier should be converted correctly");
        
        String result4 = (String) invokePrivateMethod("infixToPostfix", "a{,4}");
        assertEquals("aQM4", result4, "At-most quantifier should be converted correctly");
    }
    
    @Test
    public void testInfixToPostfixInvalidUnionAtStart() throws Exception {
        // Test invalid union at start
        assertThrowsIllegalArgumentException("infixToPostfix", "|a", "Invalid use of | operator");
    }
    
    @Test
    public void testInfixToPostfixInvalidUnionAtEnd() throws Exception {
        // Test invalid union at end
        assertThrowsIllegalArgumentException("infixToPostfix", "a|", "Invalid use of | operator");
    }
    
    @Test
    public void testInfixToPostfixMismatchedParentheses() throws Exception {
        // Test mismatched parentheses
        assertThrowsIllegalArgumentException("infixToPostfix", "(a", "Mismatched parentheses");
        assertThrowsIllegalArgumentException("infixToPostfix", "a)", "Mismatched parentheses");
    }
    
    @Test
    public void testInfixToPostfixUnclosedCharacterClass() throws Exception {
        // Test unclosed character class
        assertThrowsIllegalArgumentException("infixToPostfix", "[abc", "Unclosed character class");
    }
    
    @Test
    public void testInfixToPostfixComplexQuantifierExpression() throws Exception {
        // Test complex expression with quantifiers
        String result = (String) invokePrivateMethod("infixToPostfix", "a{2,4}|b*");
        assertEquals("aQR2,4b*|", result, "Complex quantifier expression should be converted correctly");
    }
    
    @Test
    public void testInfixToPostfixMixedOperators() throws Exception {
        // Test mixed operators
        String result = (String) invokePrivateMethod("infixToPostfix", "a*b+|c?");
        assertEquals("a*b+c?|", result, "Mixed operators should be handled correctly");
    }
    
    @Test
    public void testInfixToPostfixImplicitConcatenation() throws Exception {
        // Test implicit concatenation
        String result1 = (String) invokePrivateMethod("infixToPostfix", "a(b|c)");
        assertEquals("abc|&", result1, "Implicit concatenation with parentheses should work");
        
        String result2 = (String) invokePrivateMethod("infixToPostfix", "(a|b)c");
        assertEquals("ab|c&", result2, "Implicit concatenation with parentheses should work");
        
        String result3 = (String) invokePrivateMethod("infixToPostfix", "a[bcd]");
        assertEquals("a[bcd]&", result3, "Implicit concatenation with character class should work");
    }
    
    @Test
    public void testInfixToPostfixQuantifierWithParentheses() throws Exception {
        // Test quantifier with parentheses
        String result = (String) invokePrivateMethod("infixToPostfix", "(a|b){2,4}");
        assertEquals("ab|QR2,4", result, "Quantifier with parentheses should be handled correctly");
    }
    
    @Test
    public void testInfixToPostfixMultipleQuantifiers() throws Exception {
        // Test multiple quantifiers
        String result = (String) invokePrivateMethod("infixToPostfix", "a*b+");
        assertEquals("a*b+&", result, "Multiple quantifiers should be handled correctly");
    }
    
    @Test
    public void testInfixToPostfixComplexNestedExpression() throws Exception {
        // Test complex nested expression
        String result = (String) invokePrivateMethod("infixToPostfix", "((a|b)*|(c|d)+)?");
        assertEquals("ab|*cd|+|?", result, "Complex nested expression should be converted correctly");
    }
    
    @Test
    public void testInfixToPostfixWildcard() throws Exception {
        // Test wildcard
        String result = (String) invokePrivateMethod("infixToPostfix", ".");
        assertEquals(".", result, "Wildcard should remain unchanged");
    }
    
    @Test
    public void testInfixToPostfixWildcardWithOperators() throws Exception {
        // Test wildcard with operators
        String result1 = (String) invokePrivateMethod("infixToPostfix", ".*");
        assertEquals(".*", result1, "Wildcard with star should be handled correctly");
        
        String result2 = (String) invokePrivateMethod("infixToPostfix", ".+");
        assertEquals(".+", result2, "Wildcard with plus should be handled correctly");
        
        String result3 = (String) invokePrivateMethod("infixToPostfix", ".?");
        assertEquals(".?", result3, "Wildcard with question should be handled correctly");
    }
    
    @Test
    public void testInfixToPostfixLiteralQuantifier() throws Exception {
        // Test literal quantifier (not a valid quantifier)
        String result = (String) invokePrivateMethod("infixToPostfix", "a{abc}");
        assertEquals("a{abc}", result, "Invalid quantifier should be treated as literal");
    }
    
    @Test
    public void testInfixToPostfixEdgeCases() throws Exception {
        // Test edge cases
        String result1 = (String) invokePrivateMethod("infixToPostfix", "a|a");
        assertEquals("aa|", result1, "Same operands in union should work");
        
        String result2 = (String) invokePrivateMethod("infixToPostfix", "a*a");
        assertEquals("a*a&", result2, "Same operands in concatenation should work");
        
        String result3 = (String) invokePrivateMethod("infixToPostfix", "a**");
        assertEquals("a**", result3, "Multiple stars should be handled correctly");
    }
}
