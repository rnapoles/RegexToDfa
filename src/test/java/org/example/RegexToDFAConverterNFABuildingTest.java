package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the buildNFAFromPostfix() method of RegexToDFAConverter.
 * 
 * Single Responsibility: Tests only NFA building functionality
 * Open/Closed: Extensible for new NFA building scenarios
 * Liskov Substitution: Maintains consistent testing interface
 * Interface Segregation: Focused on NFA building testing only
 * Dependency Inversion: Depends on abstractions (NFA interface)
 */
public class RegexToDFAConverterNFABuildingTest extends RegexToDFAConverterBaseTest {
    
    @Test
    public void testBuildNFAFromPostfixEmpty() throws Exception {
        // Test empty postfix
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "");
        assertNotNull(result, "Empty postfix should return NFA");
        assertTrue(result.accepts(""), "Empty postfix NFA should accept empty string");
        assertFalse(result.accepts("a"), "Empty postfix NFA should not accept non-empty string");
    }
    
    @Test
    public void testBuildNFAFromPostfixSingleOperand() throws Exception {
        // Test single operand
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "a");
        assertNotNull(result, "Single operand should return NFA");
        assertTrue(result.accepts("a"), "Single operand NFA should accept the character");
        assertFalse(result.accepts(""), "Single operand NFA should not accept empty string");
        assertFalse(result.accepts("b"), "Single operand NFA should not accept different character");
        assertFalse(result.accepts("aa"), "Single operand NFA should not accept multiple characters");
    }
    
    @Test
    public void testBuildNFAFromPostfixConcatenation() throws Exception {
        // Test concatenation
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "ab&");
        assertNotNull(result, "Concatenation should return NFA");
        assertTrue(result.accepts("ab"), "Concatenation NFA should accept 'ab'");
        assertFalse(result.accepts(""), "Concatenation NFA should not accept empty string");
        assertFalse(result.accepts("a"), "Concatenation NFA should not accept 'a'");
        assertFalse(result.accepts("b"), "Concatenation NFA should not accept 'b'");
        assertFalse(result.accepts("ba"), "Concatenation NFA should not accept 'ba'");
        assertFalse(result.accepts("abc"), "Concatenation NFA should not accept 'abc'");
    }
    
    @Test
    public void testBuildNFAFromPostfixUnion() throws Exception {
        // Test union
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "ab|");
        assertNotNull(result, "Union should return NFA");
        assertTrue(result.accepts("a"), "Union NFA should accept 'a'");
        assertTrue(result.accepts("b"), "Union NFA should accept 'b'");
        assertFalse(result.accepts(""), "Union NFA should not accept empty string");
        assertFalse(result.accepts("ab"), "Union NFA should not accept 'ab'");
        assertFalse(result.accepts("ba"), "Union NFA should not accept 'ba'");
    }
    
    @Test
    public void testBuildNFAFromPostfixStar() throws Exception {
        // Test Kleene star
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "a*");
        assertNotNull(result, "Star should return NFA");
        assertTrue(result.accepts(""), "Star NFA should accept empty string");
        assertTrue(result.accepts("a"), "Star NFA should accept 'a'");
        assertTrue(result.accepts("aa"), "Star NFA should accept 'aa'");
        assertTrue(result.accepts("aaa"), "Star NFA should accept 'aaa'");
        assertFalse(result.accepts("b"), "Star NFA should not accept 'b'");
        assertFalse(result.accepts("ab"), "Star NFA should not accept 'ab'");
    }
    
    @Test
    public void testBuildNFAFromPostfixPlus() throws Exception {
        // Test plus
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "a+");
        assertNotNull(result, "Plus should return NFA");
        assertFalse(result.accepts(""), "Plus NFA should not accept empty string");
        assertTrue(result.accepts("a"), "Plus NFA should accept 'a'");
        assertTrue(result.accepts("aa"), "Plus NFA should accept 'aa'");
        assertTrue(result.accepts("aaa"), "Plus NFA should accept 'aaa'");
        assertFalse(result.accepts("b"), "Plus NFA should not accept 'b'");
        assertFalse(result.accepts("ab"), "Plus NFA should not accept 'ab'");
    }
    
    @Test
    public void testBuildNFAFromPostfixQuestion() throws Exception {
        // Test question mark
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "a?");
        assertNotNull(result, "Question should return NFA");
        assertTrue(result.accepts(""), "Question NFA should accept empty string");
        assertTrue(result.accepts("a"), "Question NFA should accept 'a'");
        assertFalse(result.accepts("aa"), "Question NFA should not accept 'aa'");
        assertFalse(result.accepts("b"), "Question NFA should not accept 'b'");
        assertFalse(result.accepts("ab"), "Question NFA should not accept 'ab'");
    }
    
    @Test
    public void testBuildNFAFromPostfixCharacterClass() throws Exception {
        // Test character class
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "[abc]");
        assertNotNull(result, "Character class should return NFA");
        assertTrue(result.accepts("a"), "Character class NFA should accept 'a'");
        assertTrue(result.accepts("b"), "Character class NFA should accept 'b'");
        assertTrue(result.accepts("c"), "Character class NFA should accept 'c'");
        assertFalse(result.accepts(""), "Character class NFA should not accept empty string");
        assertFalse(result.accepts("d"), "Character class NFA should not accept 'd'");
        assertFalse(result.accepts("ab"), "Character class NFA should not accept 'ab'");
    }
    
    @Test
    public void testBuildNFAFromPostfixEscapeSequence() throws Exception {
        // Test escape sequence
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "\\.");
        assertNotNull(result, "Escape sequence should return NFA");
        assertTrue(result.accepts("."), "Escape sequence NFA should accept '.'");
        assertFalse(result.accepts(""), "Escape sequence NFA should not accept empty string");
        assertFalse(result.accepts("a"), "Escape sequence NFA should not accept 'a'");
    }
    
    @Test
    public void testBuildNFAFromPostfixWildcard() throws Exception {
        // Test wildcard
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", ".");
        assertNotNull(result, "Wildcard should return NFA");
        assertTrue(result.accepts("a"), "Wildcard NFA should accept 'a'");
        assertTrue(result.accepts("b"), "Wildcard NFA should accept 'b'");
        assertTrue(result.accepts("1"), "Wildcard NFA should accept '1'");
        assertTrue(result.accepts("@"), "Wildcard NFA should accept '@'");
        assertFalse(result.accepts(""), "Wildcard NFA should not accept empty string");
        assertFalse(result.accepts("ab"), "Wildcard NFA should not accept 'ab'");
    }
    
    @Test
    public void testBuildNFAFromPostfixComplexExpression() throws Exception {
        // Test complex expression: (a|b)*c
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "ab|*c&");
        assertNotNull(result, "Complex expression should return NFA");
        assertTrue(result.accepts("c"), "Complex expression NFA should accept 'c'");
        assertTrue(result.accepts("ac"), "Complex expression NFA should accept 'ac'");
        assertTrue(result.accepts("bc"), "Complex expression NFA should accept 'bc'");
        assertTrue(result.accepts("abc"), "Complex expression NFA should accept 'abc'");
        assertTrue(result.accepts("aabbc"), "Complex expression NFA should accept 'aabbc'");
        assertFalse(result.accepts(""), "Complex expression NFA should not accept empty string");
        assertFalse(result.accepts("a"), "Complex expression NFA should not accept 'a'");
        assertFalse(result.accepts("b"), "Complex expression NFA should not accept 'b'");
        assertFalse(result.accepts("ab"), "Complex expression NFA should not accept 'ab'");
        assertFalse(result.accepts("d"), "Complex expression NFA should not accept 'd'");
    }
    
    @Test
    public void testBuildNFAFromPostfixMultipleConcatenations() throws Exception {
        // Test multiple concatenations
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "ab&c&");
        assertNotNull(result, "Multiple concatenations should return NFA");
        assertTrue(result.accepts("abc"), "Multiple concatenations NFA should accept 'abc'");
        assertFalse(result.accepts(""), "Multiple concatenations NFA should not accept empty string");
        assertFalse(result.accepts("a"), "Multiple concatenations NFA should not accept 'a'");
        assertFalse(result.accepts("ab"), "Multiple concatenations NFA should not accept 'ab'");
        assertFalse(result.accepts("ac"), "Multiple concatenations NFA should not accept 'ac'");
        assertFalse(result.accepts("bc"), "Multiple concatenations NFA should not accept 'bc'");
    }
    
    @Test
    public void testBuildNFAFromPostfixMultipleUnions() throws Exception {
        // Test multiple unions
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "ab|c|");
        assertNotNull(result, "Multiple unions should return NFA");
        assertTrue(result.accepts("a"), "Multiple unions NFA should accept 'a'");
        assertTrue(result.accepts("b"), "Multiple unions NFA should accept 'b'");
        assertTrue(result.accepts("c"), "Multiple unions NFA should accept 'c'");
        assertFalse(result.accepts(""), "Multiple unions NFA should not accept empty string");
        assertFalse(result.accepts("ab"), "Multiple unions NFA should not accept 'ab'");
        assertFalse(result.accepts("ac"), "Multiple unions NFA should not accept 'ac'");
        assertFalse(result.accepts("bc"), "Multiple unions NFA should not accept 'bc'");
    }
    
    @Test
    public void testBuildNFAFromPostfixQuantifiers() throws Exception {
        // Test quantifiers
        NFA result1 = (NFA) invokePrivateMethod("buildNFAFromPostfix", "aQE2");
        assertNotNull(result1, "Exact quantifier should return NFA");
        assertTrue(result1.accepts("aa"), "Exact quantifier NFA should accept 'aa'");
        assertFalse(result1.accepts(""), "Exact quantifier NFA should not accept empty string");
        assertFalse(result1.accepts("a"), "Exact quantifier NFA should not accept 'a'");
        assertFalse(result1.accepts("aaa"), "Exact quantifier NFA should not accept 'aaa'");
        
        NFA result2 = (NFA) invokePrivateMethod("buildNFAFromPostfix", "aQR2,4");
        assertNotNull(result2, "Range quantifier should return NFA");
        assertTrue(result2.accepts("aa"), "Range quantifier NFA should accept 'aa'");
        assertTrue(result2.accepts("aaa"), "Range quantifier NFA should accept 'aaa'");
        assertTrue(result2.accepts("aaaa"), "Range quantifier NFA should accept 'aaaa'");
        assertFalse(result2.accepts(""), "Range quantifier NFA should not accept empty string");
        assertFalse(result2.accepts("a"), "Range quantifier NFA should not accept 'a'");
        assertFalse(result2.accepts("aaaaa"), "Range quantifier NFA should not accept 'aaaaa'");
        
        NFA result3 = (NFA) invokePrivateMethod("buildNFAFromPostfix", "aQA2");
        assertNotNull(result3, "At-least quantifier should return NFA");
        assertFalse(result3.accepts(""), "At-least quantifier NFA should not accept empty string");
        assertFalse(result3.accepts("a"), "At-least quantifier NFA should not accept 'a'");
        assertTrue(result3.accepts("aa"), "At-least quantifier NFA should accept 'aa'");
        assertTrue(result3.accepts("aaa"), "At-least quantifier NFA should accept 'aaa'");
        
        NFA result4 = (NFA) invokePrivateMethod("buildNFAFromPostfix", "aQM4");
        assertNotNull(result4, "At-most quantifier should return NFA");
        assertTrue(result4.accepts(""), "At-most quantifier NFA should accept empty string");
        assertTrue(result4.accepts("a"), "At-most quantifier NFA should accept 'a'");
        assertTrue(result4.accepts("aa"), "At-most quantifier NFA should accept 'aa'");
        assertTrue(result4.accepts("aaa"), "At-most quantifier NFA should accept 'aaa'");
        assertTrue(result4.accepts("aaaa"), "At-most quantifier NFA should accept 'aaaa'");
        assertFalse(result4.accepts("aaaaa"), "At-most quantifier NFA should not accept 'aaaaa'");
    }
    
    @Test
    public void testBuildNFAFromPostfixInvalidOperators() throws Exception {
        // Test invalid operators
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "*", "Invalid regex: * without operand");
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "+", "Invalid regex: + without operand");
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "?", "Invalid regex: ? without operand");
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "|", "Invalid regex: | requires two operands");
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "&", "Invalid regex: concatenation requires two operands");
    }
    
    @Test
    public void testBuildNFAFromPostfixInvalidQuantifier() throws Exception {
        // Test invalid quantifier
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "Q", "Invalid regex: quantifier without operand");
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "QE", "Invalid regex: quantifier without operand");
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "QA", "Invalid regex: quantifier without operand");
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "QM", "Invalid regex: quantifier without operand");
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "QR", "Invalid regex: quantifier without operand");
    }
    
    @Test
    public void testBuildNFAFromPostfixInvalidQuantifierOperator() throws Exception {
        // Test invalid quantifier operator
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "aQX", "Invalid quantifier operator: QX");
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "aQE", "Invalid quantifier operator: QE");
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "aQA", "Invalid quantifier operator: QA");
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "aQM", "Invalid quantifier operator: QM");
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "aQR", "Invalid quantifier operator: QR");
    }
    
    @Test
    public void testBuildNFAFromPostfixUnclosedCharacterClass() throws Exception {
        // Test unclosed character class
        assertThrowsIllegalArgumentException("buildNFAFromPostfix", "[abc", "Unclosed character class");
    }
    
    @Test
    public void testBuildNFAFromPostfixMultipleNFAs() throws Exception {
        // Test multiple NFAs (should be concatenated)
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "ab");
        assertNotNull(result, "Multiple NFAs should return concatenated NFA");
        assertTrue(result.accepts("ab"), "Multiple NFAs should accept concatenated string");
        assertFalse(result.accepts(""), "Multiple NFAs should not accept empty string");
        assertFalse(result.accepts("a"), "Multiple NFAs should not accept 'a'");
        assertFalse(result.accepts("b"), "Multiple NFAs should not accept 'b'");
        assertFalse(result.accepts("ba"), "Multiple NFAs should not accept 'ba'");
    }
    
    @Test
    public void testBuildNFAFromPostfixComplexQuantifierExpression() throws Exception {
        // Test complex expression with quantifiers
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "aQR2,4b*|");
        assertNotNull(result, "Complex quantifier expression should return NFA");
        assertTrue(result.accepts("aa"), "Complex quantifier expression should accept 'aa'");
        assertTrue(result.accepts("aaa"), "Complex quantifier expression should accept 'aaa'");
        assertTrue(result.accepts("aaaa"), "Complex quantifier expression should accept 'aaaa'");
        assertTrue(result.accepts(""), "Complex quantifier expression should accept empty string");
        assertTrue(result.accepts("b"), "Complex quantifier expression should accept 'b'");
        assertTrue(result.accepts("bb"), "Complex quantifier expression should accept 'bb'");
        assertFalse(result.accepts("a"), "Complex quantifier expression should not accept 'a'");
        assertFalse(result.accepts("aaaaa"), "Complex quantifier expression should not accept 'aaaaa'");
        assertFalse(result.accepts("ab"), "Complex quantifier expression should not accept 'ab'");
    }
    
    @Test
    public void testBuildNFAFromPostfixMixedOperators() throws Exception {
        // Test mixed operators
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "a*b+c?|");
        assertNotNull(result, "Mixed operators should return NFA");
        assertTrue(result.accepts(""), "Mixed operators should accept empty string");
        assertTrue(result.accepts("a"), "Mixed operators should accept 'a'");
        assertTrue(result.accepts("aa"), "Mixed operators should accept 'aa'");
        assertTrue(result.accepts("b"), "Mixed operators should accept 'b'");
        assertTrue(result.accepts("bb"), "Mixed operators should accept 'bb'");
        assertTrue(result.accepts("c"), "Mixed operators should accept 'c'");
        assertFalse(result.accepts("ab"), "Mixed operators should not accept 'ab'");
        assertFalse(result.accepts("ac"), "Mixed operators should not accept 'ac'");
        assertFalse(result.accepts("bc"), "Mixed operators should not accept 'bc'");
    }
    
    @Test
    public void testBuildNFAFromPostfixNestedExpression() throws Exception {
        // Test nested expression
        NFA result = (NFA) invokePrivateMethod("buildNFAFromPostfix", "ab|*cd|+|?");
        assertNotNull(result, "Nested expression should return NFA");
        assertTrue(result.accepts(""), "Nested expression should accept empty string");
        assertTrue(result.accepts("a"), "Nested expression should accept 'a'");
        assertTrue(result.accepts("b"), "Nested expression should accept 'b'");
        assertTrue(result.accepts("aa"), "Nested expression should accept 'aa'");
        assertTrue(result.accepts("bb"), "Nested expression should accept 'bb'");
        assertTrue(result.accepts("ab"), "Nested expression should accept 'ab'");
        assertTrue(result.accepts("c"), "Nested expression should accept 'c'");
        assertTrue(result.accepts("d"), "Nested expression should accept 'd'");
        assertTrue(result.accepts("cc"), "Nested expression should accept 'cc'");
        assertTrue(result.accepts("dd"), "Nested expression should accept 'dd'");
        assertTrue(result.accepts("cd"), "Nested expression should accept 'cd'");
        assertFalse(result.accepts("e"), "Nested expression should not accept 'e'");
        assertFalse(result.accepts("ac"), "Nested expression should not accept 'ac'");
        assertFalse(result.accepts("bd"), "Nested expression should not accept 'bd'");
    }
}
