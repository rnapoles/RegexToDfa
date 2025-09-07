package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the validateRegex() method of RegexToDFAConverter.
 * 
 * Single Responsibility: Tests only regex validation functionality
 * Open/Closed: Extensible for new validation scenarios
 * Liskov Substitution: Maintains consistent testing interface
 * Interface Segregation: Focused on validation testing only
 * Dependency Inversion: Depends on abstractions (validation interface)
 */
public class RegexToDFAConverterValidationTest extends RegexToDFAConverterBaseTest {
    
    @Test
    public void testValidateValidRegexes() throws Exception {
        // Test that valid regexes don't throw exceptions
        for (String validRegex : VALID_REGEXES) {
            assertDoesNotThrow(() -> {
                invokePrivateMethod("validateRegex", validRegex);
            }, "Valid regex should not throw exception: " + validRegex);
        }
    }
    
    @Test
    public void testValidateInvalidOperatorAtStart() throws Exception {
        // Test operators at the start without operands
        assertThrowsIllegalArgumentException("validateRegex", "*a", "Invalid regex: * without operand");
        assertThrowsIllegalArgumentException("validateRegex", "+a", "Invalid regex: + without operand");
        assertThrowsIllegalArgumentException("validateRegex", "?a", "Invalid regex: ? without operand");
        assertThrowsIllegalArgumentException("validateRegex", "|a", "Invalid regex: | requires two operands");
    }
    
    @Test
    public void testValidateInvalidConcatenationOperator() throws Exception {
        // Test invalid concatenation operator
        assertThrowsIllegalArgumentException("validateRegex", ".", "Invalid regex: concatenation requires two operands");
    }
    
    @Test
    public void testValidateInvalidOperatorAtEnd() throws Exception {
        // Test operators at the end
        assertThrowsIllegalArgumentException("validateRegex", "a|", "Invalid regex: | requires two operands");
    }
    
    @Test
    public void testValidateIncompleteEscapeSequence() throws Exception {
        // Test incomplete escape sequence
        assertThrowsIllegalArgumentException("validateRegex", "\\", "Incomplete escape sequence");
    }
    
    @Test
    public void testValidateConsecutiveQuantifiers() throws Exception {
        // Test consecutive quantifiers
        assertThrowsIllegalArgumentException("validateRegex", "a**", "Invalid regex: * without operand");
        assertThrowsIllegalArgumentException("validateRegex", "a++", "Invalid regex: + without operand");
        assertThrowsIllegalArgumentException("validateRegex", "a??", "Invalid regex: ? without operand");
        assertThrowsIllegalArgumentException("validateRegex", "a*+", "Invalid regex: + without operand");
        assertThrowsIllegalArgumentException("validateRegex", "a+?", "Invalid regex: ? without operand");
    }
    
    @Test
    public void testValidateConsecutiveUnionOperators() throws Exception {
        // Test consecutive union operators
        assertThrowsIllegalArgumentException("validateRegex", "a||b", "Invalid regex: | requires two operands");
        assertThrowsIllegalArgumentException("validateRegex", "|a|b", "Invalid regex: | requires two operands");
        assertThrowsIllegalArgumentException("validateRegex", "a|b|", "Invalid regex: | requires two operands");
    }
    
    @Test
    public void testValidateConsecutiveConcatenationOperators() throws Exception {
        // Test consecutive concatenation operators
        assertThrowsIllegalArgumentException("validateRegex", "a..b", "Invalid regex: concatenation requires two operands");
    }
    
    @Test
    public void testValidateUnionAtStart() throws Exception {
        // Test union at start
        assertThrowsIllegalArgumentException("validateRegex", "|a", "Invalid regex: | requires two operands");
    }
    
    @Test
    public void testValidateUnionBeforeClosingParen() throws Exception {
        // Test union before closing parenthesis
        assertThrowsIllegalArgumentException("validateRegex", "(a|)", "Invalid regex: | requires two operands");
    }
    
    @Test
    public void testValidateMismatchedParentheses() throws Exception {
        // Test mismatched parentheses
        assertThrowsIllegalArgumentException("validateRegex", "(a", "Mismatched parentheses");
        assertThrowsIllegalArgumentException("validateRegex", "a)", "Mismatched parentheses");
        assertThrowsIllegalArgumentException("validateRegex", "((a)", "Mismatched parentheses");
        assertThrowsIllegalArgumentException("validateRegex", "(a))", "Mismatched parentheses");
    }
    
    @Test
    public void testValidateBalancedParentheses() throws Exception {
        // Test balanced parentheses (should not throw)
        String[] balancedParens = {
            "()", "(a)", "((a))", "(((a)))", "(a|b)", "((a)|(b))", "(a*|b+)"
        };
        
        for (String regex : balancedParens) {
            assertDoesNotThrow(() -> {
                invokePrivateMethod("validateRegex", regex);
            }, "Balanced parentheses should not throw exception: " + regex);
        }
    }
    
    @Test
    public void testValidateNestedParentheses() throws Exception {
        // Test nested parentheses (should be allowed)
        String[] nestedParens = {
            "((a))", "(((a)))", "((a|b))", "(((a)|(b)))", "((a*)|(b+))"
        };
        
        for (String regex : nestedParens) {
            assertDoesNotThrow(() -> {
                invokePrivateMethod("validateRegex", regex);
            }, "Nested parentheses should be allowed: " + regex);
        }
    }
    
    @Test
    public void testValidateComplexValidPatterns() throws Exception {
        // Test complex but valid patterns
        String[] complexValid = {
            "(a|b)*", "((a|b)|(c|d))*", "a*(b|c)+", "(a|b)*c?", 
            "((a|b)*|(c|d)*)*", "a+((b|c)*|d+)", "(a|(b|c))*"
        };
        
        for (String regex : complexValid) {
            assertDoesNotThrow(() -> {
                invokePrivateMethod("validateRegex", regex);
            }, "Complex valid pattern should not throw exception: " + regex);
        }
    }
    
    @Test
    public void testValidateEmptyString() throws Exception {
        // Test empty string (should be valid)
        assertDoesNotThrow(() -> {
            invokePrivateMethod("validateRegex", "");
        }, "Empty string should be valid");
    }
    
    @Test
    public void testValidateSingleCharacter() throws Exception {
        // Test single characters
        String[] singleChars = {"a", "b", "c", "1", "2", "3", "@", "#", "$"};
        
        for (String regex : singleChars) {
            assertDoesNotThrow(() -> {
                invokePrivateMethod("validateRegex", regex);
            }, "Single character should be valid: " + regex);
        }
    }
    
    @Test
    public void testValidateCharacterClasses() throws Exception {
        // Test character classes
        String[] charClasses = {
            "[a]", "[abc]", "[a-z]", "[0-9]", "[^a]", "[^abc]", 
            "[a-zA-Z]", "[0-9a-f]", "[\\s]", "[\\t\\n\\r]"
        };
        
        for (String regex : charClasses) {
            assertDoesNotThrow(() -> {
                invokePrivateMethod("validateRegex", regex);
            }, "Character class should be valid: " + regex);
        }
    }
    
    @Test
    public void testValidateEscapeSequences() throws Exception {
        // Test escape sequences
        String[] escapeSeqs = {
            "\\.", "\\*", "\\+", "\\?", "\\|", "\\(", "\\)", 
            "\\[", "\\]", "\\^", "\\$", "\\\\", "\\t", "\\n", "\\r"
        };
        
        for (String regex : escapeSeqs) {
            assertDoesNotThrow(() -> {
                invokePrivateMethod("validateRegex", regex);
            }, "Escape sequence should be valid: " + regex);
        }
    }
    
    @Test
    public void testValidateQuantifiers() throws Exception {
        // Test quantifiers
        String[] quantifiers = {
            "a*", "a+", "a?", "a{2}", "a{2,4}", "a{2,}", "a{,4}",
            "(a|b)*", "[abc]+", "\\d{3,6}"
        };
        
        for (String regex : quantifiers) {
            assertDoesNotThrow(() -> {
                invokePrivateMethod("validateRegex", regex);
            }, "Quantifier should be valid: " + regex);
        }
    }
    
    @Test
    public void testValidateInvalidQuantifierSyntax() throws Exception {
        // Test invalid quantifier syntax
        String[] invalidQuantifiers = {
            "a{}", "a{,}", "a{2,1}", "a{-1}", "a{abc}", "a{2,abc}"
        };
        
        for (String regex : invalidQuantifiers) {
            // These should be caught during preprocessing, not validation
            // But we test that validation doesn't crash
            assertDoesNotThrow(() -> {
                invokePrivateMethod("validateRegex", regex);
            }, "Invalid quantifier syntax should not crash validation: " + regex);
        }
    }
    
    @Test
    public void testValidateEdgeCases() throws Exception {
        // Test edge cases
        String[] edgeCases = {
            "a", "aa", "aaa", "a|a", "a*a", "a+a", "a?a",
            "a|b|c", "a*b*c*", "a+b+c+", "a?b?c?"
        };
        
        for (String regex : edgeCases) {
            assertDoesNotThrow(() -> {
                invokePrivateMethod("validateRegex", regex);
            }, "Edge case should be valid: " + regex);
        }
    }
}
