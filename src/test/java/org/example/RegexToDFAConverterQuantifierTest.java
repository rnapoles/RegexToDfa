package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for quantifier-related methods of RegexToDFAConverter.
 * 
 * Single Responsibility: Tests only quantifier functionality
 * Open/Closed: Extensible for new quantifier scenarios
 * Liskov Substitution: Maintains consistent testing interface
 * Interface Segregation: Focused on quantifier testing only
 * Dependency Inversion: Depends on abstractions (quantifier interface)
 */
public class RegexToDFAConverterQuantifierTest extends RegexToDFAConverterBaseTest {
    
    @Test
    public void testParseQuantifierExact() throws Exception {
        // Test exact quantifier {n}
        Object result = invokePrivateMethod("parseQuantifier", "a{2}", 1);
        assertNotNull(result, "Exact quantifier should be parsed");
        
        // Test exact quantifier {0}
        result = invokePrivateMethod("parseQuantifier", "a{0}", 1);
        assertNotNull(result, "Exact quantifier {0} should be parsed");
        
        // Test exact quantifier {1}
        result = invokePrivateMethod("parseQuantifier", "a{1}", 1);
        assertNotNull(result, "Exact quantifier {1} should be parsed");
        
        // Test exact quantifier {10}
        result = invokePrivateMethod("parseQuantifier", "a{10}", 1);
        assertNotNull(result, "Exact quantifier {10} should be parsed");
    }
    
    @Test
    public void testParseQuantifierRange() throws Exception {
        // Test range quantifier {n,m}
        Object result = invokePrivateMethod("parseQuantifier", "a{2,4}", 1);
        assertNotNull(result, "Range quantifier should be parsed");
        
        // Test range quantifier {0,1}
        result = invokePrivateMethod("parseQuantifier", "a{0,1}", 1);
        assertNotNull(result, "Range quantifier {0,1} should be parsed");
        
        // Test range quantifier {1,10}
        result = invokePrivateMethod("parseQuantifier", "a{1,10}", 1);
        assertNotNull(result, "Range quantifier {1,10} should be parsed");
    }
    
    @Test
    public void testParseQuantifierAtLeast() throws Exception {
        // Test at-least quantifier {n,}
        Object result = invokePrivateMethod("parseQuantifier", "a{2,}", 1);
        assertNotNull(result, "At-least quantifier should be parsed");
        
        // Test at-least quantifier {0,}
        result = invokePrivateMethod("parseQuantifier", "a{0,}", 1);
        assertNotNull(result, "At-least quantifier {0,} should be parsed");
        
        // Test at-least quantifier {1,}
        result = invokePrivateMethod("parseQuantifier", "a{1,}", 1);
        assertNotNull(result, "At-least quantifier {1,} should be parsed");
    }
    
    @Test
    public void testParseQuantifierAtMost() throws Exception {
        // Test at-most quantifier {,m}
        Object result = invokePrivateMethod("parseQuantifier", "a{,4}", 1);
        assertNotNull(result, "At-most quantifier should be parsed");
        
        // Test at-most quantifier {,0}
        result = invokePrivateMethod("parseQuantifier", "a{,0}", 1);
        assertNotNull(result, "At-most quantifier {,0} should be parsed");
        
        // Test at-most quantifier {,1}
        result = invokePrivateMethod("parseQuantifier", "a{,1}", 1);
        assertNotNull(result, "At-most quantifier {,1} should be parsed");
    }
    
    @Test
    public void testParseQuantifierInvalidEmpty() throws Exception {
        // Test empty quantifier {}
        assertThrowsIllegalArgumentException("parseQuantifier", "a{}", "Invalid quantifier: empty braces {}");
    }
    
    @Test
    public void testParseQuantifierInvalidNegative() throws Exception {
        // Test negative quantifier
        assertThrowsIllegalArgumentException("parseQuantifier", "a{-1}", "Invalid quantifier: negative number");
        assertThrowsIllegalArgumentException("parseQuantifier", "a{2,-1}", "Invalid quantifier: negative number");
        assertThrowsIllegalArgumentException("parseQuantifier", "a{-1,2}", "Invalid quantifier: negative number");
    }
    
    @Test
    public void testParseQuantifierInvalidRange() throws Exception {
        // Test invalid range (upper bound less than lower bound)
        assertThrowsIllegalArgumentException("parseQuantifier", "a{4,2}", "Invalid quantifier: upper bound less than lower bound");
        assertThrowsIllegalArgumentException("parseQuantifier", "a{10,5}", "Invalid quantifier: upper bound less than lower bound");
    }
    
    @Test
    public void testParseQuantifierInvalidFormat() throws Exception {
        // Test invalid format
        assertThrowsIllegalArgumentException("parseQuantifier", "a{,}", "Invalid quantifier: {,}");
        assertThrowsIllegalArgumentException("parseQuantifier", "a{abc}", "Invalid quantifier: non-numeric value");
        assertThrowsIllegalArgumentException("parseQuantifier", "a{2,abc}", "Invalid quantifier: non-numeric bounds");
        assertThrowsIllegalArgumentException("parseQuantifier", "a{abc,2}", "Invalid quantifier: non-numeric bounds");
    }
    
    @Test
    public void testParseQuantifierNotQuantifier() throws Exception {
        // Test when it's not a quantifier
        Object result = invokePrivateMethod("parseQuantifier", "abc", 0);
        assertNull(result, "Non-quantifier should return null");
        
        result = invokePrivateMethod("parseQuantifier", "a{", 1);
        assertNull(result, "Incomplete quantifier should return null");
    }
    
    @Test
    public void testGenerateExactlyNQuantifier() throws Exception {
        // Test exact quantifier generation
        String result1 = (String) invokePrivateMethod("generateExactlyNQuantifier", 0);
        assertEquals("", result1, "Exactly 0 should return empty string");
        
        String result2 = (String) invokePrivateMethod("generateExactlyNQuantifier", 1);
        assertEquals("", result2, "Exactly 1 should return empty string");
        
        String result3 = (String) invokePrivateMethod("generateExactlyNQuantifier", 2);
        assertEquals("&", result3, "Exactly 2 should return one concatenation operator");
        
        String result4 = (String) invokePrivateMethod("generateExactlyNQuantifier", 3);
        assertEquals("&&", result4, "Exactly 3 should return two concatenation operators");
        
        String result5 = (String) invokePrivateMethod("generateExactlyNQuantifier", 5);
        assertEquals("&&&&", result5, "Exactly 5 should return four concatenation operators");
    }
    
    @Test
    public void testGenerateAtLeastNQuantifier() throws Exception {
        // Test at-least quantifier generation
        String result1 = (String) invokePrivateMethod("generateAtLeastNQuantifier", 0);
        assertEquals("*", result1, "At least 0 should return star");
        
        String result2 = (String) invokePrivateMethod("generateAtLeastNQuantifier", 1);
        assertEquals("+", result2, "At least 1 should return plus");
        
        String result3 = (String) invokePrivateMethod("generateAtLeastNQuantifier", 2);
        assertEquals("&*", result3, "At least 2 should return concatenation and star");
        
        String result4 = (String) invokePrivateMethod("generateAtLeastNQuantifier", 3);
        assertEquals("&&*", result4, "At least 3 should return two concatenations and star");
        
        String result5 = (String) invokePrivateMethod("generateAtLeastNQuantifier", 5);
        assertEquals("&&&&*", result5, "At least 5 should return four concatenations and star");
    }
    
    @Test
    public void testGenerateAtMostMQuantifier() throws Exception {
        // Test at-most quantifier generation
        String result1 = (String) invokePrivateMethod("generateAtMostMQuantifier", 0);
        assertEquals("", result1, "At most 0 should return empty string");
        
        String result2 = (String) invokePrivateMethod("generateAtMostMQuantifier", 1);
        assertEquals("?", result2, "At most 1 should return question");
        
        String result3 = (String) invokePrivateMethod("generateAtMostMQuantifier", 2);
        assertEquals("?&?", result3, "At most 2 should return two question operators with concatenation");
        
        String result4 = (String) invokePrivateMethod("generateAtMostMQuantifier", 3);
        assertEquals("?&?&?", result4, "At most 3 should return three question operators with concatenations");
        
        String result5 = (String) invokePrivateMethod("generateAtMostMQuantifier", 5);
        assertEquals("?&?&?&?&?", result5, "At most 5 should return five question operators with concatenations");
    }
    
    @Test
    public void testGenerateRangeQuantifier() throws Exception {
        // Test range quantifier generation
        String result1 = (String) invokePrivateMethod("generateRangeQuantifier", 2, 2);
        assertEquals("&", result1, "Range {2,2} should return concatenation");
        
        String result2 = (String) invokePrivateMethod("generateRangeQuantifier", 0, 1);
        assertEquals("?", result2, "Range {0,1} should return question");
        
        String result3 = (String) invokePrivateMethod("generateRangeQuantifier", 0, 3);
        assertEquals("?&?&?&?", result3, "Range {0,3} should return at-most quantifier");
        
        String result4 = (String) invokePrivateMethod("generateRangeQuantifier", 2, Integer.MAX_VALUE);
        assertEquals("&*", result4, "Range {2,∞} should return at-least quantifier");
        
        String result5 = (String) invokePrivateMethod("generateRangeQuantifier", 2, 4);
        assertEquals("&?&?", result5, "Range {2,4} should return concatenation and optional operators");
        
        String result6 = (String) invokePrivateMethod("generateRangeQuantifier", 3, 6);
        assertEquals("&&?&?&?", result6, "Range {3,6} should return concatenations and optional operators");
    }
    
    @Test
    public void testCreateExactQuantifierNFA() throws Exception {
        // Test exact quantifier NFA creation
        NFA operand = createSimpleNFA();
        
        NFA result1 = (NFA) invokePrivateMethod("createExactQuantifierNFA", operand, 0);
        assertNotNull(result1, "Exact quantifier NFA for 0 should not be null");
        assertTrue(result1.accepts(""), "Exact quantifier NFA for 0 should accept empty string");
        assertFalse(result1.accepts("a"), "Exact quantifier NFA for 0 should not accept 'a'");
        
        NFA result2 = (NFA) invokePrivateMethod("createExactQuantifierNFA", operand, 1);
        assertNotNull(result2, "Exact quantifier NFA for 1 should not be null");
        assertTrue(result2.accepts("a"), "Exact quantifier NFA for 1 should accept 'a'");
        assertFalse(result2.accepts(""), "Exact quantifier NFA for 1 should not accept empty string");
        assertFalse(result2.accepts("aa"), "Exact quantifier NFA for 1 should not accept 'aa'");
        
        NFA result3 = (NFA) invokePrivateMethod("createExactQuantifierNFA", operand, 2);
        assertNotNull(result3, "Exact quantifier NFA for 2 should not be null");
        assertTrue(result3.accepts("aa"), "Exact quantifier NFA for 2 should accept 'aa'");
        assertFalse(result3.accepts(""), "Exact quantifier NFA for 2 should not accept empty string");
        assertFalse(result3.accepts("a"), "Exact quantifier NFA for 2 should not accept 'a'");
        assertFalse(result3.accepts("aaa"), "Exact quantifier NFA for 2 should not accept 'aaa'");
    }
    
    @Test
    public void testCreateAtLeastQuantifierNFA() throws Exception {
        // Test at-least quantifier NFA creation
        NFA operand = createSimpleNFA();
        
        NFA result1 = (NFA) invokePrivateMethod("createAtLeastQuantifierNFA", operand, 0);
        assertNotNull(result1, "At-least quantifier NFA for 0 should not be null");
        assertTrue(result1.accepts(""), "At-least quantifier NFA for 0 should accept empty string");
        assertTrue(result1.accepts("a"), "At-least quantifier NFA for 0 should accept 'a'");
        assertTrue(result1.accepts("aa"), "At-least quantifier NFA for 0 should accept 'aa'");
        
        NFA result2 = (NFA) invokePrivateMethod("createAtLeastQuantifierNFA", operand, 1);
        assertNotNull(result2, "At-least quantifier NFA for 1 should not be null");
        assertFalse(result2.accepts(""), "At-least quantifier NFA for 1 should not accept empty string");
        assertTrue(result2.accepts("a"), "At-least quantifier NFA for 1 should accept 'a'");
        assertTrue(result2.accepts("aa"), "At-least quantifier NFA for 1 should accept 'aa'");
        
        NFA result3 = (NFA) invokePrivateMethod("createAtLeastQuantifierNFA", operand, 2);
        assertNotNull(result3, "At-least quantifier NFA for 2 should not be null");
        assertFalse(result3.accepts(""), "At-least quantifier NFA for 2 should not accept empty string");
        assertFalse(result3.accepts("a"), "At-least quantifier NFA for 2 should not accept 'a'");
        assertTrue(result3.accepts("aa"), "At-least quantifier NFA for 2 should accept 'aa'");
        assertTrue(result3.accepts("aaa"), "At-least quantifier NFA for 2 should accept 'aaa'");
    }
    
    @Test
    public void testCreateAtMostQuantifierNFA() throws Exception {
        // Test at-most quantifier NFA creation
        NFA operand = createSimpleNFA();
        
        NFA result1 = (NFA) invokePrivateMethod("createAtMostQuantifierNFA", operand, 0);
        assertNotNull(result1, "At-most quantifier NFA for 0 should not be null");
        assertTrue(result1.accepts(""), "At-most quantifier NFA for 0 should accept empty string");
        assertFalse(result1.accepts("a"), "At-most quantifier NFA for 0 should not accept 'a'");
        
        NFA result2 = (NFA) invokePrivateMethod("createAtMostQuantifierNFA", operand, 1);
        assertNotNull(result2, "At-most quantifier NFA for 1 should not be null");
        assertTrue(result2.accepts(""), "At-most quantifier NFA for 1 should accept empty string");
        assertTrue(result2.accepts("a"), "At-most quantifier NFA for 1 should accept 'a'");
        assertFalse(result2.accepts("aa"), "At-most quantifier NFA for 1 should not accept 'aa'");
        
        NFA result3 = (NFA) invokePrivateMethod("createAtMostQuantifierNFA", operand, 2);
        assertNotNull(result3, "At-most quantifier NFA for 2 should not be null");
        assertTrue(result3.accepts(""), "At-most quantifier NFA for 2 should accept empty string");
        assertTrue(result3.accepts("a"), "At-most quantifier NFA for 2 should accept 'a'");
        assertTrue(result3.accepts("aa"), "At-most quantifier NFA for 2 should accept 'aa'");
        assertFalse(result3.accepts("aaa"), "At-most quantifier NFA for 2 should not accept 'aaa'");
    }
    
    @Test
    public void testCreateRangeQuantifierNFA() throws Exception {
        // Test range quantifier NFA creation
        NFA operand = createSimpleNFA();
        
        NFA result1 = (NFA) invokePrivateMethod("createRangeQuantifierNFA", operand, 2, 2);
        assertNotNull(result1, "Range quantifier NFA for {2,2} should not be null");
        assertTrue(result1.accepts("aa"), "Range quantifier NFA for {2,2} should accept 'aa'");
        assertFalse(result1.accepts(""), "Range quantifier NFA for {2,2} should not accept empty string");
        assertFalse(result1.accepts("a"), "Range quantifier NFA for {2,2} should not accept 'a'");
        assertFalse(result1.accepts("aaa"), "Range quantifier NFA for {2,2} should not accept 'aaa'");
        
        NFA result2 = (NFA) invokePrivateMethod("createRangeQuantifierNFA", operand, 0, 1);
        assertNotNull(result2, "Range quantifier NFA for {0,1} should not be null");
        assertTrue(result2.accepts(""), "Range quantifier NFA for {0,1} should accept empty string");
        assertTrue(result2.accepts("a"), "Range quantifier NFA for {0,1} should accept 'a'");
        assertFalse(result2.accepts("aa"), "Range quantifier NFA for {0,1} should not accept 'aa'");
        
        NFA result3 = (NFA) invokePrivateMethod("createRangeQuantifierNFA", operand, 2, 4);
        assertNotNull(result3, "Range quantifier NFA for {2,4} should not be null");
        assertTrue(result3.accepts("aa"), "Range quantifier NFA for {2,4} should accept 'aa'");
        assertTrue(result3.accepts("aaa"), "Range quantifier NFA for {2,4} should accept 'aaa'");
        assertTrue(result3.accepts("aaaa"), "Range quantifier NFA for {2,4} should accept 'aaaa'");
        assertFalse(result3.accepts(""), "Range quantifier NFA for {2,4} should not accept empty string");
        assertFalse(result3.accepts("a"), "Range quantifier NFA for {2,4} should not accept 'a'");
        assertFalse(result3.accepts("aaaaa"), "Range quantifier NFA for {2,4} should not accept 'aaaaa'");
    }
    
    @Test
    public void testCreateRangeQuantifierNFAEdgeCases() throws Exception {
        // Test range quantifier NFA edge cases
        NFA operand = createSimpleNFA();
        
        // Test {0,0} - should be empty string
        NFA result1 = (NFA) invokePrivateMethod("createRangeQuantifierNFA", operand, 0, 0);
        assertNotNull(result1, "Range quantifier NFA for {0,0} should not be null");
        assertTrue(result1.accepts(""), "Range quantifier NFA for {0,0} should accept empty string");
        assertFalse(result1.accepts("a"), "Range quantifier NFA for {0,0} should not accept 'a'");
        
        // Test {0,∞} - should be star
        NFA result2 = (NFA) invokePrivateMethod("createRangeQuantifierNFA", operand, 0, Integer.MAX_VALUE);
        assertNotNull(result2, "Range quantifier NFA for {0,∞} should not be null");
        assertTrue(result2.accepts(""), "Range quantifier NFA for {0,∞} should accept empty string");
        assertTrue(result2.accepts("a"), "Range quantifier NFA for {0,∞} should accept 'a'");
        assertTrue(result2.accepts("aa"), "Range quantifier NFA for {0,∞} should accept 'aa'");
        
        // Test {1,∞} - should be plus
        NFA result3 = (NFA) invokePrivateMethod("createRangeQuantifierNFA", operand, 1, Integer.MAX_VALUE);
        assertNotNull(result3, "Range quantifier NFA for {1,∞} should not be null");
        assertFalse(result3.accepts(""), "Range quantifier NFA for {1,∞} should not accept empty string");
        assertTrue(result3.accepts("a"), "Range quantifier NFA for {1,∞} should accept 'a'");
        assertTrue(result3.accepts("aa"), "Range quantifier NFA for {1,∞} should accept 'aa'");
    }
}
