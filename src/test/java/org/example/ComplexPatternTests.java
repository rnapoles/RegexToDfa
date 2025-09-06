package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for complex regex patterns combining multiple features.
 * Tests nested patterns, complex combinations, and real-world scenarios.
 */
public class ComplexPatternTests extends BaseRegexToDFATest {
    
    @Test
    public void testEmailLikePattern() {
        convertRegex("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}");
        
        assertMultipleTestCases(
            testCase("user@domain.com", true),
            testCase("test123@example.org", true),
            testCase("admin@site.net", true),
            testCase("", false),
            testCase("user", false),           // no @
            testCase("@domain.com", false),     // no username
            testCase("user@", false),          // no domain
            testCase("user@domain", false),    // no extension
            testCase("user@domain.", false),   // incomplete extension
            testCase("user@domain.c", false),  // extension too short
            testCase("user@domain.com.", false), // trailing dot
            testCase("user@@domain.com", false), // double @
            testCase("user@domain@com", false)   // @ in domain
        );
    }
    
    @Test
    public void testPhoneNumberPattern() {
        convertRegex("\\([0-9]{3}\\)[0-9]{3}-[0-9]{4}");
        
        assertMultipleTestCases(
            testCase("(123)456-7890", true),
            testCase("(555)123-4567", true),
            testCase("(000)000-0000", true),
            testCase("", false),
            testCase("1234567890", false),      // no formatting
            testCase("(123)4567890", false),    // missing dash
            testCase("(123)456-789", false),    // too few digits
            testCase("(123)456-78901", false),  // too many digits
            testCase("(12)456-7890", false),    // too few area code digits
            testCase("(1234)456-7890", false), // too many area code digits
            testCase("123-456-7890", false),   // no parentheses
            testCase("(abc)456-7890", false)    // letters in area code
        );
    }
    
    @Test
    public void testIdentifierPattern() {
        convertRegex("[a-zA-Z_][a-zA-Z0-9_]*");
        
        assertMultipleTestCases(
            testCase("variable", true),
            testCase("_private", true),
            testCase("camelCase", true),
            testCase("snake_case", true),
            testCase("UPPER_CASE", true),
            testCase("var123", true),
            testCase("_123", true),
            testCase("", false),
            testCase("123var", false),          // starts with digit
            testCase("var-name", false),        // contains dash
            testCase("var.name", false),        // contains dot
            testCase("var name", false),       // contains space
            testCase("var@name", false),        // contains @
            testCase("var+name", false)        // contains +
        );
    }
    
    @Test
    public void testVersionNumberPattern() {
        convertRegex("[0-9]+\\.[0-9]+(\\.[0-9]+)?");
        
        assertMultipleTestCases(
            testCase("1.0", true),
            testCase("1.0.0", true),
            testCase("10.25.100", true),
            testCase("0.0.0", true),
            testCase("", false),
            testCase("1", false),              // missing minor version
            testCase("1.", false),             // incomplete minor version
            testCase("1.0.", false),           // incomplete patch version
            testCase("1.0.0.0", false),        // too many parts
            testCase("a.0", false),             // non-numeric major
            testCase("1.b", false),            // non-numeric minor
            testCase("1.0.c", false),          // non-numeric patch
            testCase("1-0", false),            // wrong separator
            testCase("1 0", false)             // space separator
        );
    }
    
    @Test
    public void testTimePattern() {
        convertRegex("([0-1]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?");
        
        assertMultipleTestCases(
            testCase("00:00", true),
            testCase("12:30", true),
            testCase("23:59", true),
            testCase("9:30", true),
            testCase("00:00:00", true),
            testCase("12:30:45", true),
            testCase("23:59:59", true),
            testCase("", false),
            testCase("24:00", false),          // invalid hour
            testCase("12:60", false),          // invalid minute
            testCase("12:30:60", false),       // invalid second
            testCase("12", false),             // missing minutes
            testCase("12:", false),            // incomplete minutes
            testCase("12:30:", false),         // incomplete seconds
            testCase("a:30", false),           // non-numeric hour
            testCase("12:b", false),           // non-numeric minute
            testCase("12:30:c", false)         // non-numeric second
        );
    }
    
    @Test
    public void testNestedParentheses() {
        convertRegex("((a|b)(c|d))*");
        
        assertMultipleTestCases(
            testCase("", true),               // zero occurrences
            testCase("ac", true),             // one occurrence
            testCase("bd", true),             // one occurrence
            testCase("ad", true),             // one occurrence
            testCase("bc", true),             // one occurrence
            testCase("acbd", true),           // two occurrences
            testCase("acad", true),           // two occurrences
            testCase("bcbd", true),           // two occurrences
            testCase("acbdac", true),         // three occurrences
            testCase("a", false),             // incomplete
            testCase("c", false),             // incomplete
            testCase("ab", false),            // wrong combination
            testCase("cd", false),            // wrong combination
            testCase("ace", false),           // invalid character
            testCase("acb", false)            // incomplete second occurrence
        );
    }
    
    @Test
    public void testComplexQuantifierNesting() {
        convertRegex("(a+)*");
        
        assertMultipleTestCases(
            testCase("", true),               // zero occurrences
            testCase("a", true),              // one 'a'
            testCase("aa", true),             // two 'a's
            testCase("aaa", true),            // three 'a's
            testCase("aaaa", true),           // four 'a's
            testCase("b", false),             // different character
            testCase("ab", false),            // mixed characters
            testCase("aab", false)            // mixed characters
        );
    }
    
    @Test
    public void testAlternatingPattern() {
        convertRegex("(ab|cd|ef)*");
        
        assertMultipleTestCases(
            testCase("", true),               // zero occurrences
            testCase("ab", true),             // one occurrence
            testCase("cd", true),             // one occurrence
            testCase("ef", true),             // one occurrence
            testCase("abcd", true),           // two occurrences
            testCase("abef", true),           // two occurrences
            testCase("cdef", true),           // two occurrences
            testCase("abcdef", true),         // three occurrences
            testCase("a", false),             // incomplete
            testCase("ac", false),            // wrong combination
            testCase("ad", false),            // wrong combination
            testCase("ae", false),            // wrong combination
            testCase("af", false),            // wrong combination
            testCase("abc", false),           // incomplete third occurrence
            testCase("abce", false)           // incomplete third occurrence
        );
    }
    
    @Test
    public void testOptionalGroups() {
        convertRegex("(a|b)?(c|d)?");
        
        assertMultipleTestCases(
            testCase("", true),               // both optional
            testCase("a", true),              // first only
            testCase("b", true),              // first only
            testCase("c", true),              // second only
            testCase("d", true),              // second only
            testCase("ac", true),             // both
            testCase("ad", true),             // both
            testCase("bc", true),             // both
            testCase("bd", true),             // both
            testCase("aa", false),            // duplicate first
            testCase("cc", false),            // duplicate second
            testCase("ab", false),            // two firsts
            testCase("cd", false),            // two seconds
            testCase("e", false),             // invalid character
            testCase("ace", false)            // invalid character
        );
    }
    
    @Test
    public void testWhitespaceHandling() {
        convertRegex("\\s*[a-zA-Z]+\\s*");
        
        assertMultipleTestCases(
            testCase("word", true),           // no leading/trailing whitespace
            testCase(" word", true),          // leading space
            testCase("word ", true),          // trailing space
            testCase(" word ", true),         // both leading and trailing
            testCase("\tword\t", true),       // tab whitespace
            testCase("\nword\n", true),       // newline whitespace
            testCase(" \t word \t ", true),   // mixed whitespace
            testCase("", false),              // empty string
            testCase(" ", false),             // just whitespace
            testCase("word1", false),         // contains digit
            testCase("word word", false),     // contains space in middle
            testCase("word-word", false)      // contains dash
        );
    }
    
    @Test
    public void testComplexCharacterClass() {
        convertRegex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        
        assertMultipleTestCases(
            testCase("user@domain.com", true),
            testCase("user.name@domain.com", true),
            testCase("user_name@domain.com", true),
            testCase("user-name@domain.com", true),
            testCase("user123@domain.com", true),
            testCase("user@sub.domain.com", true),
            testCase("user@domain.co.uk", true),
            testCase("", false),
            testCase("user", false),          // no @
            testCase("@domain.com", false),   // no username
            testCase("user@", false),        // no domain
            testCase("user@domain", false),  // no extension
            testCase("user@domain.c", false), // extension too short
            testCase("user@domain.com.", false), // trailing dot
            testCase("user@domain@com", false), // @ in domain
            testCase("user@domain.com.", false) // trailing dot
        );
    }
}
